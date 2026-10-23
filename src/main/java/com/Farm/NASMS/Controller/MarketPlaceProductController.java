package com.Farm.NASMS.Controller;

import com.Farm.NASMS.Service.MarketPlaceProductService;
import com.Farm.NASMS.dto.CreateProductRequest;
import com.Farm.NASMS.dto.ProductResponse;
import com.Farm.NASMS.model.MarketPlaceProduct;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/marketplace/products")
public class MarketPlaceProductController {
    private final MarketPlaceProductService marketPlaceProductService;
    public MarketPlaceProductController(MarketPlaceProductService marketPlaceProductService){
        this.marketPlaceProductService=marketPlaceProductService;
    }
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid  @RequestBody CreateProductRequest request){
        ProductResponse response = marketPlaceProductService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAvailableProducts(){
        return ResponseEntity.ok(marketPlaceProductService.getAvailableProducts());
    }
    @GetMapping("/mine")
    public ResponseEntity<List<ProductResponse>> getMyProducts(){
        return ResponseEntity.ok(marketPlaceProductService.getMyProducts());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){
        return ResponseEntity.ok(marketPlaceProductService.getProductById(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProducts(@PathVariable Long id ,@RequestBody CreateProductRequest request){
        return ResponseEntity.ok(marketPlaceProductService.updateProducts(id,request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        marketPlaceProductService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
