package com.Farm.NASMS.Service;

import com.Farm.NASMS.dto.AgrovetProductRequest;
import com.Farm.NASMS.dto.AgrovetProductResponse;

import java.util.List;

public interface AgrovetProductService {
    AgrovetProductResponse addProduct(AgrovetProductRequest request);
    AgrovetProductResponse updateProduct(Long productId, AgrovetProductRequest request);
    List<AgrovetProductResponse> getMyProducts();
    List<AgrovetProductResponse> getAllProducts();
    AgrovetProductResponse getProductById(Long productId);
    AgrovetProductResponse deactivateProduct(Long productId);
}
