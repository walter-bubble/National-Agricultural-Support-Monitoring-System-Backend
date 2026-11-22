package com.Farm.NASMS.Service;

import com.Farm.NASMS.dto.LoanResponse;
import com.Farm.NASMS.model.Loan;

import java.util.List;

public interface LoanService {
    List<LoanResponse> getAllLoans();
    LoanResponse getLoansById(Long id);
    List<LoanResponse>getLoansByFarmer(Long nationalId,String status);
    void deleteLoan(Long id);
    LoanResponse createLoanFromPackage(Long nationalId, Long id);
}
