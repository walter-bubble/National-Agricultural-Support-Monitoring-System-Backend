package com.Farm.NASMS.Service;

import com.Farm.NASMS.model.Loan;
import com.Farm.NASMS.model.LoanPayment;
import com.Farm.NASMS.enums.LoanStatus;
import com.Farm.NASMS.enums.PaymentMethod;
import com.Farm.NASMS.Repository.LoanPaymentRepository;
import com.Farm.NASMS.Repository.LoanRepository;
import com.Farm.NASMS.dto.LoanPaymentRequest;
import com.Farm.NASMS.dto.LoanPaymentResponse;
import com.Farm.NASMS.security.AuthenticatedUserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;


@Service
public class LoanPaymentServiceImpl implements LoanPaymentService {
    private final LoanPaymentRepository loanPaymentRepository;
    private final AuthenticatedUserService authenticatedUserService;
    private final LoanRepository loanRepository;
    public LoanPaymentServiceImpl(LoanPaymentRepository loanPaymentRepository,LoanRepository loanRepository,AuthenticatedUserService authenticatedUserService){
        this.loanPaymentRepository=loanPaymentRepository;
        this.authenticatedUserService = authenticatedUserService;
        this.loanRepository=loanRepository;
    }
    @Override
    @Transactional
    public LoanPaymentResponse makePayment(Long id, LoanPaymentRequest request) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Loan does not exist"));
        if (!authenticatedUserService.isLoanOwner(loan)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
        }

        BigDecimal amount = request.getAmountToPay();
        PaymentMethod paymentMethod = request.getPaymentMethod();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"amount to pay can't be zero!");
        }
        if (loan.getRemainingBalance().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Loan already repaid");
        }
        if (amount.compareTo(loan.getRemainingBalance())>0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The amount to pay exceeds the remaining balance");
        }
        BigDecimal previousTotal = loanPaymentRepository.findByLoan(loan)
                .stream()
                .map(LoanPayment::getAmountToPay)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalAmountPaid = previousTotal.add(amount);
        LoanPayment payment = new LoanPayment();
        payment.setLoan(loan);
        payment.setAmountToPay(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setPreviousTotal(previousTotal);
        payment.setTotalAmountPaid(totalAmountPaid);


        //payment.setTransactionCode();

        BigDecimal newBalance = loan.getRemainingBalance().subtract(amount);
        loan.setRemainingBalance(newBalance);
        payment.setRemainingBalance(newBalance);

        if (newBalance.compareTo(BigDecimal.ZERO) == 0) {
            loan.setStatus(LoanStatus.COMPLETED);
        }
        loanRepository.save(loan);
        LoanPayment saved = loanPaymentRepository.save(payment);

        LoanPaymentResponse response = new LoanPaymentResponse();
        response.setAmountToPay(saved.getAmountToPay());
        response.setTotalAmountPaid(saved.getTotalAmountPaid());
        response.setRemainingBalance(saved.getRemainingBalance());
        response.setPaymentDate(saved.getPaymentDate());
        response.setTransactionCode(saved.getTransactionCode());
        response.setPaymentMethod(saved.getPaymentMethod());
        response.setLoanCode(String.valueOf(saved.getLoan().getId()));

        return response;
    }
}
