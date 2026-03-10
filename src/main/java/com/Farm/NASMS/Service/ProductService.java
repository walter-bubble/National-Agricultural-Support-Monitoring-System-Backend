package com.Farm.NASMS.Service;

import com.Farm.NASMS.Product;

import java.util.List;

public interface ProductService {
List<Product> getAllProduct();
Product addProduct(Product product);
Product updateProductById(Long id, Product product);
void deleteProduct(Long id);
}
