package com.Farm.NASMS.Service;

import com.Farm.NASMS.Repository.MarketListingRepository;
import com.Farm.NASMS.Repository.ProductRepo;
import com.Farm.NASMS.model.Farmer;
import com.Farm.NASMS.model.MarketListing;
import com.Farm.NASMS.model.MarketTransaction;
import com.Farm.NASMS.model.Product;
import com.Farm.NASMS.security.AuthenticatedUserService;
import jakarta.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class OrderProcessingService {
    private final AuthenticatedUserService authenticatedUserService;
    private final MarketTransactionService marketTransactionService;
    private final MarketListingRepository marketListingRepository;
    private final ProductRepo productRepo;

    public OrderProcessingService(MarketTransactionService marketTransactionService, MarketListingRepository marketListingRepository, AuthenticatedUserService authenticatedUserService, ProductRepo productRepo){
        this.marketListingRepository = marketListingRepository;
        this.marketTransactionService = marketTransactionService;
        this.authenticatedUserService = authenticatedUserService;
        this.productRepo = productRepo;
    }
    @Transactional
    public MarketTransaction processBuyerOrder(String productCode, double quantityRequested) {
        Farmer buyer = authenticatedUserService.getCurrentFarmer();

        Long buyerId = buyer.getId();
        String buyerName = buyer.getName();
        //fetch active stocks
        MarketListing listing =  marketListingRepository.findByProductCode(productCode)
                .orElseThrow(()-> new RuntimeException("Product doesn't exist!"));
        if (quantityRequested <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Quantity must be greater than zero"
            );
        }
        //avoid over purchasing
        if(listing.getQuantity()<quantityRequested){
            throw  new RuntimeException("Insufficient farming stock for this transaction!");
        }
        double totalInvoiceCost = listing.getPrice()*quantityRequested;
        //auto update after purchase
        listing.setQuantity(listing.getQuantity()-quantityRequested);
        if (listing.getQuantity()<=0){
            marketListingRepository.save(listing);
        }
        else{
            marketListingRepository.save(listing);
        }
        Product product = productRepo.findByProductCode(productCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product doesn't exist!"));
        //receipt
        MarketTransaction receipt = new MarketTransaction(
                listing.getSellerName(),
                listing.getSellerType(),
                listing.getProductName(),
                productCode,
               buyerName,
                quantityRequested,
                totalInvoiceCost
        );
        receipt.setSellerId(listing.getSellerId());
        receipt.setBuyerId(buyerId);
        receipt.setProduct(product);
        return marketTransactionService.createTransaction(receipt);
    }
}
