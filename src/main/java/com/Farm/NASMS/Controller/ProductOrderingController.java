package com.Farm.NASMS.Controller;

import com.Farm.NASMS.Service.OrderProcessingService;
import com.Farm.NASMS.dto.BuyerOrderRequest;
import com.Farm.NASMS.model.MarketTransaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class ProductOrderingController {
    private OrderProcessingService orderProcessingService;

    public ProductOrderingController(OrderProcessingService orderProcessingService) {
        this.orderProcessingService = orderProcessingService;
    }

    @PostMapping("/submit")
    public ResponseEntity<?> placeBuyerOrder(@RequestBody BuyerOrderRequest request){
        try {
            MarketTransaction completedTransaction = orderProcessingService.processBuyerOrder(
            request.getProductCode(),
            request.getBuyerId(),
            request.getBuyerName(),
            request.getQuantity()
            );
            return new ResponseEntity<>(completedTransaction, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal error disrupted your ordering checkout flow.");
        }
    }
}
