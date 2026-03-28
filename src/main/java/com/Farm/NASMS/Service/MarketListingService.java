package com.Farm.NASMS.Service;

import java.util.List;
import java.util.Optional;

public interface MarketListing {
    MarketListing createList(MarketListing listing);
    List<MarketListing> getAllListing();

    Optional<MarketListing> getProductByCode(String productCode);
    MarketListing getProductBySellerId(Long sellerId);
    MarketListing getProductByName(String productName);
    MarketListing updateProductList(MarketListing listing);
    void deleteListing(String productCode);
}

