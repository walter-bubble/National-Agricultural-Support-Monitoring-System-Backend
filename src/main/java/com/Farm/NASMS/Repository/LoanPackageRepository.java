package com.Farm.NASMS.Repository;

import com.Farm.NASMS.model.FarmingSeason;
import com.Farm.NASMS.model.LoanPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LoanPackageRepository extends JpaRepository<LoanPackage,Long> {
}
