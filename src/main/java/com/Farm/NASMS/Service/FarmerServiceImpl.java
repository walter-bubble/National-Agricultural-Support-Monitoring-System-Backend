package com.Farm.NASMS.Service;

import com.Farm.NASMS.model.Farmer;
import com.Farm.NASMS.Repository.FarmerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FarmerServiceImpl implements FarmerService {
    private final FarmerRepository farmerRepository;
    public FarmerServiceImpl(FarmerRepository farmerRepository){
        this.farmerRepository=farmerRepository;
    }
    @Override
    public List<Farmer> getAllFarmers() {
        return farmerRepository.findAll();
    }
    @Override
    public Farmer getFarmerById(Long id) {
        return farmerRepository.findById(id).orElseThrow(()-> new RuntimeException("Farmer not found!"));
    }
    @Override
    public Farmer getFarmerByNationalId(Long nationalId) {
        return farmerRepository.findByNationalId(nationalId)
                .orElseThrow(()-> new RuntimeException("Farmer not found!"));
    }
    @Override
    public Farmer addFarmer(Farmer farmer) {
        return farmerRepository.save(farmer);
    }
    @Override
    public Farmer updateFarmer(Long id, Farmer farmer) {
        Farmer existingFarmer = getFarmerById(id);
        existingFarmer.setEmail(farmer.getEmail());
        existingFarmer.setCounty(farmer.getCounty());
        existingFarmer.setFarmSize(farmer.getFarmSize());
        existingFarmer.setPhoneNumber(farmer.getPhoneNumber());
        return farmerRepository.save(existingFarmer);
    }
    @Override
    public void deleteFarmer(Long id) {
        Farmer farmer = getFarmerById(id);
        farmerRepository.delete(farmer);
    }
}
