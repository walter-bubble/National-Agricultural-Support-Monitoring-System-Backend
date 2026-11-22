package com.Farm.NASMS.Controller;

import com.Farm.NASMS.dto.LoanResponse;
import com.Farm.NASMS.model.Loan;
import com.Farm.NASMS.security.AuthenticatedUserService;
import com.Farm.NASMS.Service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }
    //create loan
    @PostMapping("/{nationalId}/package/{id}")
        public LoanResponse createLoan(@PathVariable Long nationalId, @PathVariable Long id){
            return loanService.createLoanFromPackage(nationalId,id);
        }
        //get list of loans
        @GetMapping
    public List<LoanResponse> getAllLoans(){
        return loanService.getAllLoans();
        }
        //select loan
        @GetMapping("/{id}")
    public LoanResponse getLoansById(@PathVariable Long id){
            return loanService.getLoansById(id);
        }
        //get the loan
    @GetMapping("/farmer/{nationalId}")
    public List<LoanResponse> getLoansByFarmerNationalId(@PathVariable Long nationalId, @RequestParam(required = false) String status) {
        if (status == null) {
            return loanService.getLoansByFarmer(nationalId, null);
        } else {
            return loanService.getLoansByFarmer(nationalId, status.toUpperCase());
        }
    }
    //delete loan
    @DeleteMapping("/{id}")
    public void deleteLoan(@PathVariable Long id){
        loanService.deleteLoan(id);
    }
}
