package com.Farm.NASMS.Service;

import com.Farm.NASMS.model.MarketTransaction;
import java.util.Optional;

import java.util.List;

public interface MarketTransactionService {
MarketTransaction createTransaction(MarketTransaction transaction);
List <MarketTransaction>  getAllTransactions();
List<MarketTransaction> getTransactionBySellerId(Long sellerId);
List<MarketTransaction> getTransactionByBuyerId(Long buyerId);
Optional<MarketTransaction> getTransactionById(Long id);
MarketTransaction updateTransaction(MarketTransaction marketTransaction);
void deleteTransaction(Long id);
}
