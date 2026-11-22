package com.Farm.NASMS.Service;

import com.Farm.NASMS.Repository.FarmerRepository;
import com.Farm.NASMS.Repository.ProduceOrderNRepo;
import com.Farm.NASMS.dto.ProduceDeliveryRequestN;
import com.Farm.NASMS.dto.ProduceDeliveryResponseN;
import com.Farm.NASMS.enums.ProductOrderStatusN;
import com.Farm.NASMS.model.Farmer;
import com.Farm.NASMS.model.ProduceDeliveryN;
import com.Farm.NASMS.security.AuthenticatedUserService;
import com.walter.toolkit.security_starter.audit.Auditable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProduceOrderNServiceImpl implements ProduceOrderNService {
    private final FarmerRepository farmerRepository;
    private final ProduceOrderNRepo produceOrderNRepo;
    private final AuthenticatedUserService authenticatedUserService;

    public ProduceOrderNServiceImpl(FarmerRepository farmerRepository, ProduceOrderNRepo produceOrderNRepo, AuthenticatedUserService authenticatedUserService) {
        this.farmerRepository = farmerRepository;
        this.produceOrderNRepo = produceOrderNRepo;
        this.authenticatedUserService = authenticatedUserService;
    }

    @Override
    @PreAuthorize("hasAnyRole('FARMER', 'ADMIN')")
    @Auditable(action = "PRODUCE_ORDER_PLACED")
    public ProduceDeliveryResponseN placeOrder(Long nationalId, ProduceDeliveryRequestN requestN) {
        if (!authenticatedUserService.isCurrentFarmerNationalId(nationalId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only place produce orders for your own account");
        }
        Farmer farmer = farmerRepository.findByNationalId(nationalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer not found"));
        if (requestN.getProductName() == null || requestN.getProductName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name is required");
        }
        if (requestN.getQuantityKg() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater than zero");
        }
        ProduceDeliveryN deliveryN = ProduceDeliveryN.builder()
                .orderCode(generateOrderCode())
                .farmer(farmer)
                .productName(requestN.getProductName())
                .quantityKg(requestN.getQuantityKg())
                .orderDate(LocalDateTime.now())
                .status(ProductOrderStatusN.PENDING)
                .build();
        ProduceDeliveryN savedOrder = produceOrderNRepo.save(deliveryN);

        return toResponse(savedOrder);
    }

    @Override
    @PreAuthorize("hasAnyRole('FARMER', 'ADMIN')")
    public List<ProduceDeliveryResponseN> getMyOrders(Long nationalId) { // <--- CHANGED return type to ProduceDeliveryResponseN
        if (!authenticatedUserService.isCurrentFarmerNationalId(nationalId)) { // <--- ADDED ownership check
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only view produce orders for your own account");
        }

        Farmer farmer = farmerRepository.findByNationalId(nationalId) // <--- ADDED lookup that defines `farmer`
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer not found"));

        return produceOrderNRepo.findByFarmerOrderByOrderDateDesc(farmer)
                .stream()
                .map(this::toResponse)
                .toList();
    }
    private ProduceDeliveryResponseN toResponse(ProduceDeliveryN order) {
        return new ProduceDeliveryResponseN(
                order.getId(),
                order.getOrderCode(),
                order.getProductName(),
                order.getQuantityKg(),
                order.getOrderDate(),
                order.getScheduledDate(),
                order.getStatus()
        );
    }
    private String generateOrderCode(){
        return "PROD-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0,8)
                        .toUpperCase();
    }
}
