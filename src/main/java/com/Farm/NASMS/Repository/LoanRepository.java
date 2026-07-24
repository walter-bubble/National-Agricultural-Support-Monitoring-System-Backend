package com.Farm.NASMS.Repository;

import com.Farm.NASMS.model.Farmer;
import com.Farm.NASMS.model.FarmingSeason;
import com.Farm.NASMS.model.Loan;
import com.Farm.NASMS.enums.LoanStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
@NotNull

public interface LoanRepository  extends JpaRepository<Loan, Long> {
    List<Loan> findByFarmerNationalIdAndStatus(Long nationalId,LoanStatus status);
    boolean existsByFarmerAndStatusIn(Farmer farmer, List<LoanStatus>statuses);
    @Query("SELECT COUNT(1) FROM Loan l WHERE l.farmingSeason.id=:seasonId")
    Long countLoansBySeason(@Param("seasonId")Long seasonId);

    @Query("SELECT COUNT(1) FROM Loan l WHERE l.farmingSeason.id=:seasonId AND l.status='Approved'")
    Long countApprovedLoans(@Param("seasonId") Long seasonId);

    @Query("SELECT SUM(l.amount) FROM Loan l WHERE l.farmingSeason.id=:seasonId")
    Double getTotalLoanAmountBySeason(@Param("seasonId") Long seasonId);

    boolean existsByFarmerAndFarmingSeason(Farmer farmer, FarmingSeason season);

    List<Loan> findByLoanPackage_id(Long id);
    List<Loan> findByFarmerNationalId(Long nationalId);
}
