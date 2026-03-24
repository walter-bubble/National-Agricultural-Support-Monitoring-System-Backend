package com.Farm.NASMS.Repository;

import com.Farm.NASMS.MarketTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketTransactionRepository extends JpaRepository<MarketTransaction, Long> {
    List<MarketTransaction>findBySellerId(Long sellerId);
    List<MarketTransaction>findByBuyerId(Long buyerId);
}
