package com.Farm.NASMS.Repository;

import com.Farm.NASMS.model.Loan;
import com.Farm.NASMS.model.LoanPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanPaymentRepository extends JpaRepository <LoanPayment,Long> {
    List<LoanPayment> findByLoan(Loan loan);
}
