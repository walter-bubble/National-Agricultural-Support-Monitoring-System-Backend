package com.Farm.NASMS;

import com.Farm.NASMS.Repository.FarmingSeasonRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;



@Component
@EnableScheduling
public class SeasonScheduler {
    private final FarmingSeasonRepository farmRepo;

    public SeasonScheduler(FarmingSeasonRepository farmRepo) {
        this.farmRepo = farmRepo;
    }

    private LocalDate endDate;

    @Scheduled(cron = "0 0 0 * * ?")
    public Void autoCloseSeason() {
        for (FarmingSeason s : farmRepo.findAll()) {
            if (!s.isClosed() && s.getEndDate().isBefore(LocalDate.now()))
                s.setClosed(true);
            farmRepo.save(s);
        }
        return null;
    }

}
