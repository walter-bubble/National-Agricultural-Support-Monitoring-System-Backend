package com.Farm.NASMS.Service;

import com.Farm.NASMS.Loan;

import java.util.List;

public interface LoanService {
    <loan> Loan createLoan(Long farmerId, Loan loan);
    List<Loan> getAllLoans();
    Loan getLoansById(Long id);
    List<Loan>getLoansByFarmer(Long farmerId);
    Loan updateLoanByFarmer(Long id, String status);
    void deleteLoan(Long id);
}
