package com.Farm.NASMS.Controller;

import com.Farm.NASMS.model.Loan;
import com.Farm.NASMS.Service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/adminApproval")
@PreAuthorize("hasRole('Admin')")
public class AdminLoanController {
    private LoanService loanService;
    public AdminLoanController(LoanService loanService){
        this.loanService=loanService;
    }
    @PutMapping("/{id}/review")
    public ResponseEntity<Loan> reviewLoan(@PathVariable Long id,@RequestParam boolean approved){
        String status = approved  ? "APPROVED" : "REJECTED";
        Loan updateLoan = loanService.updateLoanStatus(id,status);
        return ResponseEntity.ok(updateLoan);
    }
}
