package com.Farm.NASMS.Service;

import com.Farm.NASMS.dto.CreateProductRequest;
import com.Farm.NASMS.dto.ProductResponse;

import java.util.List;

public interface MarketPlaceProductService {
ProductResponse createProduct(CreateProductRequest request);
List<ProductResponse> getAvailableProducts();
ProductResponse getProductById(Long id);
List<ProductResponse> getMyProducts();
ProductResponse  updateProducts(Long id,CreateProductRequest request);
void deleteProduct(Long id);
}
