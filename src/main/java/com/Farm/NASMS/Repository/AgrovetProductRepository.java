package com.Farm.NASMS.Repository;

import com.Farm.NASMS.enums.AgrovetProductCategory;
import com.Farm.NASMS.model.AgrovetProducts;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AgrovetProductRepository extends JpaRepository<AgrovetProducts,Long> {
    List<AgrovetProducts> findByAgrovetId(Long agrovetId);
    List<AgrovetProducts> findByAgrovetIdAndActiveTrue(Long agrovetId);
    Optional<AgrovetProducts> findByIdAndAgrovetId(Long id, Long agrovetId);
    List<AgrovetProducts> findByCategoryAndActiveTrue(AgrovetProductCategory category);
    List<AgrovetProducts> findByProductNameContainingIgnoreCaseAndActiveTrue(String productName);
    List<AgrovetProducts> findByActiveTrue();
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM AgrovetProducts p WHERE p.id = :productId")
    Optional<AgrovetProducts> findByIdForUpdate(@Param("productId") Long productId);
}
