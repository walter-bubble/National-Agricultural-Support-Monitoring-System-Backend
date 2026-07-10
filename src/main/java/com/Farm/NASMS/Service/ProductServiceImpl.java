package com.Farm.NASMS.Service;

import com.Farm.NASMS.Repository.ProductRepo;
import com.Farm.NASMS.model.Farmer;
import com.Farm.NASMS.model.Product;
import com.Farm.NASMS.model.ProductRequest;
import com.Farm.NASMS.Repository.FarmerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepo productRepo;
    private FarmerRepository farmerRepository;
    public ProductServiceImpl(ProductRepo productRepo,FarmerRepository farmerRepository){
        this.productRepo = productRepo;
        this.farmerRepository = farmerRepository;
    }
    @Override
    public List<Product> getAllProduct() {
        return productRepo.findAll();
    }

    @Override
    public Product addProduct(ProductRequest request) {
        Farmer farmer = farmerRepository.findById(request.getFarmerId())
                .orElseThrow(()-> new RuntimeException("Farmer not found!"));
        Product product = new Product();
        product.setName(request.getName());
        product.setQuantityUnit(request.getQuantityRequested());
        product.setUnitPrice_ksh(request.getTotalInvoice());
        product.setFarmer(farmer);
        return productRepo.save(product);
    }

    @Override
    public Product updateProductById(Long id,ProductRequest request) {
        Product existingProduct = productRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Product not found"));
        /*existingProduct.setAgroforestryProduct(product.getAgroforestryProduct());
        existingProduct.setAquacultureProduct(product.getAquacultureProduct());
        existingProduct.setCropProduct(product.getCropProduct());
        existingProduct.setFarmingType(product.getFarmingType());
        existingProduct.setHorticulturalProduct(product.getHorticulturalProduct());
        existingProduct.setLivestockProduct(product.getLivestockProduct());
        existingProduct.setPoultryProduct(product.getPoultryProduct());
        existingProduct.setProductStatus(product.getProductStatus());*/
        BeanUtils.copyProperties(request, existingProduct, "id", "farmer", "createdAt", "updatedAt");

        return productRepo.save(existingProduct);
    }

    @Override
    public void deleteProduct(Long id) {
       if(!productRepo.existsById(id)){
           throw new RuntimeException("Product not found");
       }
       productRepo.deleteById(id);
    }
}
