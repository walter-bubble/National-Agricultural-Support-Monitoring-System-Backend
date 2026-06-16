package com.Farm.NASMS.Service;

import com.Farm.NASMS.LoanPackage;

import java.util.List;

public interface LoanPackageService {
LoanPackage createLoanPackage(LoanPackage loanPackage);
List<LoanPackage>getAllLoanPackage();
LoanPackage getLoanPackageById(Long id);
void deleteLoanPackage(Long id);
LoanPackage updateLoanPackage(Long id,LoanPackage loanPackage);
}
