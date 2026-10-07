package com.Farm.NASMS.Service;

import com.Farm.NASMS.dto.ProduceDeliveryRequestN;
import com.Farm.NASMS.dto.ProduceDeliveryResponseN;
import com.Farm.NASMS.model.ProduceDeliveryN;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface ProduceOrderNService {

    ProduceDeliveryResponseN placeOrder(Long nationalId, ProduceDeliveryRequestN requestN);

    List<ProduceDeliveryN> getMyOrders(Long nationalId);
}
