package com.Farm.NASMS.Repository;

import com.Farm.NASMS.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository  extends JpaRepository<Loan, Long> {
    List<Loan> findByFarmerNationalIdAndStatus(Long farmerId,String status);
}
