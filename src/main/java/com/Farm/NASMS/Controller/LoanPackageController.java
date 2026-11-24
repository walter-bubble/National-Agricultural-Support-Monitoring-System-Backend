package com.Farm.NASMS.Controller;

import com.Farm.NASMS.dto.LoanPackageResponse;
import com.Farm.NASMS.model.LoanPackage;
import com.Farm.NASMS.Service.LoanPackageService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loan-package")
public class LoanPackageController {

private final LoanPackageService loanPackageService;

public LoanPackageController(LoanPackageService loanPackageService){
    this.loanPackageService=loanPackageService;
  }

  @PostMapping("/loan")
  public LoanPackageResponse createLoanPackage(@RequestBody LoanPackage loanPackage){
    return loanPackageService.createLoanPackage(loanPackage);
  }
  @GetMapping
    public List<LoanPackageResponse> getAllLoanPackage(){
    return loanPackageService.getAllLoanPackage();
  }
  @GetMapping("/{id}")
    public LoanPackageResponse getLoanPackageById(@PathVariable Long id){
    return loanPackageService.getLoanPackageById(id);
  }
  @PutMapping("/{id}")
  public LoanPackageResponse updateLoanPackage(@PathVariable Long id,@RequestBody LoanPackage loanPackage){
    return loanPackageService.updateLoanPackage(id,loanPackage);
  }
    @DeleteMapping("/{id}")
    public void deleteLoanPackage(@PathVariable Long id){
    loanPackageService.deleteLoanPackage(id);
  }
}
