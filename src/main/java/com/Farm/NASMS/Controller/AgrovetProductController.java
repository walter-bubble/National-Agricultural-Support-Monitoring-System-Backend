package com.Farm.NASMS.Controller;

import com.Farm.NASMS.Service.AgrovetProductService;
import com.Farm.NASMS.dto.AgrovetProductRequest;
import com.Farm.NASMS.dto.AgrovetProductResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/agrovet-product")
public class AgrovetProductController {
    private final AgrovetProductService agrovetProductService;
    public AgrovetProductController(AgrovetProductService agrovetProductService) {
        this.agrovetProductService = agrovetProductService;
    }
    @PostMapping
    public ResponseEntity<AgrovetProductResponse> addProduct(@Valid @RequestBody AgrovetProductRequest request) {
        AgrovetProductResponse response = agrovetProductService.addProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/my-products")
    public ResponseEntity<List<AgrovetProductResponse>> getMyProducts() {
        return ResponseEntity.ok(agrovetProductService.getMyProducts());
    }
    @PutMapping("/{productId}")
    public ResponseEntity<AgrovetProductResponse> updateProduct(@PathVariable Long productId, @Valid @RequestBody AgrovetProductRequest request) {
        return ResponseEntity.ok(agrovetProductService.updateProduct(productId, request));
    }
    @PatchMapping("/{productId}/deactivate")
    public ResponseEntity<AgrovetProductResponse> deactivateProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(agrovetProductService.deactivateProduct(productId));
    }
    @GetMapping("/admin/all")
    public ResponseEntity<List<AgrovetProductResponse>> getAllProducts() {
        return ResponseEntity.ok(agrovetProductService.getAllProducts());
    }
    @GetMapping("/admin/{productId}")
    public ResponseEntity<AgrovetProductResponse> getProductById(@PathVariable Long productId) {
        return ResponseEntity.ok(agrovetProductService.getProductById(productId));
    }
}
