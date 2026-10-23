package com.Farm.NASMS.Repository;

import com.Farm.NASMS.enums.ProductStatus;
import com.Farm.NASMS.model.MarketPlaceProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarketPlaceProductRepository extends JpaRepository<MarketPlaceProduct,Long> {
    List<MarketPlaceProduct>findByStatusOrderByCreatedAtDesc(ProductStatus status);
    List<MarketPlaceProduct>findBySeller_IdOrderByCreatedAtDesc(Long sellerId);
    Optional<MarketPlaceProduct> findByIdAndStatus(Long id,ProductStatus status);
}
