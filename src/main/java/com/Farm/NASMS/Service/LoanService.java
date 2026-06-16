package com.Farm.NASMS.Service;

import com.Farm.NASMS.Loan;

import java.util.List;

public interface LoanService {
    <loan> Loan createLoanFromPackage(Long nationalId, Long id,Long seasonId);
    List<Loan> getAllLoans();
    Loan getLoansById(Long id);
    List<Loan>getLoansByFarmer(Long nationalId,String status);
    Loan payLoan(Long id);
    void deleteLoan(Long id);
    Loan updateLoanStatus(Long id, String status);
}
