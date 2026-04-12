package com.Farm.NASMS.Service;

import com.Farm.NASMS.Loan;
import com.Farm.NASMS.LoanPayment;
import com.Farm.NASMS.Repository.LoanPaymentRepository;
import com.Farm.NASMS.Repository.LoanRepository;

/*public class LoanPaymentService {
    private LoanPaymentRepository loanPaymentRepository;
    private LoanRepository loanRepository;
    public LoanPaymentService(LoanPaymentRepository loanPaymentRepository,LoanRepository loanRepository){
        this.loanPaymentRepository=loanPaymentRepository;
        this.loanRepository=loanRepository;
    }
    public LoanPayment makePayment(String loanCode, double amount, String paymentMethod) {
        Loan loan = loanRepository.findByFarmerNationalId(loanCode)
                .orElseThrow(()-> newRuntimeException("Loan Not Found!"));
        p

    }
}*/
