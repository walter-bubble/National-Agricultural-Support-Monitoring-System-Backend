package com.Farm.NASMS.Controller;

import com.Farm.NASMS.Service.ProduceOrderNServiceImpl;
import com.Farm.NASMS.dto.ProduceDeliveryRequestN;
import com.Farm.NASMS.dto.ProduceDeliveryResponseN;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produce-order")
public class ProduceOrderNController {
    private final ProduceOrderNServiceImpl produceOrderNServiceImpl;

    public ProduceOrderNController(ProduceOrderNServiceImpl produceOrderNServiceImpl) {
        this.produceOrderNServiceImpl = produceOrderNServiceImpl;
    }
    @PostMapping("/{nationalId}")
    public ResponseEntity<ProduceDeliveryResponseN> placeOrder(@PathVariable Long nationalId, @RequestBody ProduceDeliveryRequestN requestN){
        ProduceDeliveryResponseN order = produceOrderNServiceImpl.placeOrder(nationalId,requestN);
        return ResponseEntity.ok(order);
    }
    @GetMapping("/list/{nationalId}")
    public ResponseEntity<List<ProduceDeliveryResponseN>> getMyOrders(@PathVariable Long nationalId){
        return ResponseEntity.ok(produceOrderNServiceImpl.getMyOrders(nationalId));
    }
}
