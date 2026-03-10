package com.Farm.NASMS.Service;

import com.Farm.NASMS.Product;
import com.Farm.NASMS.Repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;
    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProductById(Long id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Product not found"));
        /*existingProduct.setAgroforestryProduct(product.getAgroforestryProduct());
        existingProduct.setAquacultureProduct(product.getAquacultureProduct());
        existingProduct.setCropProduct(product.getCropProduct());
        existingProduct.setFarmingType(product.getFarmingType());
        existingProduct.setHorticulturalProduct(product.getHorticulturalProduct());
        existingProduct.setLivestockProduct(product.getLivestockProduct());
        existingProduct.setPoultryProduct(product.getPoultryProduct());
        existingProduct.setProductStatus(product.getProductStatus());*/
        BeanUtils.copyProperties(product, existingProduct, "id", "farmer", "createdAt", "updatedAt");

        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(Long id) {
       if(!productRepository.existsById(id)){
           throw new RuntimeException("Product not found");
       }
       productRepository.deleteById(id);
    }
}
