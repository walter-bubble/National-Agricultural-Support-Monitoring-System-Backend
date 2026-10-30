package com.Farm.NASMS.Service;

import com.Farm.NASMS.Repository.AgrovetProductRepository;
import com.Farm.NASMS.dto.AgrovetProductResponse;
import com.Farm.NASMS.enums.AgrovetOperationalStatus;
import com.Farm.NASMS.enums.AgrovetStatus;
import com.Farm.NASMS.model.AgrovetProducts;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
@Service
public class FarmerInputCatalogueServiceImpl implements FarmerInputCatalogueService{
    private final AgrovetProductRepository productRepository;

    public FarmerInputCatalogueServiceImpl(AgrovetProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Override
    @Transactional(readOnly = true)
    public List<AgrovetProductResponse> searchAvailableProducts(
            String search) {

        return productRepository.findByActiveTrue()
                .stream()
                .filter(product ->
                        product.getQuantityAvailable()
                                .compareTo(BigDecimal.ZERO) > 0)
                .filter(product ->
                        product.getAgrovet().getStatus() == AgrovetStatus.APPROVED)
                .filter(product ->
                        product.getAgrovet().getOperationalStatus() == AgrovetOperationalStatus.ACTIVE)
                .filter(product ->
                        search == null || search.isBlank()
                                || product.getProductName()
                                .toLowerCase()
                                .contains(search.trim().toLowerCase()))
                .map(this::toResponse)
                .toList();
    }

    private AgrovetProductResponse toResponse(AgrovetProducts product) {
        AgrovetProductResponse response = new AgrovetProductResponse();
        response.setId(product.getId());
        response.setAgrovetId(product.getAgrovet().getId());
        response.setAgrovetName(product.getAgrovet().getBusinessName());
        response.setProductName(product.getProductName());
        response.setCategory(product.getCategory());
        response.setBrand(product.getBrand());
        response.setDescription(product.getDescription());
        response.setInitialQuantity(product.getInitialQuantity());
        response.setQuantityAvailable(product.getQuantityAvailable());
        response.setPrice(product.getPrice());
        response.setUnit(product.getUnit());
        response.setActive(product.isActive());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        response.setStockStatus(calculateStockStatus(product));
        return response;
    }
    private String calculateStockStatus(AgrovetProducts product) {
        BigDecimal available = product.getQuantityAvailable();
        BigDecimal initial = product.getInitialQuantity();
        if (available.compareTo(BigDecimal.ZERO) <= 0) {
            return "OUT_OF_STOCK";
        }
        BigDecimal criticalThreshold = initial.multiply(new BigDecimal("0.10"));
        BigDecimal lowThreshold = initial.multiply(new BigDecimal("0.30"));
        if (available.compareTo(criticalThreshold) <= 0) {
            return "CRITICALLY_LOW";
        }
        if (available.compareTo(lowThreshold) <= 0) {
            return "LOW_STOCK";
        }
        return "IN_STOCK";
    }
}
