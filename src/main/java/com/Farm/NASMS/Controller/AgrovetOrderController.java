package com.Farm.NASMS.Controller;

import com.Farm.NASMS.Service.AgrovetOrderService;
import com.Farm.NASMS.dto.AgrovetOrderCancelDTO;
import com.Farm.NASMS.dto.AgrovetOrderResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agrovet/orders")
public class AgrovetOrderController {
    private final AgrovetOrderService orderService;
    public AgrovetOrderController(AgrovetOrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping
    public ResponseEntity<List<AgrovetOrderResponseDTO>> getMyOrders() {
        return ResponseEntity.ok(orderService.getMyOrders());
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<AgrovetOrderResponseDTO> getMyOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getMyOrderById(orderId));
    }
    @PutMapping("/{orderId}/approve")
    public ResponseEntity<Map<String, Object>> approveOrder(@PathVariable Long orderId) {
        orderService.approveOrder(orderId);
        return ResponseEntity.ok(Map.of(
                "message", "Order approved successfully",
                "orderId", orderId,
                "status", "APPROVED"
        ));
    }
    @PutMapping("/{orderId}/reject")
    public ResponseEntity<Map<String, Object>> rejectOrder(@PathVariable Long orderId) {
        orderService.rejectOrder(orderId);
        return ResponseEntity.ok(Map.of(
                "message", "Order rejected successfully",
                "orderId", orderId,
                "status", "REJECTED"
        ));
    }
    @PutMapping("/{orderId}/fulfill")
    public ResponseEntity<Map<String, Object>> fulfillOrder(@PathVariable Long orderId) {
        orderService.fulfillOrder(orderId);
        return ResponseEntity.ok(Map.of(
                "message", "Order marked as fulfilled successfully",
                "orderId", orderId,
                "status", "FULFILLED"
        ));
    }
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Map<String, Object>> cancelOrder(@PathVariable Long orderId, @Valid @RequestBody AgrovetOrderCancelDTO request) {
        orderService.cancelOrder(orderId, request.reason());
        return ResponseEntity.ok(Map.of(
                "message", "Order canceled successfully",
                "orderId", orderId,
                "status", "CANCELED"
        ));
    }
}
