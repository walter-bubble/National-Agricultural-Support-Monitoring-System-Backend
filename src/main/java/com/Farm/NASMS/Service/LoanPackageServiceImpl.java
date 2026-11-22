package com.Farm.NASMS.Service;

import com.Farm.NASMS.Repository.FarmingSeasonRepository;
import com.Farm.NASMS.Repository.LoanPackageRepository;
import com.Farm.NASMS.model.FarmingSeason;
import com.Farm.NASMS.model.LoanPackage;
import com.walter.toolkit.security_starter.audit.Auditable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
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
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "LOAN_PACKAGE_CREATED")
    public LoanPackage createLoanPackage(LoanPackage loanPackage) {
        FarmingSeason activeSeason = farmingSeasonRepository
                .findActiveSeason()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No active season found"));
        BigDecimal seasonBudget = activeSeason.getBudget();
        BigDecimal allocatedAmount = loanPackageRepository.sumPackageAmountsByFarmingSeason(activeSeason);
        if (allocatedAmount == null) {
            allocatedAmount = BigDecimal.ZERO;
        }
        BigDecimal remainingBudget = seasonBudget.subtract(allocatedAmount);
        if(loanPackage.getAmount().compareTo(remainingBudget)>0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loan package exceeds the remaining farming season budget. Remaining budget: " + remainingBudget);
        }
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan Package not found!"));
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "LOAN_PACKAGE_UPDATED")
    public LoanPackage updateLoanPackage(Long id,LoanPackage loanPackage){
        LoanPackage existing = getLoanPackageById(id);
        existing.setAmount(loanPackage.getAmount());
        existing.setInterestRate(loanPackage.getInterestRate());
        existing.setDurationMonths(loanPackage.getDurationMonths());
        existing.setMonthlyPenalty(loanPackage.getMonthlyPenalty());
        existing.setDescription(loanPackage.getDescription());
        existing.setMinimumFarmSize(loanPackage.getMinimumFarmSize());
        existing.setMaximumFarmSize(loanPackage.getMaximumFarmSize());
        return loanPackageRepository.save(existing);
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "LOAN_PACKAGE_DELETED")
    public void deleteLoanPackage(Long id) {
        LoanPackage existing=getLoanPackageById(id);
        loanPackageRepository.delete(existing);
    }
}
