package com.Farm.NASMS.Controller;

import com.Farm.NASMS.Service.FarmerInputCatalogueService;
import com.Farm.NASMS.dto.AgrovetProductResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/farmer/input-catalogue")
public class FarmerInputCatalogueController {
    private final FarmerInputCatalogueService catalogueService;

    public FarmerInputCatalogueController(
            FarmerInputCatalogueService catalogueService) {
        this.catalogueService = catalogueService;
    }
    @GetMapping
    public List<AgrovetProductResponse> searchProducts(@RequestParam(required = false) String search) {
        return catalogueService.searchAvailableProducts(search);
    }
}
