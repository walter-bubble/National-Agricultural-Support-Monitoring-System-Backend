package com.Farm.NASMS.Service;

import com.Farm.NASMS.dto.LoanResponse;
import com.Farm.NASMS.model.Loan;

import java.util.List;

public interface LoanService {

    List<Loan> getAllLoans();
    Loan getLoansById(Long id);
    List<Loan>getLoansByFarmer(Long nationalId,String status);
    Loan payLoan(Long id);
    void deleteLoan(Long id);
    Loan updateLoanStatus(Long id, String status);

    LoanResponse createLoanFromPackage(Long nationalId, Long id);
}
