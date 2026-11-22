package com.Farm.NASMS.Repository;

import com.Farm.NASMS.model.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FarmerRepository extends JpaRepository<Farmer,Long> {
    Optional<Farmer> findByNationalId(Long nationalId);
    boolean existsByNationalId(Long nationalId);
    Optional<Farmer> findByUserId(Long userId);
    @Query("SELECT COUNT(f) FROM Farmer f")
    long countFarmers();

}
