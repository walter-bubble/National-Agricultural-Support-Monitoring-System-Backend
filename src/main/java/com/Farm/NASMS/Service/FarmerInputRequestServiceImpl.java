package com.Farm.NASMS.Service;

import com.Farm.NASMS.Repository.AgrovetOrderRepository;
import com.Farm.NASMS.Repository.AgrovetProductRepository;
import com.Farm.NASMS.Repository.FarmerInputRequestRepository;
import com.Farm.NASMS.dto.*;
import com.Farm.NASMS.enums.AgrovetOperationalStatus;
import com.Farm.NASMS.enums.AgrovetOrderStatus;
import com.Farm.NASMS.enums.AgrovetStatus;
import com.Farm.NASMS.enums.FarmerInputRequestStatus;
import com.Farm.NASMS.model.*;
import com.Farm.NASMS.security.AuthenticatedUserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class FarmerInputRequestServiceImpl implements FarmerInputRequestService {
    private final FarmerInputRequestRepository requestRepository;
    private final AgrovetProductRepository productRepository;
    private final AuthenticatedUserService authenticatedUserService;
    private final AgrovetOrderRepository orderRepository;

    public FarmerInputRequestServiceImpl(FarmerInputRequestRepository requestRepository, AgrovetProductRepository productRepository, AuthenticatedUserService authenticatedUserService, AgrovetOrderRepository orderRepository) {
        this.requestRepository = requestRepository;
        this.productRepository = productRepository;
        this.authenticatedUserService = authenticatedUserService;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public Long createRequest(FarmerInputRequestCreateDTO requestDto) {
        if (!authenticatedUserService.isFarmer()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only farmers can submit input requests");
        }
        Farmer farmer = authenticatedUserService.getCurrentFarmer();
        FarmerInputRequest request = new FarmerInputRequest();
        request.setFarmer(farmer);
        request.setStatus(FarmerInputRequestStatus.SUBMITTED);
        for (FarmerInputRequestItemDTO itemDTO : requestDto.items()) {
            AgrovetProducts product = productRepository
                    .findById(itemDTO.agrovetProductId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Selected product was not found"));
            if (!product.isActive()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The selected product is not active");
            }
            Agrovet agrovet = product.getAgrovet();
            if (agrovet.getStatus() != AgrovetStatus.APPROVED || agrovet.getOperationalStatus() != AgrovetOperationalStatus.ACTIVE) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The selected Agrovet is not approved and active");
            }
            if (!agrovet.getId().equals(itemDTO.preferredAgrovetId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The selected product does not belong to the chosen Agrovet");
            }
            BigDecimal quantity = itemDTO.quantity();
            if (quantity == null
                    || quantity.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requested quantity must be greater than zero");
            }
            if (product.getQuantityAvailable().compareTo(quantity) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock for " + product.getProductName());
            }
            FarmerInputRequestItem item = new FarmerInputRequestItem();
            item.setFarmerInputRequest(request);
            item.setSelectedProduct(product);
            item.setPreferredAgrovet(agrovet);
            item.setProductName(product.getProductName());
            item.setCategory(product.getCategory());
            item.setRequestedQuantity(quantity);
            item.setUnit(product.getUnit());
            request.getItems().add(item);
        }
        FarmerInputRequest savedRequest = requestRepository.save(request);
        return savedRequest.getId();
    }
    @Override
    @Transactional

    public void reviewRequest(Long requestId, FarmerInputRequestReviewDTO reviewDTO) {
        if (!authenticatedUserService.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only administrators can review input requests");
        }
        FarmerInputRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Input request not found"));

        if (request.getStatus() != FarmerInputRequestStatus.SUBMITTED && request.getStatus() != FarmerInputRequestStatus.UNDER_REVIEW) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only submitted or under-review requests can be reviewed");
        }
        if (reviewDTO.approved()) {
            request.setStatus(FarmerInputRequestStatus.APPROVED);
        } else {
            request.setStatus(FarmerInputRequestStatus.REJECTED);
        }
        requestRepository.save(request);
    }
    @Override
    @Transactional
    public Long forwardRequest(Long requestId) {

        if (!authenticatedUserService.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only administrators can forward input requests");
        }
        FarmerInputRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Input request not found"));

        if (request.getStatus() != FarmerInputRequestStatus.APPROVED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Only approved input requests can be forwarded"
            );
        }

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot forward a request without items");
        }

        // Do not create duplicate orders for this request.
        if (!orderRepository.findByFarmerInputRequest_Id(requestId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Orders already exist for this request");
        }

        // Group request items by their selected Agrovet.
        Map<Long, List<FarmerInputRequestItem>> itemsByAgrovet = new LinkedHashMap<>();

        for (FarmerInputRequestItem item : request.getItems()) {

            if (item.getPreferredAgrovet() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A requested item has no selected Agrovet");
            }
            Long agrovetId = item.getPreferredAgrovet().getId();
            itemsByAgrovet
                    .computeIfAbsent(agrovetId, key -> new ArrayList<>())
                    .add(item);
        }
        // Create one order for each selected Agrovet.
        for (List<FarmerInputRequestItem> items : itemsByAgrovet.values()) {
            FarmerInputRequestItem firstItem = items.get(0);
            AgrovetOrder order = new AgrovetOrder();
            order.setFarmerInputRequest(request);
            order.setAgrovet(firstItem.getPreferredAgrovet());
            order.setStatus(AgrovetOrderStatus.PENDING);
            order.setForwardedAt(LocalDateTime.now());
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (FarmerInputRequestItem requestItem : items) {
                AgrovetProducts product = requestItem.getSelectedProduct();
                BigDecimal quantity = requestItem.getRequestedQuantity();

                // Recheck current stock before forwarding.
                if (!product.isActive()
                        || product.getQuantityAvailable().compareTo(quantity) < 0) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Product is inactive or has insufficient stock: " + product.getProductName());
                }
                BigDecimal unitPrice = product.getPrice();
                if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid price for product: " + product.getProductName());
                }
                BigDecimal subtotal = unitPrice
                        .multiply(quantity)
                        .setScale(2, RoundingMode.HALF_UP);
                AgrovetOrderItem orderItem = new AgrovetOrderItem();
                orderItem.setAgrovetOrder(order);
                orderItem.setRequestItem(requestItem);
                orderItem.setAgrovetProduct(product);
                orderItem.setQuantity(quantity);
                orderItem.setUnitPrice(unitPrice);
                orderItem.setSubtotal(subtotal);
                order.getItems().add(orderItem);
                totalAmount = totalAmount.add(subtotal);
            }
            order.setTotalAmount(totalAmount);
            orderRepository.save(order);
        }
        request.setStatus(FarmerInputRequestStatus.FORWARDED);
        requestRepository.save(request);
        return request.getId();
    }
    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<FarmerInputRequestResponseDTO> getAllRequests() {

        return requestRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public FarmerInputRequestResponseDTO getRequestById(Long requestId) {

        FarmerInputRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Input request not found"
                ));

        return mapToResponseDTO(request);
    }

    private FarmerInputRequestResponseDTO mapToResponseDTO(
            FarmerInputRequest request) {
        List<FarmerInputRequestItemResponseDTO> items =
                request.getItems().stream()
                        .map(item -> new FarmerInputRequestItemResponseDTO(
                                item.getSelectedProduct().getId(),
                                item.getProductName(),
                                item.getCategory().toString(),
                                item.getRequestedQuantity(),
                                item.getUnit(),
                                item.getPreferredAgrovet().getId(),
                                item.getPreferredAgrovet().getBusinessName()
                        ))
                        .toList();
        return new FarmerInputRequestResponseDTO(
                request.getId(),
                request.getFarmer().getName(),
                request.getFarmer().getUser().getEmailAddress(),
                request.getStatus().name(),
                request.getCreatedAt(),
                items
        );
    }
}

