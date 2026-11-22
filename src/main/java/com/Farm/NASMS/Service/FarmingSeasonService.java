package com.Farm.NASMS.Service;

import com.Farm.NASMS.dto.FarmingSeasonRequest;
import com.Farm.NASMS.dto.FarmingSeasonResponse;
import com.Farm.NASMS.model.FarmingSeason;

import java.util.List;

public interface FarmingSeasonService {
    FarmingSeasonResponse createSeason(FarmingSeasonRequest request);
    List<FarmingSeasonResponse> getAllSeasons();
    FarmingSeasonResponse getSeasonById(Long id);
    FarmingSeasonResponse updateSeason(Long id, FarmingSeasonRequest request);
    void deleteSeason(Long id);
}
