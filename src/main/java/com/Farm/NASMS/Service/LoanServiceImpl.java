package com.Farm.NASMS.Service;

import com.Farm.NASMS.Farmer;
import com.Farm.NASMS.Loan;
import com.Farm.NASMS.Repository.FarmerRepository;
import com.Farm.NASMS.Repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LoanServiceImpl implements LoanService {
    @Autowired
    private FarmerRepository farmerRepository;
    private LoanRepository loanRepository;
    public LoanServiceImpl(FarmerRepository farmerRepository, LoanRepository loanRepository){
        this.farmerRepository=farmerRepository;
        this.loanRepository=loanRepository;
    }
    @Override
    public Loan createLoan(Long farmerId, Loan loan) {
        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(()->new RuntimeException("Farmer not found"));
        loan.setFarmer(farmer);
        loan.setStatus("pending");
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
    public List<Loan> getLoansByFarmer(Long farmerId) {
        return loanRepository.findByFarmerId(farmerId);
    }
    @Override
    public Loan updateLoanByFarmer(Long id, String status) {
        Loan loan =getLoansById(id);
        loan.setStatus(status.toUpperCase());
        return loanRepository.save(loan);

    }
    @Override
    public void deleteLoan(Long id) {
        //check if loan exists
        Loan loan = getLoansById(id);
        loanRepository.delete(loan);
    }
}
