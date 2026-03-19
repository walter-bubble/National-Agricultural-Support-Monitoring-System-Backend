package com.Farm.NASMS.Service;

import com.Farm.NASMS.Loan;

import java.util.List;

public interface LoanService {
    <loan> Loan createLoanFromPackage(Long farmerId, String loanCode);
    List<Loan> getAllLoans();
    Loan getLoansById(Long id);
    List<Loan>getLoansByFarmer(Long farmerId,String status);
    Loan updateLoanStatus(Long id, String status);
    void deleteLoan(Long id);
}
