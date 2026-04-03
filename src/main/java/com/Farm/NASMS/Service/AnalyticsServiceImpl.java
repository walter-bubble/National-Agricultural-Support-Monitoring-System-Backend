package com.Farm.NASMS.Service;

import com.Farm.NASMS.FarmingSeason;
import com.Farm.NASMS.Repository.FarmerRepository;
import com.Farm.NASMS.Repository.FarmingSeasonRepository;
import com.Farm.NASMS.Repository.LoanRepository;
import com.Farm.NASMS.Repository.MarketTransactionRepository;
import com.Farm.NASMS.SeasonAnalyticsDto;
import com.Farm.NASMS.SeasonComparisonDto;
import com.Farm.NASMS.SeasonGraphDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.expression.common.ExpressionUtils.toDouble;


@Service
public class AnalyticsServiceImpl implements AnalyticsService{
    private FarmerRepository farmerRepository;
    private LoanRepository loanRepository;
    private MarketTransactionRepository marketTransactionRepository;
    private FarmingSeasonRepository farmingSeasonRepository;
    public AnalyticsServiceImpl(FarmingSeasonRepository farmingSeasonRepository,
                                MarketTransactionRepository marketTransactionRepository,
                                LoanRepository loanRepository,
                                FarmerRepository farmerRepository){
        this.farmerRepository=farmerRepository;
        this.loanRepository=loanRepository;
        this.marketTransactionRepository=marketTransactionRepository;
        this.farmingSeasonRepository=farmingSeasonRepository;
    }
    @Override
    public SeasonAnalyticsDto getSeasonAnalytics(Long seasonId) {
        FarmingSeason farmingSeason = farmingSeasonRepository.findById(seasonId).
                orElseThrow(()->new RuntimeException("Season not Found"));
        SeasonAnalyticsDto seasonAnalyticsDto = new SeasonAnalyticsDto();
        seasonAnalyticsDto.setSeasonName(farmingSeason.getSeasonName());
        seasonAnalyticsDto.setTotalFarmers(farmerRepository.count());
        seasonAnalyticsDto.setTotalLoans(loanRepository.countLoansBySeason(seasonId));
        seasonAnalyticsDto.setTotalLoansTaken(loanRepository.countApprovedLoans(seasonId));
        seasonAnalyticsDto.setTotalSupportFunds(loanRepository.getTotalLoanAmountBySeason(seasonId));
        seasonAnalyticsDto.setTotalSales(marketTransactionRepository.getTotalSalesBySeason(seasonId));
        return seasonAnalyticsDto;
    }

    @Override
    public SeasonComparisonDto compareSeasons(Long season1, Long season2) {
        SeasonAnalyticsDto s1 = getSeasonAnalytics(season1);
        SeasonAnalyticsDto s2 = getSeasonAnalytics(season2);

        SeasonComparisonDto seasonComparisonDto = new SeasonComparisonDto();
        seasonComparisonDto.setSeason1(s1.getSeasonName());
        seasonComparisonDto.setSeason2(s2.getSeasonName());

        seasonComparisonDto.setSalesGrowthPercentage(calcGrowth(s1.getTotalSales(),
                s2.getTotalSales()));
        seasonComparisonDto.setFarmersGrowthPercentage(calcGrowth(
                toDouble(s1.getTotalFarmers()),
                toDouble(s2.getTotalFarmers())
                ));
        return seasonComparisonDto;
    }

    private double toDouble(Number value) {
        return value ==null?0.0:value.doubleValue();
    }


    @Override
    public List<SeasonGraphDto> getAllSeasonAnalytics() {
        List<FarmingSeason> seasons = farmingSeasonRepository.findAll();
        List<SeasonGraphDto> list = new ArrayList<>();
        for(FarmingSeason season:seasons){
            SeasonGraphDto seasonGraphDto = new SeasonGraphDto();
            seasonGraphDto.setSeasonName(season.getSeasonName());
            seasonGraphDto.setTotalLoans(marketTransactionRepository.getTotalSalesBySeason(season.getId()));
            seasonGraphDto.setTotalLoans(loanRepository.getTotalLoanAmountBySeason(season.getId()));
            list.add(seasonGraphDto);
        }
        return list;
    }
    private double calcGrowth(double oldVal, double newVal){
        if (oldVal==0)
            return 100.0;
        return ((newVal-oldVal)/oldVal)*100;
    }
}
