package com.Farm.NASMS.Service;

import com.Farm.NASMS.dto.FarmingSeasonRequest;
import com.Farm.NASMS.dto.FarmingSeasonResponse;
import com.Farm.NASMS.model.FarmingSeason;
import com.Farm.NASMS.Repository.FarmingSeasonRepository;
import com.walter.toolkit.security_starter.audit.Auditable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
public class FarmingSeasonServiceImpl implements FarmingSeasonService{
    private final FarmingSeasonRepository farmingSeasonRepository;
    public FarmingSeasonServiceImpl(FarmingSeasonRepository farmingSeasonRepository){
        this.farmingSeasonRepository = farmingSeasonRepository;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "FARMING_SEASON_CREATED")
    public FarmingSeasonResponse createSeason(FarmingSeasonRequest request) {
        if (request.endDate().isBefore(request.startDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date cannot be before start date");
        }
        FarmingSeason season = new FarmingSeason();
        season.setSeasonName(request.seasonName().trim());
        season.setBudget(request.budget());
        season.setStartDate(request.startDate());
        season.setEndDate(request.endDate());
        season.setClosed(Boolean.TRUE.equals(request.closed()));

        return FarmingSeasonResponse.from(farmingSeasonRepository.save(season));
    }

    @Override
    public List<FarmingSeasonResponse> getAllSeasons() {
        return farmingSeasonRepository.findAll()
                .stream()
                .map(FarmingSeasonResponse::from)
                .toList();
    }

    @Override
    public FarmingSeasonResponse getSeasonById(Long id) {
        FarmingSeason season = farmingSeasonRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Season not found!"));
        return FarmingSeasonResponse.from(season);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "FARMING_SEASON_UPDATED")
    public FarmingSeasonResponse updateSeason(Long id, FarmingSeasonRequest request){
        if (request.endDate().isBefore(request.startDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date cannot be before start date");
        }
        FarmingSeason existing = farmingSeasonRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Season not found!"));
        existing.setSeasonName(request.seasonName().trim());
        existing.setBudget(request.budget());
        existing.setStartDate(request.startDate());
        existing.setEndDate(request.endDate());
        if (request.closed() != null) {
            existing.setClosed(request.closed());
        }

        return FarmingSeasonResponse.from(farmingSeasonRepository.save(existing));
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "FARMING_SEASON_DELETED")
    public void deleteSeason(Long id) {
        if (!farmingSeasonRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Season not found");
        }
        farmingSeasonRepository.deleteById(id);
    }
}
