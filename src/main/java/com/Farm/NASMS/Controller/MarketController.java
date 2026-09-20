package com.Farm.NASMS.Controller;


import com.Farm.NASMS.model.MarketTransaction;
import com.Farm.NASMS.Service.MarketTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market")
public class MarketController {
    private final MarketTransactionService marketTransactionService;
    public MarketController(MarketTransactionService marketTransactionService){
        this.marketTransactionService=marketTransactionService;
    }
    @PostMapping("/transaction")
   public ResponseEntity<MarketTransaction>createTransaction(@RequestBody MarketTransaction transaction){
        MarketTransaction saved = marketTransactionService.createTransaction(transaction);
       return ResponseEntity.ok(saved);
    }
    @GetMapping("/transactions/")
    public ResponseEntity<List<MarketTransaction>> getAllTransactions(){
        List<MarketTransaction> transactions = marketTransactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<MarketTransaction>> getTransactionBySellerId(@PathVariable Long sellerId){
       return ResponseEntity.ok(marketTransactionService.getTransactionBySellerId(sellerId));
    }
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<MarketTransaction>> getTransactionByBuyerId(@PathVariable Long buyerId){
        return ResponseEntity.ok(marketTransactionService.getTransactionByBuyerId(buyerId));
    }
    @PutMapping("/transaction/{id}")
    public ResponseEntity<MarketTransaction> updateTransaction(@PathVariable Long id, @RequestBody MarketTransaction transaction){
       return ResponseEntity.ok(marketTransactionService.updateTransaction(id,transaction));
    }
    @DeleteMapping("/transaction/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id){
                marketTransactionService.deleteTransaction(id);
                return ResponseEntity.noContent().build();
    }
}
