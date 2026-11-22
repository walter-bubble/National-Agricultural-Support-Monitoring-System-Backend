package com.Farm.NASMS.Controller;

import com.Farm.NASMS.dto.FarmerResponse;
import com.Farm.NASMS.dto.FarmerUpdateRequest;
import com.Farm.NASMS.model.Farmer;
import com.Farm.NASMS.Service.FarmerService;

import com.Farm.NASMS.security.AuthenticatedUserService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/farmers")
public class FarmerController {
   private final FarmerService farmerService;
    private final AuthenticatedUserService authenticatedUserService;
    public FarmerController(FarmerService farmerService, AuthenticatedUserService authenticatedUserService){
        this.farmerService = farmerService;
        this.authenticatedUserService = authenticatedUserService;
    }
    @GetMapping
    public List<FarmerResponse> getAllFarmers(){
        return farmerService.getAllFarmers();
    }
    @GetMapping("/{id}")
    public FarmerResponse getFarmerById(@PathVariable Long id){
        return farmerService.getFarmerById(id);
    }
    @GetMapping("/search/{nationalId}")
    public FarmerResponse getFarmerByNationalId(@PathVariable Long nationalId){
        return farmerService.getFarmerByNationalId(nationalId);
    }
    @PutMapping("/{id}")
    public FarmerResponse updateFarmer(@PathVariable Long id, @Valid @RequestBody FarmerUpdateRequest request){
        return farmerService.updateFarmer(id,request);
    }
    @DeleteMapping("/{id}")
    public String deleteFarmer(@PathVariable Long id){
        farmerService.deleteFarmer(id);
        return "Farmer deleted successfully";
    }
}
