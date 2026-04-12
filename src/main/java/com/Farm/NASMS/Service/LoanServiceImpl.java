package com.Farm.NASMS.Service;

import com.Farm.NASMS.Farmer;
import com.Farm.NASMS.Loan;
import com.Farm.NASMS.LoanPackage;
import com.Farm.NASMS.LoanStatus;
import com.Farm.NASMS.Repository.FarmerRepository;
import com.Farm.NASMS.Repository.LoanPackageRepository;
import com.Farm.NASMS.Repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class LoanServiceImpl implements LoanService {
    private FarmerRepository farmerRepository;
    private LoanRepository loanRepository;
    private LoanPackageRepository loanPackageRepository;
    public LoanServiceImpl(FarmerRepository farmerRepository, LoanRepository loanRepository,LoanPackageRepository loanPackageRepository){
        this.farmerRepository=farmerRepository;
        this.loanRepository=loanRepository;
        this.loanPackageRepository=loanPackageRepository;
    }
    @Override
    public Loan createLoanFromPackage(Long nationalId, String loanCode) {
        Farmer farmer = farmerRepository.findByNationalId(nationalId)
                .orElseThrow(()->new RuntimeException("Farmer not found"));
        //now we get the loan
        LoanPackage loanPackage=loanPackageRepository.findById(loanCode)
                .orElseThrow(()->new RuntimeException("loan package not found"));
        Loan loan = new Loan();
        loan.setFarmer(farmer);
        loan.setLoanPackage(loanPackage);
        loan.setAmount(loanPackage.getAmount());
        loan.setInterestRate(loanPackage.getInterestRate());
        loan.setMonthlyPenalty(loanPackage.getMonthlyPenalty());
       loan.setDurationMonths(loanPackage.getDurationMonths());


       LocalDateTime now = LocalDateTime.now();
       loan.setIssuedDate(now);
       LocalDateTime dueDate = now
               .plusMonths(loan.getDurationMonths());
       loan.setDueDate(dueDate);
       //calculate payment
        double time = loanPackage.getDurationMonths()/12.0;
        double interest = loanPackage.getAmount() * (loanPackage.getInterestRate()/100) * time;
        double totalPayment = loanPackage.getAmount() + interest;
        loan.setTotalPayment(totalPayment);
        loan.setStatus(LoanStatus.PENDING);
        return loanRepository.save(loan);
    }
    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    @Override
    public Loan getLoansById(Long id) {
        return loanRepository.findById(id).orElseThrow(()->new RuntimeException("Loan not Found!"));
    }
    @Override
    public List<Loan> getLoansByFarmer(Long nationalId,String status) {
        return loanRepository.findByFarmerNationalIdAndStatus(nationalId,status);
    }
    @Override
    public Loan updateLoanStatus(String loanCode,String status) {
        Loan loan =loanRepository.findByLoanPackage_LoanCode(loanCode)
                .orElseThrow(()-> new RuntimeException("Loan not found!"));
        loan.setStatus(LoanStatus.valueOf(status.trim().toUpperCase()));
        return loanRepository.save(loan);

    }
    @Override
    public Loan payLoan(Long id){
        Loan loan = getLoansById(id);
        LocalDateTime dueDate =LocalDateTime.now();
        loan.setDueDate(dueDate);
        double totalDue = loan.calculateTotalDue(dueDate);
        loan.setTotalPayment(totalDue);
        loan.setStatus(LoanStatus.PENDING);
        return loanRepository.save(loan);
    }
    @Override
    public void deleteLoan(Long id) {
        //check if loan exists
        Loan loan = getLoansById(id);
        loanRepository.delete(loan);
    }
}
