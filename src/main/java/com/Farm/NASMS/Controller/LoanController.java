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
    @PostMapping("/farmer/{nationalId}/package/{loanCode}")
        public Loan createLoan(@PathVariable Long nationalId,@PathVariable String loanCode){
            return loanService.createLoanFromPackage(nationalId,loanCode);
        }
        //get list of loans
        @GetMapping
    public List<Loan> getAllLoans(){
        return loanService.getAllLoans();
        }
        //select loan
        @GetMapping("/{loanCode}")
    public Loan getLoansById(@PathVariable Long id){
        return loanService.getLoansById(id);
        }
        //get the loan
    @GetMapping("/farmer/national{NationalId}")
    public List<Loan> getLoansByFarmerNationalId(@PathVariable Long nationalId,
                                                 @RequestParam(required = false)
                                                 String status) {
        if (status == null) {
            return loanService.getLoansByFarmer(nationalId, null);
        } else {
            return loanService.getLoansByFarmer(nationalId, status.toUpperCase());
        }
    }
    //update the loan
    @PutMapping("/id/{id}")
    public Loan updateLoanByFarmer(@PathVariable Long nationalId, @RequestBody String status){
        return loanService.updateLoanStatus(nationalId,status);
    }
    //delete loan
    @DeleteMapping("/{id}")
    public void deleteLoan(@PathVariable Long id){
        loanService.deleteLoan(id);
    }
}
