package com.Farm.NASMS.Service;

import com.Farm.NASMS.Repository.UserRepository;
import com.Farm.NASMS.dto.FarmerResponse;
import com.Farm.NASMS.dto.FarmerUpdateRequest;
import com.Farm.NASMS.model.Farmer;
import com.Farm.NASMS.Repository.FarmerRepository;
import com.Farm.NASMS.model.User;
import com.Farm.NASMS.security.AuthenticatedUserService;
import com.walter.toolkit.security_starter.audit.Auditable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FarmerServiceImpl implements FarmerService {
    private final FarmerRepository farmerRepository;
    private final UserRepository userRepository;
    private final AuthenticatedUserService authenticatedUserService;
    public FarmerServiceImpl(FarmerRepository farmerRepository,UserRepository userRepository,AuthenticatedUserService authenticatedUserService){
        this.farmerRepository=farmerRepository;
        this.userRepository=userRepository;
        this.authenticatedUserService = authenticatedUserService;
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<FarmerResponse> getAllFarmers() {
        return farmerRepository.findAll()
                .stream()
                .map(FarmerResponse::from)
                .toList();

    }
    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'FARMER')")
    public FarmerResponse getFarmerById(Long id) {
        if(!authenticatedUserService.isCurrentFarmer(id)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access denied");
        }
        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer not found!"));
        return FarmerResponse.from(farmer);
    }
    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'FARMER')")
    public FarmerResponse getFarmerByNationalId(Long nationalId) {
        if(!authenticatedUserService.isCurrentFarmerNationalId(nationalId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access denied");
        }
        Farmer farmer = farmerRepository.findByNationalId(nationalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer not found!"));
        return FarmerResponse.from(farmer);
    }
    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'FARMER')")
    @Auditable(action = "FARMER_PROFILE_UPDATED")
    public FarmerResponse updateFarmer(Long id, FarmerUpdateRequest request) {
        if(!authenticatedUserService.isCurrentFarmer(id)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access denied");
        }
        Farmer existingFarmer = farmerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer not found!"));

        existingFarmer.setName(request.name().trim());
        existingFarmer.setCounty(request.county().trim());
        existingFarmer.setFarmSize(request.farmSize());
        existingFarmer.setPhoneNumber(request.phoneNumber().trim());
        existingFarmer.setTitleNumber(request.titleNumber());

        return FarmerResponse.from(farmerRepository.save(existingFarmer));
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "FARMER_DELETED")
    public void deleteFarmer(Long id) {
        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer not found!"));
        farmerRepository.delete(farmer);
    }
}
