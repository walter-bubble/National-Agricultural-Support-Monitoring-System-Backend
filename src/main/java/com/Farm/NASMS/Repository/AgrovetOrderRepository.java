package com.Farm.NASMS.Repository;

import com.Farm.NASMS.model.AgrovetOrder;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AgrovetOrderRepository extends JpaRepository<AgrovetOrder, Long> {
    List<AgrovetOrder> findByAgrovet_IdAndForwardedAtIsNotNull(Long agrovetId);
    List<AgrovetOrder> findByFarmerInputRequest_Id(Long requestId);
    boolean existsByFarmerInputRequest_IdAndAgrovet_IdAndForwardedAtIsNotNull(Long requestId, Long agrovetId);
    Optional<AgrovetOrder> findByAgrovet_IdAndId(Long agrovetId, Long orderId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT o FROM AgrovetOrder o
    WHERE o.id = :orderId
    AND o.agrovet.id = :agrovetId
""")
    Optional<AgrovetOrder> findByAgrovetAndIdForUpdate(
            @Param("agrovetId") Long agrovetId,
            @Param("orderId") Long orderId
    );
}
