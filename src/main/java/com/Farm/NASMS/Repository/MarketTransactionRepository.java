package com.Farm.NASMS.Repository;

import com.Farm.NASMS.model.MarketTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarketTransactionRepository extends JpaRepository<MarketTransaction, Long> {
    List<MarketTransaction>findBySellerId(Long sellerId);
    List<MarketTransaction>findByBuyerId(Long buyerId);
    Optional<MarketTransaction> findById(Long id);
    void deleteById(Long id);
    @Query("""
    SELECT COALESCE(SUM(m.price * m.quantityRequested),0)
    FROM MarketTransaction m
    WHERE m.season.id = :seasonId
""")
    double getTotalSalesBySeason(@Param("seasonId") Long seasonId);

}
