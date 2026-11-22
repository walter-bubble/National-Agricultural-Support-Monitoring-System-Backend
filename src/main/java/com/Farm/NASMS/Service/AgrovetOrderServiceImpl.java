package com.Farm.NASMS.Service;

import com.Farm.NASMS.Repository.AgrovetOrderRepository;
import com.Farm.NASMS.Repository.AgrovetProductRepository;
import com.Farm.NASMS.Repository.AgrovetRepository;
import com.Farm.NASMS.Repository.FarmerInputRequestRepository;
import com.Farm.NASMS.dto.AgrovetOrderItemResponseDTO;
import com.Farm.NASMS.dto.AgrovetOrderResponseDTO;
import com.Farm.NASMS.enums.AgrovetOrderStatus;
import com.Farm.NASMS.enums.FarmerInputRequestStatus;
import com.Farm.NASMS.model.*;
import com.walter.toolkit.security_starter.audit.Auditable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.Farm.NASMS.model.AgrovetProducts;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@Transactional(readOnly = true)
@PreAuthorize("hasRole('AGROVET')")
public class AgrovetOrderServiceImpl implements AgrovetOrderService {
    private final AgrovetOrderRepository orderRepository;
    private final AgrovetRepository agrovetRepository;
    private final FarmerInputRequestRepository requestRepository;
    private final AgrovetProductRepository productRepository;

    public AgrovetOrderServiceImpl(AgrovetOrderRepository orderRepository, AgrovetRepository agrovetRepository, FarmerInputRequestRepository requestRepository, AgrovetProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.agrovetRepository = agrovetRepository;
        this.requestRepository = requestRepository;
        this.productRepository = productRepository;
    }

    private Agrovet getCurrentAgrovet() {
        String email = org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return agrovetRepository.findByUser_EmailAddress(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agrovet profile not found for the logged-in user"));
    }

    @Override
    public List<AgrovetOrderResponseDTO> getMyOrders() {
        Agrovet agrovet = getCurrentAgrovet();
        return orderRepository
                .findByAgrovet_IdAndForwardedAtIsNotNull(agrovet.getId())
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    public AgrovetOrderResponseDTO getMyOrderById(Long orderId) {
        Agrovet agrovet = getCurrentAgrovet();
        AgrovetOrder order = orderRepository.findByAgrovet_IdAndId(agrovet.getId(), orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found for this Agrovet"));
        if (order.getForwardedAt() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Forwarded order not found");
        }
        return toResponseDTO(order);
    }

    private AgrovetOrderResponseDTO toResponseDTO(AgrovetOrder order) {
        List<AgrovetOrderItemResponseDTO> items = order.getItems()
                .stream()
                .map(this::toItemResponseDTO)
                .toList();
        return new AgrovetOrderResponseDTO(
                order.getId(),
                order.getFarmerInputRequest().getId(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getForwardedAt(),
                items
        );
    }

    private AgrovetOrderItemResponseDTO toItemResponseDTO(AgrovetOrderItem item) {
        String productName = item.getAgrovetProduct().getProductName();
        String unit = item.getRequestItem().getUnit();
        return new AgrovetOrderItemResponseDTO(
                item.getId(),
                item.getAgrovetProduct().getId(),
                productName,
                item.getQuantity(),
                unit,
                item.getUnitPrice(),
                item.getSubtotal()
        );
    }

    @Override
    @Transactional
    @Auditable(action = "AGROVET_ORDER_APPROVED")
    public void approveOrder(Long orderId) {
        reviewOrder(orderId, AgrovetOrderStatus.APPROVED);
    }

    @Override
    @Transactional
    @Auditable(action = "AGROVET_ORDER_REJECTED")
    public void rejectOrder(Long orderId) {
        reviewOrder(orderId, AgrovetOrderStatus.REJECTED);
    }

    private void reviewOrder(Long orderId, AgrovetOrderStatus newStatus) {
        Agrovet agrovet = getCurrentAgrovet();
        AgrovetOrder order = orderRepository.findByAgrovet_IdAndId(agrovet.getId(), orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found for this Agrovet"));
        if (order.getForwardedAt() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Forwarded order not found");
        }
        if (order.getStatus() != AgrovetOrderStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only pending orders can be reviewed");
        }
        order.setStatus(newStatus);
        orderRepository.save(order);
        updateFarmerRequestStatus(order.getFarmerInputRequest());
    }

    @Override
    @Transactional
    @Auditable(action = "AGROVET_ORDER_FULFILLED")
    public void fulfillOrder(Long orderId) {
        Agrovet agrovet = getCurrentAgrovet();
        // Lock the order so it cannot be fulfilled twice simultaneously.
        AgrovetOrder order = orderRepository.findByAgrovetAndIdForUpdate(agrovet.getId(), orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found for this Agrovet"));
        if (order.getForwardedAt() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Forwarded order not found");
        }

        if (order.getStatus() != AgrovetOrderStatus.APPROVED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only approved orders can be fulfilled");
        }

        // Group quantities by product ID. TreeMap sorts IDs to keep
        // product locks in a consistent order and reduce deadlock risk.
        Map<Long, BigDecimal> requiredQuantities = new TreeMap<>();
        for (AgrovetOrderItem item : order.getItems()) {
            Long productId = item.getAgrovetProduct().getId();
            requiredQuantities.merge(
                    productId,
                    item.getQuantity(),
                    BigDecimal::add
            );
        }
        // Lock all products and validate stock before deducting anything.
        Map<Long, AgrovetProducts> lockedProducts = new TreeMap<>();
        for (Map.Entry<Long, BigDecimal> entry : requiredQuantities.entrySet()) {
            Long productId = entry.getKey();
            BigDecimal requiredQuantity = entry.getValue();
            AgrovetProducts product = productRepository.findByIdForUpdate(productId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: " + productId));

            // Confirm that the product belongs to this Agrovet.
            if (!product.getAgrovet().getId().equals(agrovet.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot fulfill an order containing another Agrovet's product");
            }

            if (!product.isActive()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, product.getProductName() + " is inactive");
            }
            if (product.getQuantityAvailable().compareTo(requiredQuantity) < 0) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient stock for " + product.getProductName());
            }
            lockedProducts.put(productId, product);
        }
        // Deduct stock only after every product has passed validation.
        for (Map.Entry<Long, BigDecimal> entry : requiredQuantities.entrySet()) {
            AgrovetProducts product = lockedProducts.get(entry.getKey());
            BigDecimal remainingStock = product.getQuantityAvailable()
                    .subtract(entry.getValue());
            product.setQuantityAvailable(remainingStock);
            productRepository.save(product);
        }
        // Update the order after stock deduction.
        order.setStatus(AgrovetOrderStatus.FULFILLED);
        orderRepository.save(order);
        // Update the overall farmer request status.
        updateFarmerRequestStatus(order.getFarmerInputRequest());
    }

    private void updateFarmerRequestStatus(FarmerInputRequest request) {
        List<AgrovetOrder> orders = orderRepository.findByFarmerInputRequest_Id(request.getId());
        if (orders.isEmpty()) {
            return;
        }
        long fulfilledCount = orders.stream()
                .filter(o -> o.getStatus() == AgrovetOrderStatus.FULFILLED)
                .count();
        long approvedCount = orders.stream()
                .filter(o -> o.getStatus() == AgrovetOrderStatus.APPROVED)
                .count();
        long rejectedCount = orders.stream()
                .filter(o -> o.getStatus() == AgrovetOrderStatus.REJECTED)
                .count();
        long canceledCount = orders.stream()
                .filter(o -> o.getStatus() == AgrovetOrderStatus.CANCELED)
                .count();
        long pendingCount = orders.stream()
                .filter(o -> o.getStatus() == AgrovetOrderStatus.PENDING)
                .count();
        long totalOrders = orders.size();
        FarmerInputRequestStatus newStatus;
        if (fulfilledCount == totalOrders) {
            newStatus = FarmerInputRequestStatus.COMPLETED;
        } else if (fulfilledCount > 0) {
            newStatus = FarmerInputRequestStatus.PARTIALLY_FULFILLED;
        } else if (canceledCount == totalOrders) {
            newStatus = FarmerInputRequestStatus.CANCELED;

        }
        else if (approvedCount + fulfilledCount == totalOrders) {
            newStatus = FarmerInputRequestStatus.ACCEPTED;
        }
        else if (approvedCount > 0) {
            newStatus = FarmerInputRequestStatus.PARTIALLY_ACCEPTED;
        }
        else if (rejectedCount + canceledCount == totalOrders) {
            newStatus = FarmerInputRequestStatus.DECLINED_BY_AGROVETS;
        }
        else if (rejectedCount + canceledCount > 0 && pendingCount > 0) {
            newStatus = FarmerInputRequestStatus.PARTIALLY_REVIEWED;
        }
        else {
            newStatus = FarmerInputRequestStatus.FORWARDED;
        }
        request.setStatus(newStatus);
        requestRepository.save(request);
    }
    @Override
    @Transactional
    @Auditable(action = "AGROVET_ORDER_CANCELED")
    public void cancelOrder(Long orderId, String reason) {
        Agrovet agrovet = getCurrentAgrovet();
        AgrovetOrder order = orderRepository.findByAgrovet_IdAndId(agrovet.getId(), orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found for this Agrovet"));
        System.out.println("========== CANCEL ORDER DEBUG ==========");
        System.out.println("Order ID: " + order.getId());
        System.out.println("Agrovet ID: " + order.getAgrovet().getId());
        System.out.println("Order status: " + order.getStatus());
        System.out.println("Is PENDING: " +
                (order.getStatus() == AgrovetOrderStatus.PENDING));
        System.out.println("Is APPROVED: " +
                (order.getStatus() == AgrovetOrderStatus.APPROVED));
        System.out.println("========================================");
        if (order.getForwardedAt() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This order has not been forwarded yet");
        }
        if (order.getStatus() != AgrovetOrderStatus.PENDING &&
                order.getStatus() != AgrovetOrderStatus.APPROVED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only pending or approved orders can be canceled");
        }
        order.setStatus(AgrovetOrderStatus.CANCELED);
        order.setCancellationReason(reason != null ? reason.trim() : null);
        orderRepository.save(order);
        updateFarmerRequestStatus(order.getFarmerInputRequest());
    }
}
