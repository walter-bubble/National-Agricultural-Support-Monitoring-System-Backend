package com.Farm.NASMS.Controller;


import com.Farm.NASMS.MarketTransaction;
import com.Farm.NASMS.Service.MarketTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/market")
public class MarketController {
    private MarketTransactionService marketTransactionService;
    public MarketController(MarketTransactionService marketTransactionService){
        this.marketTransactionService=marketTransactionService;
    }
    @PostMapping("/transaction")
    public ResponseEntity<MarketTransactionService>createTransaction(@RequestBody MarketTransactionService marketTransactionService){
        MarketTransaction saved = marketTransactionService.createTransaction(marketTransactionService);
        return ResponseEntity.ok(saved);
    }
}
