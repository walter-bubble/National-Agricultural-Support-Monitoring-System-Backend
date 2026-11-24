package com.Farm.NASMS.Service;

import com.Farm.NASMS.dto.ProduceDeliveryRequestN;
import com.Farm.NASMS.dto.ProduceDeliveryResponseN;

import java.time.LocalDate;
import java.util.List;

public interface ProduceOrderNService {
    ProduceDeliveryResponseN placeOrder(Long nationalId, ProduceDeliveryRequestN requestN);
    List<ProduceDeliveryResponseN> getMyOrders(Long nationalId);
    List<ProduceDeliveryResponseN> getAllOrders();
    ProduceDeliveryResponseN scheduleOrder(Long orderId, LocalDate scheduledDate);
}
