package com.Farm.NASMS.Service;

import com.Farm.NASMS.dto.AgrovetProductResponse;

import java.util.List;

public interface FarmerInputCatalogueService {
    List<AgrovetProductResponse> searchAvailableProducts(String search);
}
