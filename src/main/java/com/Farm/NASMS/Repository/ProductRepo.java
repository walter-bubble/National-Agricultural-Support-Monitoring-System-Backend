package com.Farm.NASMS.Repository;

import com.Farm.NASMS.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product,Long> {
    Optional<Product> findByProductCode(String productCode);
}
