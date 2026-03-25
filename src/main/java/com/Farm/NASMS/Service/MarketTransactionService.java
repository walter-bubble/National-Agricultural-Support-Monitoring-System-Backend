package com.Farm.NASMS.Service;

import com.Farm.NASMS.MarketTransaction;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MarketTransactionService {
MarketTransaction createTransaction(MarketTransaction transaction);
List <MarketTransaction>  getAllTransactions();
List<MarketTransaction> getTransactionBySellerId(Long sellerId);
List<MarketTransaction> getTransactionByBuyerId(Long buyerId);
MarketTransaction getTransactionByProductId(String productId);
MarketTransaction updateTransaction(existing);
void deleteTransaction(String productId);


}
