package com.Farm.NASMS.Service;

import com.Farm.NASMS.Loan;
import com.Farm.NASMS.LoanPayment;
import com.Farm.NASMS.LoanStatus;
import com.Farm.NASMS.PaymentMethod;
import com.Farm.NASMS.Repository.LoanPaymentRepository;
import com.Farm.NASMS.Repository.LoanRepository;
import com.Farm.NASMS.dto.LoanPaymentRequest;
import com.Farm.NASMS.dto.LoanPaymentResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Long.sum;

@Service
public class LoanPaymentServiceImpl implements LoanPaymentService {
    private LoanPaymentRepository loanPaymentRepository;
    private LoanRepository loanRepository;
    public LoanPaymentServiceImpl(LoanPaymentRepository loanPaymentRepository,LoanRepository loanRepository){
        this.loanPaymentRepository=loanPaymentRepository;
        this.loanRepository=loanRepository;
    }
    @Override
    @Transactional
    public LoanPaymentResponse makePayment(Long id, LoanPaymentRequest request) {
        List<Loan> loans = loanRepository.findByLoanPackage_id(id);
        if (loans.isEmpty()) {
            throw new RuntimeException("loan does not exist");
        }
        Loan loan = loans.get(0);
        double amount = request.getAmountToPay();
        PaymentMethod paymentMethod = request.getPaymentMethod();
        if (amount <= 0) {
            throw new RuntimeException("amount to pay can't be zero!");
        }
        if (loan.getRemainingBalance() <= 0) {
            throw new RuntimeException("Loan already repaid");
        }
        if (amount > loan.getRemainingBalance()) {
            throw new RuntimeException("The amount to pay exceeds the remaining balance");
        }
        LoanPayment payment = new LoanPayment();
        payment.setLoan(loan);
        payment.setPaymentMethod(paymentMethod);
        payment.setAmountToPay(amount);
        payment.setPaymentDate(LocalDateTime.now());
        //payment.setTransactionCode();

        double newBalance = loan.getRemainingBalance() - amount;
        loan.setRemainingBalance(newBalance);
        payment.setRemainingBalance(newBalance);

        if (newBalance == 0) {
            loan.setStatus(LoanStatus.COMPLETED);
        }
        loanRepository.save(loan);
        LoanPayment saved = loanPaymentRepository.save(payment);

        double previousTotal = loanPaymentRepository
                .findByLoan(loan)
                .stream()
                .mapToDouble(LoanPayment::getAmountToPay)
                .sum();
        double totalAmountPaid = previousTotal + amount;


        LoanPaymentResponse response = new LoanPaymentResponse();
        response.setAmountToPay(saved.getAmountToPay());
        response.setTotalAmountPaid(saved.getTotalAmountPaid());
        response.setRemainingBalance(saved.getRemainingBalance());
        response.setPaymentDate(saved.getPaymentDate());
        response.setTransactionCode(saved.getTransactionCode());
        response.setPaymentMethod(saved.getPaymentMethod());

        return response;
    }
}
