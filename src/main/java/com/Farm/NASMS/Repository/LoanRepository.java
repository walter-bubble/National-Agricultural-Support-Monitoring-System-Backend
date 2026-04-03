package com.Farm.NASMS.Repository;

import com.Farm.NASMS.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoanRepository  extends JpaRepository<Loan, Long> {
    List<Loan> findByFarmerNationalIdAndStatus(Long nationalId,String status);
    List<Loan>findByFarmerNationalId(Long nationalId);
    @Query("SELECT COUNT(1) FROM Loan L WHERE L.season.id=:seasonId")
    Long countLoansBySeason(Long seasonId);

    @Query("SELECT COUNT(1) FROM Loan L WHERE L.season.id=:seasonId AND L.status='Approved'")
    Long countApprovedLoans(Long seasonId);

    @Query("SELECT COUNT(1) FROM Loan L WHERE L.season.id=:seasonId")
    double getTotalLoanAmountBySeason(Long seasonId);
}
