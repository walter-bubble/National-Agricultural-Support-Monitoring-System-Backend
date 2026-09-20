package com.Farm.NASMS.Service;

import com.Farm.NASMS.Repository.UserRepository;
import com.Farm.NASMS.model.Farmer;
import com.Farm.NASMS.Repository.FarmerRepository;
import com.Farm.NASMS.model.User;
import com.Farm.NASMS.security.AuthenticatedUserService;
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
    public List<Farmer> getAllFarmers() {
        return farmerRepository.findAll();
    }
    @Override
    public Farmer getFarmerById(Long id) {
        if(!authenticatedUserService.isCurrentFarmer(id)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access denied");
        }
        return farmerRepository.findById(id).orElseThrow(()-> new RuntimeException("Farmer not found!"));
    }
    @Override
    public Farmer getFarmerByNationalId(Long nationalId) {
        if(!authenticatedUserService.isCurrentFarmerNationalId(nationalId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access denied");
        }
        return farmerRepository.findByNationalId(nationalId)
                .orElseThrow(()-> new RuntimeException("Farmer not found!"));
    }
    @Override
    public Farmer updateFarmer(Long id, Farmer farmer) {
        if(!authenticatedUserService.isCurrentFarmer(id)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access denied");
        }
        Farmer existingFarmer = getFarmerById(id);
        existingFarmer.setEmail(farmer.getEmail());
        existingFarmer.setCounty(farmer.getCounty());
        existingFarmer.setFarmSize(farmer.getFarmSize());
        existingFarmer.setPhoneNumber(farmer.getPhoneNumber());
        return farmerRepository.save(existingFarmer);
    }
    @Override
    public void deleteFarmer(Long id) {
        if(!authenticatedUserService.isCurrentFarmer(id)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access denied");
        }
        Farmer farmer = getFarmerById(id);
        farmerRepository.delete(farmer);
    }
}
