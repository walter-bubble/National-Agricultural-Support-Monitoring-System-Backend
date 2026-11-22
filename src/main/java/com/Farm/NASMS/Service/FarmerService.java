package com.Farm.NASMS.Service;

import com.Farm.NASMS.dto.FarmerResponse;
import com.Farm.NASMS.dto.FarmerUpdateRequest;
import com.Farm.NASMS.model.Farmer;

import java.util.List;

public interface FarmerService {
    List<FarmerResponse> getAllFarmers();
    FarmerResponse getFarmerById(Long id);
    FarmerResponse getFarmerByNationalId(Long nationalId);
    FarmerResponse updateFarmer(Long id, FarmerUpdateRequest request);
    void deleteFarmer(Long id);


}
