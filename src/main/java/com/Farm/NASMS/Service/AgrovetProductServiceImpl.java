package com.Farm.NASMS.Service;

import com.Farm.NASMS.Repository.AgrovetProductRepository;
import com.Farm.NASMS.Repository.AgrovetRepository;
import com.Farm.NASMS.dto.*;
import com.Farm.NASMS.enums.AgrovetOperationalStatus;
import com.Farm.NASMS.enums.AgrovetStatus;
import com.Farm.NASMS.model.Agrovet;
import com.Farm.NASMS.model.AgrovetProducts;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class AgrovetProductServiceImpl implements AgrovetProductService {
    private final AgrovetProductRepository productRepository;
    private final AgrovetRepository agrovetRepository;
    public AgrovetProductServiceImpl(AgrovetProductRepository productRepository, AgrovetRepository agrovetRepository) {
        this.productRepository = productRepository;
        this.agrovetRepository = agrovetRepository;
    }
    private String getLoggedInEmail() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }
    private Agrovet getLoggedInAgrovet() {
        return agrovetRepository.findByUser_EmailAddress(getLoggedInEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agrovet account not found"));
    }

    private void ensureCanManageProducts(Agrovet agrovet) {
        if (agrovet.getStatus() != AgrovetStatus.APPROVED) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Your Agrovet must be approved first");
        }
        if (agrovet.getOperationalStatus() != AgrovetOperationalStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Your Agrovet is currently inactive");
        }
    }

    @Override
    @PreAuthorize("hasRole('AGROVET')")
    public AgrovetProductResponse addProduct(AgrovetProductRequest request) {
        Agrovet agrovet = getLoggedInAgrovet();
        ensureCanManageProducts(agrovet);

        if (request.getQuantityAvailable() == null || request.getQuantityAvailable().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Initial quantity must be greater than zero");
        }
        AgrovetProducts product = new AgrovetProducts();
        product.setAgrovet(agrovet);
        product.setProductName(request.getProductName().trim());
        product.setCategory(request.getCategory());
        product.setBrand(request.getBrand());
        product.setDescription(request.getDescription());

        // NASMS records the initial quantity automatically.
        product.setInitialQuantity(request.getQuantityAvailable());
        product.setQuantityAvailable(request.getQuantityAvailable());
        product.setPrice(request.getPrice());
        product.setUnit(request.getUnit().trim());
        product.setActive(true);
        return toResponse(productRepository.save(product));
    }
    @Override
    @PreAuthorize("hasRole('AGROVET')")
    public AgrovetProductResponse updateProduct(Long productId, AgrovetProductRequest request) {
        Agrovet agrovet = getLoggedInAgrovet();
        ensureCanManageProducts(agrovet);
        AgrovetProducts product = productRepository.findByIdAndAgrovetId(productId, agrovet.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found in your inventory"));
        product.setProductName(request.getProductName().trim());
        product.setCategory(request.getCategory());
        product.setBrand(request.getBrand());
        product.setDescription(request.getDescription());
        product.setQuantityAvailable(request.getQuantityAvailable());
        product.setPrice(request.getPrice());
        product.setUnit(request.getUnit().trim());

        // Do not change initialQuantity during an update.
        return toResponse(productRepository.save(product));
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('AGROVET')")
    public List<AgrovetProductResponse> getMyProducts() {
        Agrovet agrovet = getLoggedInAgrovet();
        return productRepository.findByAgrovetId(agrovet.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<AgrovetProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public AgrovetProductResponse getProductById(Long productId) {
        AgrovetProducts product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return toResponse(product);
    }

    @Override
    @PreAuthorize("hasRole('AGROVET')")
    public AgrovetProductResponse deactivateProduct(Long productId) {
        Agrovet agrovet = getLoggedInAgrovet();
        ensureCanManageProducts(agrovet);
        AgrovetProducts product = productRepository.findByIdAndAgrovetId(productId, agrovet.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found in your inventory"));
        product.setActive(false);
        return toResponse(productRepository.save(product));
    }
    private AgrovetProductResponse toResponse(AgrovetProducts product) {

        AgrovetProductResponse response = new AgrovetProductResponse();
        response.setId(product.getId());
        response.setAgrovetId(product.getAgrovet().getId());
        response.setAgrovetName(product.getAgrovet().getBusinessName());
        response.setProductName(product.getProductName());
        response.setCategory(product.getCategory());
        response.setBrand(product.getBrand());
        response.setDescription(product.getDescription());
        response.setInitialQuantity(product.getInitialQuantity());
        response.setQuantityAvailable(product.getQuantityAvailable());
        response.setPrice(product.getPrice());
        response.setUnit(product.getUnit());
        response.setActive(product.isActive());
        response.setStockStatus(calculateStockStatus(product));
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        return response;
    }
    private String calculateStockStatus(AgrovetProducts product) {
        BigDecimal available = product.getQuantityAvailable();
        BigDecimal initial = product.getInitialQuantity();
        if (available.compareTo(BigDecimal.ZERO) <= 0) {
            return "OUT_OF_STOCK";
        }
        BigDecimal criticalThreshold = initial.multiply(new BigDecimal("0.10"));
        BigDecimal lowThreshold = initial.multiply(new BigDecimal("0.30"));
        if (available.compareTo(criticalThreshold) <= 0) {
            return "CRITICALLY_LOW";
        }
        if (available.compareTo(lowThreshold) <= 0) {
            return "LOW_STOCK";
        }
        return "IN_STOCK";
    }
}
