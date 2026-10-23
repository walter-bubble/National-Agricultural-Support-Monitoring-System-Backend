package com.Farm.NASMS.Service;

import com.Farm.NASMS.Repository.MarketPlaceProductRepository;
import com.Farm.NASMS.dto.CreateProductRequest;
import com.Farm.NASMS.dto.ProductResponse;
import com.Farm.NASMS.enums.ProductStatus;
import com.Farm.NASMS.model.Farmer;
import com.Farm.NASMS.model.MarketPlaceProduct;
import com.Farm.NASMS.security.AuthenticatedUserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
public class MarketPlaceProductServiceImpl implements MarketPlaceProductService{
    private AuthenticatedUserService authenticatedUserService;
    private MarketPlaceProductRepository marketPlaceProductRepository;
    public MarketPlaceProductServiceImpl(AuthenticatedUserService authenticatedUserService,MarketPlaceProductRepository marketPlaceProductRepository){
        this.authenticatedUserService=authenticatedUserService;
        this.marketPlaceProductRepository=marketPlaceProductRepository;
    }
    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        Farmer seller = authenticatedUserService.getCurrentFarmer();
        MarketPlaceProduct product = new MarketPlaceProduct();
        product.setProductName(request.getProductName());
        product.setPrice(request.getPrice());
        product.setQuantityAvailable(request.getQuantityAvailable());
        product.setSeller(seller);
        product.setStatus(ProductStatus.ACTIVE);

        MarketPlaceProduct saved = marketPlaceProductRepository.save(product);

        return toResponse(saved);
    }
    @Override
    @Transactional(readOnly=true)
    public List<ProductResponse> getAvailableProducts() {
        return marketPlaceProductRepository
                .findByStatusOrderByCreatedAtDesc(ProductStatus.ACTIVE)
                .stream()
                .map(this::toResponse)
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        MarketPlaceProduct product = marketPlaceProductRepository.findByIdAndStatus(id,ProductStatus.ACTIVE)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found"));
        return toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getMyProducts() {
        Farmer seller = authenticatedUserService.getCurrentFarmer();
        return marketPlaceProductRepository.findBySeller_IdOrderByCreatedAtDesc(seller.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProductResponse updateProducts(Long id, CreateProductRequest request) {
        Farmer currentFarmer = authenticatedUserService.getCurrentFarmer();
        MarketPlaceProduct product = marketPlaceProductRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found!"));
        if(!product.getSeller().getId().equals(currentFarmer.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You can only update your own products");
        }
        if(product.getStatus()==ProductStatus.INACTIVE){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"An inactive product cannot be updated!");
        }
        product.setProductName(request.getProductName());
        product.setPrice(request.getPrice());
        product.setQuantityAvailable(request.getQuantityAvailable());
        return toResponse(marketPlaceProductRepository.save(product));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Farmer currentFarmer = authenticatedUserService.getCurrentFarmer();
        MarketPlaceProduct product = marketPlaceProductRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found"));
        if(!product.getSeller().getId().equals(currentFarmer.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"you can only deactivate you own products!");
        }
        if(product.getStatus() ==ProductStatus.INACTIVE){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"product already inactive!");
        }
        product.setStatus(ProductStatus.INACTIVE);
        marketPlaceProductRepository.save(product);

    }
    private ProductResponse toResponse(MarketPlaceProduct product) {
        return new ProductResponse(
                product.getId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantityAvailable(),
                product.getStatus(),
                product.getUpdatedAt(),
                product.getCreatedAt(),
                product.getSeller().getId()
        );
    }
}
