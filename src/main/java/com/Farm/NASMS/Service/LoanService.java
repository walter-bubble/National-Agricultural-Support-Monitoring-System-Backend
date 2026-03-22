package com.Farm.NASMS.Service;

import com.Farm.NASMS.Loan;

import java.util.List;

public interface LoanService {
    <loan> Loan createLoanFromPackage(Long nationalId, String loanCode);
    List<Loan> getAllLoans();
    Loan getLoansById(Long id);
    List<Loan>getLoansByFarmer(Long nationalId,String status);
    Loan updateLoanStatus(Long nationalId, String status);
    void deleteLoan(Long id);
}
