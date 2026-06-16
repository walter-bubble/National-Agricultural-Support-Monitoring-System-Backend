package com.Farm.NASMS.Controller;

import com.Farm.NASMS.LoanPackage;
import com.Farm.NASMS.Service.LoanPackageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/loan-package")
public class LoanPackageController {
private LoanPackageService loanPackageService;
public LoanPackageController(LoanPackageService loanPackageService){
    this.loanPackageService=loanPackageService;
  }
@PostMapping
    public LoanPackage createLoanPackage(@RequestBody LoanPackage loanPackage){
    return loanPackageService.createLoanPackage(loanPackage);
  }
  @GetMapping
    public List<LoanPackage> getAllLoanPackage(){
    return loanPackageService.getAllLoanPackage();
  }
  @GetMapping("/{id}")
    public LoanPackage getLoanPackageById(@PathVariable Long id){
    return loanPackageService.getLoanPackageById(id);
  }
  @PutMapping("/{id}")
  public LoanPackage updateLoanPackage(@PathVariable Long id,@RequestBody LoanPackage loanPackage){
    return loanPackageService.updateLoanPackage(id,loanPackage);
  }
    @DeleteMapping("/{loanCode}")
    public void deleteLoanPackage(@PathVariable Long id){
    loanPackageService.deleteLoanPackage(id);
  }
}
