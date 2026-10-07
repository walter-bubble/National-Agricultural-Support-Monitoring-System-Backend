package com.Farm.NASMS.Repository;

import com.Farm.NASMS.enums.ProductOrderStatusN;
import com.Farm.NASMS.model.Farmer;
import com.Farm.NASMS.model.ProduceDeliveryN;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProduceOrderNRepo extends JpaRepository<ProduceDeliveryN,Long> {
    List<ProduceDeliveryN> findByStatusOrderByOrderDateAsc(ProductOrderStatusN status);

    List<ProduceDeliveryN> findByFarmerOrderByOrderDateDesc(Farmer farmer);
}
