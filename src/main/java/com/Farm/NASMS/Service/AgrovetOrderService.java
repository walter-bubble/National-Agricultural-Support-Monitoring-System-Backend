package com.Farm.NASMS.Service;

import com.Farm.NASMS.dto.AgrovetOrderResponseDTO;

import java.util.List;

public interface AgrovetOrderService {
    List<AgrovetOrderResponseDTO> getMyOrders();
    AgrovetOrderResponseDTO getMyOrderById(Long orderId);
    void approveOrder(Long orderId);
    void rejectOrder(Long orderId);
    void fulfillOrder(Long orderId);
    void cancelOrder(Long orderId, String reason);
}

