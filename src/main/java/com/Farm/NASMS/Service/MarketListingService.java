package com.Farm.NASMS.Service;

import com.Farm.NASMS.model.MarketListing;

import java.util.List;
import java.util.Optional;

public interface MarketListingService {
    MarketListing createList(MarketListing listing);
    List<MarketListing> getAllListing();

    Optional<MarketListing> getProductById(Long id);
    List<MarketListing> getProductBySellerId(Long sellerId);
    List<MarketListing> getProductByName(String productName);
    MarketListing updateProductList(MarketListing listing);
    void deleteListing(Long id);
}

