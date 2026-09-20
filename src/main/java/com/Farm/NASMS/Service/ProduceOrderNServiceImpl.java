package com.Farm.NASMS.Service;

import com.Farm.NASMS.Repository.FarmerRepository;
import com.Farm.NASMS.Repository.ProduceOrderNRepo;
import com.Farm.NASMS.dto.ProduceDeliveryRequestN;
import com.Farm.NASMS.model.ProduceDeliveryN;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduceOrderNServiceImpl implements ProduceOrderNService {
    private final FarmerRepository farmerRepository;
    private final ProduceOrderNRepo produceOrderNRepo;

    public ProduceOrderNServiceImpl(FarmerRepository farmerRepository, ProduceOrderNRepo produceOrderNRepo) {
        this.farmerRepository = farmerRepository;
        this.produceOrderNRepo = produceOrderNRepo;
    }

    @Override
    public ProduceDeliveryN placeOrder(Long nationalId, ProduceDeliveryRequestN requestN) {
        return null;
    }

    @Override
    public List<ProduceDeliveryN> getMyOrders(Long nationalId) {
        return List.of();
    }
}
