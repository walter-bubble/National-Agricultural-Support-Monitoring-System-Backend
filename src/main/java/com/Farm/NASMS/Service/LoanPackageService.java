package com.Farm.NASMS.Service;

import com.Farm.NASMS.dto.LoanPackageResponse;
import com.Farm.NASMS.model.LoanPackage;

import java.util.List;

public interface LoanPackageService {
LoanPackageResponse createLoanPackage(LoanPackage loanPackage);
List<LoanPackageResponse>getAllLoanPackage();
LoanPackageResponse getLoanPackageById(Long id);
LoanPackageResponse updateLoanPackage(Long id,LoanPackage loanPackage);
void deleteLoanPackage(Long id);
}
