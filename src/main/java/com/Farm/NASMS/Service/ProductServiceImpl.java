package com.Farm.NASMS.Service;

import com.Farm.NASMS.Repository.ProductRepo;
import com.Farm.NASMS.model.Farmer;
import com.Farm.NASMS.model.Product;
import com.Farm.NASMS.dto.ProductRequest;
import com.Farm.NASMS.Repository.FarmerRepository;
import com.Farm.NASMS.security.AuthenticatedUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final FarmerRepository farmerRepository;
    private final AuthenticatedUserService authenticatedUserService;
    public ProductServiceImpl(ProductRepo productRepo,FarmerRepository farmerRepository,AuthenticatedUserService authenticatedUserService){
        this.productRepo = productRepo;
        this.farmerRepository = farmerRepository;
        this.authenticatedUserService = authenticatedUserService;
    }
    @Override
    public List<Product> getAllProduct() {
        return productRepo.findAll();
    }

    @Override
    public Product addProduct(ProductRequest request) {
        if(!authenticatedUserService.isCurrentFarmer(request.getFarmerId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access denied");
        }
        Farmer farmer = farmerRepository.findById(request.getFarmerId())
                .orElseThrow(()-> new RuntimeException("Farmer not found!"));
        Product product = new Product();
        product.setName(request.getName());
        product.setQuantityUnit(request.getQuantity());
        product.setUnitPrice_ksh(request.getUnitPrice());
        product.setFarmer(farmer);
        return productRepo.save(product);
    }

    @Override
    public Product updateProductById(Long id,ProductRequest request) {
        Product existingProduct = productRepo.findById(id)
                .orElseThrow(()->new RuntimeException("Product not found"));
        if (!authenticatedUserService.isProductOwner(existingProduct)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
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
    public String deleteProduct(Long id) {
        Product existing = productRepo.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Product doesn't exist"));
        if (!authenticatedUserService.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
       productRepo.delete(existing);
        return "Product Deleted SUCCESSFULLY";
    }
}
