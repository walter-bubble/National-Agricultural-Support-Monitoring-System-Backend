package com.Farm.NASMS.Service;

import com.Farm.NASMS.model.MarketListing;
import com.Farm.NASMS.Repository.MarketListingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MarketListingServiceImpl implements MarketListingService{
  private MarketListingRepository marketListingRepository;
  public MarketListingServiceImpl(MarketListingRepository marketListingRepository){
      this.marketListingRepository=marketListingRepository;
  }
    @Override
    public MarketListing createList(MarketListing listing) {
        return marketListingRepository.save(listing);
    }

    @Override
    public List<MarketListing> getAllListing() {
        return marketListingRepository.findAll();
    }

    @Override
    public Optional<MarketListing> getProductById(Long id) {
        return marketListingRepository.findById(id);
    }

    @Override
    public List<MarketListing> getProductBySellerId(Long sellerId) {
        return marketListingRepository.findBySellerId(sellerId);
    }

    @Override
    public List<MarketListing> getProductByName(String productName) {
        return marketListingRepository.findByProductName(productName);
    }

    @Override
    public MarketListing updateProductList(MarketListing listing) {
        return marketListingRepository.save(listing);
    }

    @Override
    public void deleteListing(Long id) {
        marketListingRepository.deleteById(id);

    }
}
