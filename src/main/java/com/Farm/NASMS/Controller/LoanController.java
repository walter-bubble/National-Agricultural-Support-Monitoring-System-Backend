package com.Farm.NASMS.Controller;

import com.Farm.NASMS.Loan;
import com.Farm.NASMS.Service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    @Autowired
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }
    //create loan
    @PostMapping("/farmer/{farmerId}/package/{loanCode}")
        public Loan createLoan(@PathVariable Long farmerId,@PathVariable String loanCode){
            return loanService.createLoanFromPackage(farmerId,loanCode);
        }
        //get list of loans
        @GetMapping
    public List<Loan> getAllLoans(){
        return loanService.getAllLoans();
        }
        //select loan
        @GetMapping("/{id}")
    public Loan getLoansById(@PathVariable Long id){
        return loanService.getLoansById(id);
        }
        //get the loan
    @GetMapping("/farmer/{farmerNationalId}")
    public List<Loan> getLoansByFarmerNationalId(@PathVariable Long farmerNationalId,
                                                 @RequestParam(required = false)
                                                 String status) {
        if (status == null) {
            return loanService.getLoansByFarmer(farmerNationalId, null);
        } else {
            return loanService.getLoansByFarmer(farmerNationalId, status.toUpperCase());
        }
    }
    //update the loan
    @PutMapping("/id/{id}")
    public Loan updateLoanByFarmer(@PathVariable Long id, @RequestBody String status){
        return loanService.updateLoanStatus(id,status);
    }
    //delete loan
    @DeleteMapping("/{id}")
    public void deleteLoan(@PathVariable Long id){
        loanService.deleteLoan(id);
    }
}
