package com.Farm.NASMS.Repository;

import com.Farm.NASMS.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product,Long> {
}
