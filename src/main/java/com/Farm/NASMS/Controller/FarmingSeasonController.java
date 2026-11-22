package com.Farm.NASMS.Controller;

import com.Farm.NASMS.dto.FarmingSeasonRequest;
import com.Farm.NASMS.dto.FarmingSeasonResponse;
import com.Farm.NASMS.model.FarmingSeason;
import com.Farm.NASMS.Service.FarmingSeasonService;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seasons")
public class FarmingSeasonController {
    private final FarmingSeasonService farmingSeasonService;
    public FarmingSeasonController(FarmingSeasonService farmingSeasonService){
        this.farmingSeasonService=farmingSeasonService;
    }
    @PostMapping
    public FarmingSeasonResponse createSeason(@Valid @RequestBody FarmingSeasonRequest request){
        return farmingSeasonService.createSeason(request);
    }
    @GetMapping
    public List<FarmingSeasonResponse> getAllSeasons(){
        return farmingSeasonService.getAllSeasons();
    }
    @GetMapping("/{id}")
    public FarmingSeasonResponse getSeason(@PathVariable Long id){
        return farmingSeasonService.getSeasonById(id);
    }
    @PutMapping("/{id}")
    public FarmingSeasonResponse updateFarmingSeason(@PathVariable Long id, @Valid @RequestBody FarmingSeasonRequest request){
        return farmingSeasonService.updateSeason(id,request);
    }
    @DeleteMapping("/{id}")
    public void deleteSeason(@PathVariable Long id){
        farmingSeasonService.deleteSeason(id);
    }
}
