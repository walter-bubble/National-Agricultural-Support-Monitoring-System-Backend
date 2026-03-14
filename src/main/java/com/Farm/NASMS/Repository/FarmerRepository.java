package com.Farm.NASMS.Repository;

import com.Farm.NASMS.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FarmerRepository extends JpaRepository<Farmer,Long> {
    Optional<Farmer> findByNationalId(Long nationalId);

}
