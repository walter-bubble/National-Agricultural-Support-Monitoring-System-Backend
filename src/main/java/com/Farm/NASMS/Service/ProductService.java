package com.Farm.NASMS.Service;

import com.Farm.NASMS.model.Product;
import com.Farm.NASMS.dto.ProductRequest;

import java.util.List;

public interface ProductService {
List<Product> getAllProduct();
Product addProduct(ProductRequest request);
Product updateProductById(Long id, ProductRequest request);
String deleteProduct(Long id);
}
