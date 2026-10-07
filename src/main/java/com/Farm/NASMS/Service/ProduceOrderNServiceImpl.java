package com.Farm.NASMS.Service;

import com.Farm.NASMS.Repository.FarmerRepository;
import com.Farm.NASMS.Repository.ProduceOrderNRepo;
import com.Farm.NASMS.dto.ProduceDeliveryRequestN;
import com.Farm.NASMS.dto.ProduceDeliveryResponseN;
import com.Farm.NASMS.enums.ProductOrderStatusN;
import com.Farm.NASMS.model.Farmer;
import com.Farm.NASMS.model.ProduceDeliveryN;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProduceOrderNServiceImpl implements ProduceOrderNService {
    private final FarmerRepository farmerRepository;
    private final ProduceOrderNRepo produceOrderNRepo;

    public ProduceOrderNServiceImpl(FarmerRepository farmerRepository, ProduceOrderNRepo produceOrderNRepo) {
        this.farmerRepository = farmerRepository;
        this.produceOrderNRepo = produceOrderNRepo;
    }

    @Override
    public ProduceDeliveryResponseN placeOrder(Long nationalId, ProduceDeliveryRequestN requestN) {
        Farmer farmer = farmerRepository.findByNationalId(nationalId)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));
        if (requestN.getProductName() == null || requestN.getProductName().isBlank()) {
            throw new RuntimeException("Product Name required");
        }
        if (requestN.getQuantityKg() <= 0) {
            throw new RuntimeException("Quantity can't be zero");
        }
        ProduceDeliveryN deliveryN = ProduceDeliveryN.builder()
                .orderCode(generateOrderCode())
                .farmer(farmer)
                .productName(requestN.getProductName())
                .quantityKg(requestN.getQuantityKg())
                .orderDate(LocalDateTime.now())
                .status(ProductOrderStatusN.PENDING)
                .build();
        ProduceDeliveryN savedOrder =
                produceOrderNRepo.save(deliveryN);

        return new ProduceDeliveryResponseN(
                savedOrder.getId(),
                savedOrder.getOrderCode(),
                savedOrder.getProductName(),
                savedOrder.getQuantityKg(),
                savedOrder.getOrderDate(),
                savedOrder.getScheduledDate(),
                savedOrder.getStatus()
        );
    }

    @Override
    public List<ProduceDeliveryN> getMyOrders(Long nationalId) {
        Farmer farmer = farmerRepository.findByNationalId(nationalId)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));
        return produceOrderNRepo.findByFarmerOrderByOrderDateDesc(farmer);
    }
    private String generateOrderCode(){
        return "PROD-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0,8)
                        .toUpperCase();
    }
}
