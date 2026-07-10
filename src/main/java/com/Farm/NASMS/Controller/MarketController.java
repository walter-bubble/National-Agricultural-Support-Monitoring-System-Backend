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
        List<MarketTransaction> list = marketTransactionService. getTransactionBySellerId(sellerId);
        return ResponseEntity.ok(list);
    }
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<MarketTransaction>> getTransactionByBuyerId(@PathVariable Long buyerId){
        List<MarketTransaction> list = marketTransactionService.getTransactionByBuyerId(buyerId);
        return ResponseEntity.ok(list);
    }
    @PutMapping("/transaction/{id}")
    public ResponseEntity<MarketTransaction> updateTransaction(@PathVariable Long id, @RequestBody MarketTransaction transaction){
        return marketTransactionService.getTransactionById(id)
                .map(existing ->{
                    existing.setSellerId(transaction.getSellerId());
                    existing.setSellerType(transaction.getSellerType());
                    existing.setBuyerId(transaction.getBuyerId());
                    existing.setPrice(transaction.getPrice());
                    existing.setQuantityRequested(transaction.getQuantityRequested());
                    existing.setProductCode(transaction.getProductCode());
                    existing.setProductName(transaction.getProductName());
                    MarketTransaction updated = marketTransactionService.updateTransaction(existing);
                            return ResponseEntity.ok(updated);
                }).orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/transaction/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id){
        marketTransactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
