package com.Farm.NASMS.Service;

import com.Farm.NASMS.Repository.MarketListingRepository;
import com.Farm.NASMS.model.MarketListing;
import com.Farm.NASMS.model.MarketTransaction;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderProcessingService {
    private MarketTransactionService marketTransactionService;
    private MarketListingRepository marketListingRepository;

    public OrderProcessingService(MarketTransactionService marketTransactionService, MarketListingRepository marketListingRepository){
        this.marketListingRepository = marketListingRepository;
        this.marketTransactionService = marketTransactionService;
    }
    @Transactional
    public MarketTransaction processBuyerOrder(String productCode, Long buyerId, String buyerName, double quantityRequested) {
        //fetch active stocks
        MarketListing listing =  marketListingRepository.findByProductCode(productCode)
                .orElseThrow(()-> new RuntimeException("Product doesn't exist!"));
        //avoid over purchasing
        if(listing.getQuantity()<quantityRequested){
            throw  new RuntimeException("Insufficient farming stock for this transaction!");
        }
        //auto update after purchase
        listing.setQuantity(listing.getQuantity()-quantityRequested);
        if (listing.getQuantity()<=0){
            marketListingRepository.deleteByProductCode(productCode);
        }
        else{
            marketListingRepository.save(listing);
        }
        //calculating the price
        double totalInvoiceCost = listing.getPrice()*quantityRequested;
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
        return marketTransactionService.createTransaction(receipt);
    }
}
