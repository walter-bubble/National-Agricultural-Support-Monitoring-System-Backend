package com.Farm.NASMS.Service;

import com.Farm.NASMS.Repository.FarmingSeasonRepository;
import com.Farm.NASMS.Repository.LoanPackageRepository;
import com.Farm.NASMS.model.FarmingSeason;
import com.Farm.NASMS.model.LoanPackage;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LoanPackageServiceImpl implements LoanPackageService {
    private final LoanPackageRepository loanPackageRepository;
    private final FarmingSeasonRepository farmingSeasonRepository;
    public LoanPackageServiceImpl(LoanPackageRepository loanPackageRepository,FarmingSeasonRepository farmingSeasonRepository){
        this.loanPackageRepository=loanPackageRepository;
        this.farmingSeasonRepository=farmingSeasonRepository;
    }
    @Override
    public LoanPackage createLoanPackage(LoanPackage loanPackage) {
        FarmingSeason activeSeason = farmingSeasonRepository
                .findActiveSeason()
                .orElseThrow(()-> new RuntimeException("no active season found") );
        loanPackage.setFarmingSeason(activeSeason);
        return loanPackageRepository.save(loanPackage);
    }
    @Override
    public List<LoanPackage> getAllLoanPackage() {
        return loanPackageRepository.findAll();
    }
    @Override
    public LoanPackage getLoanPackageById(Long id) {
        return loanPackageRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Loan Package not found!"));
    }
    @Override
    public LoanPackage updateLoanPackage(Long id,LoanPackage loanPackage){
        LoanPackage existing = getLoanPackageById(id);
        existing.setAmount(loanPackage.getAmount());
        existing.setInterestRate(loanPackage.getInterestRate());
        existing.setDurationMonths(loanPackage.getDurationMonths());
        existing.setMonthlyPenalty(loanPackage.getMonthlyPenalty());
        existing.setDescription(loanPackage.getDescription());
        return loanPackageRepository.save(existing);
    }
    @Override
    public void deleteLoanPackage(Long id) {
        LoanPackage existing=getLoanPackageById(id);
        loanPackageRepository.delete(existing);
    }
}
