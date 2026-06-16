package com.Farm.NASMS.Repository;

import com.Farm.NASMS.LoanPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanPackageRepository extends JpaRepository<LoanPackage, String> {
    Optional<LoanPackage> findById(Long id);
}
