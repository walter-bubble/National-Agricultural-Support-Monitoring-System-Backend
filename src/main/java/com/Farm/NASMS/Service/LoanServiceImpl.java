package com.Farm.NASMS.Service;

import com.Farm.NASMS.dto.LoanResponse;
import com.Farm.NASMS.enums.LoanStatus;
import com.Farm.NASMS.model.Farmer;
import com.Farm.NASMS.model.FarmingSeason;
import com.Farm.NASMS.model.Loan;
import com.Farm.NASMS.model.LoanPackage;
import com.Farm.NASMS.Repository.FarmerRepository;
import com.Farm.NASMS.Repository.FarmingSeasonRepository;
import com.Farm.NASMS.Repository.LoanPackageRepository;
import com.Farm.NASMS.Repository.LoanRepository;
import com.Farm.NASMS.security.AuthenticatedUserService;
import com.walter.toolkit.security_starter.audit.Auditable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {
    private final AuthenticatedUserService authenticatedUserService;
    private final  FarmerRepository farmerRepository;
    private final LoanRepository loanRepository;
    private final LoanPackageRepository loanPackageRepository;
    private final FarmingSeasonRepository farmingSeasonRepository;
    public LoanServiceImpl(FarmerRepository farmerRepository, LoanRepository loanRepository, LoanPackageRepository loanPackageRepository, FarmingSeasonRepository farmingSeasonRepository,AuthenticatedUserService authenticatedUserService){
        this.farmerRepository=farmerRepository;
        this.authenticatedUserService = authenticatedUserService;
        this.loanRepository=loanRepository;
        this.loanPackageRepository=loanPackageRepository;
        this.farmingSeasonRepository=farmingSeasonRepository;
    }
    @Override
    @PreAuthorize("hasAnyRole('FARMER', 'ADMIN')")
    @Auditable(action = "LOAN_CREATED")
    public LoanResponse createLoanFromPackage(Long nationalId, Long id) {
        if(!authenticatedUserService.isCurrentFarmerNationalId(nationalId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access denied");
        }
        Farmer farmer = farmerRepository.findByNationalId(nationalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer not found"));
        FarmingSeason season = farmingSeasonRepository.findActiveSeason()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No active season found!"));
        LoanPackage loanPackage=loanPackageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan package not found"));

        // closing of season
        if(season.shouldAutoClose()) {
            season.setClosed(true);
            farmingSeasonRepository.save(season);
        }
        //if season is still active
        if(!season.isActive()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loans can only be applied during an active season");
        }
        //check if loan limit has been exceeded for that season
        Double totalLoanAmount = loanRepository.getTotalLoanAmountBySeason(season.getId());
        BigDecimal totalLoans = totalLoanAmount == null ? BigDecimal.ZERO : BigDecimal.valueOf(totalLoanAmount);
        BigDecimal newTotalLoans = totalLoans.add(loanPackage.getAmount());
        if (newTotalLoans.compareTo(season.getBudget()) > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Season budget exceeded!");
        }
        //check if farmer has a lona already
        boolean hasActiveLoan = loanRepository.existsByFarmerAndStatusIn(farmer,List.of(LoanStatus.ACTIVE));
        if(hasActiveLoan){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You already have an active loan!");
        }
        Loan loan = new Loan();
        loan.setFarmer(farmer);
        loan.setFarmingSeason(season);
        loan.setLoanPackage(loanPackage);
        loan.setAmount(loanPackage.getAmount());
        loan.setInterestRate(loanPackage.getInterestRate());
        loan.setMonthlyPenalty(loanPackage.getMonthlyPenalty());
       loan.setDurationMonths(loanPackage.getDurationMonths());

       LocalDateTime now = LocalDateTime.now();
       LocalDateTime dueDate = now.plusMonths(loan.getDurationMonths());
       loan.setDueDate(dueDate);
        BigDecimal time = BigDecimal.valueOf(loanPackage.getDurationMonths())
                .divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);
        BigDecimal interest = loanPackage.getAmount().multiply(loanPackage.getInterestRate()
                                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)).multiply(time);
        BigDecimal totalPayment = loanPackage.getAmount().add(interest);
        loan.setTotalPayment(totalPayment);
        loan.setRemainingBalance(totalPayment);
        loan.setStatus(LoanStatus.ACTIVE);
        Loan savedLoan = loanRepository.save(loan);
        LoanResponse response = getLoanResponse(savedLoan);
        return response;
    }
    private static LoanResponse getLoanResponse(Loan savedLoan) {
        LoanResponse response = new LoanResponse();
        response.setLoanId(savedLoan.getId());
        response.setAmount(savedLoan.getAmount());
        response.setDueDate(savedLoan.getDueDate());
        response.setFarmerName(savedLoan.getFarmer().getName());
        response.setTotalPayment(savedLoan.getTotalPayment());
        response.setSeasonName(savedLoan.getFarmingSeason().getSeasonName());
        response.setStatus(savedLoan.getStatus());
        response.setNationalId(savedLoan.getFarmer().getNationalId());
        response.setInterestRate(savedLoan.getInterestRate().doubleValue());
        return response;
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<LoanResponse> getAllLoans() {
        return loanRepository.findAll()
                .stream()
                .map(LoanServiceImpl::getLoanResponse)
                .toList();
    }

    @Override
    @PreAuthorize("hasAnyRole('FARMER', 'ADMIN')")
    public LoanResponse getLoansById(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan Not Found"));
        if (!authenticatedUserService.isLoanOwner(loan) && !authenticatedUserService.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        return getLoanResponse(loan);
    }

    @Override
    @PreAuthorize("hasAnyRole('FARMER', 'ADMIN')")
    public List<LoanResponse> getLoansByFarmer(Long nationalId, String status) {
        if (!authenticatedUserService.isCurrentFarmerNationalId(nationalId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        List<Loan> loans = (status == null)
                ? loanRepository.findByFarmerNationalId(nationalId)
                : loanRepository.findByFarmerNationalIdAndStatus(nationalId, LoanStatus.valueOf(status.toUpperCase()));
        return loans.stream()
                .map(LoanServiceImpl::getLoanResponse)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "LOAN_DELETED")
    public void deleteLoan(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan Not Found"));
        loanRepository.delete(loan);
    }
}
