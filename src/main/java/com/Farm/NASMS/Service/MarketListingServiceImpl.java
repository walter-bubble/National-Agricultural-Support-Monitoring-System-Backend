package com.Farm.NASMS.Service;

import com.Farm.NASMS.model.MarketListing;
import com.Farm.NASMS.Repository.MarketListingRepository;
import com.Farm.NASMS.security.AuthenticatedUserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MarketListingServiceImpl implements MarketListingService{
  private final MarketListingRepository marketListingRepository;
  private final AuthenticatedUserService authenticatedUserService;
  public MarketListingServiceImpl(MarketListingRepository marketListingRepository,AuthenticatedUserService authenticatedUserService){
      this.marketListingRepository=marketListingRepository;
      this.authenticatedUserService = authenticatedUserService;
  }
    @Override
    public MarketListing createList(MarketListing listing) {
        if (!authenticatedUserService.isCurrentSeller(listing.getSellerId())) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "Access denied");
        }
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
        if (!authenticatedUserService.isCurrentSeller(sellerId)) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "Access denied");
        }
        return marketListingRepository.findBySellerId(sellerId);
    }

    @Override
    public List<MarketListing> getProductByName(String productName) {
        return marketListingRepository.findByProductName(productName);
    }

    @Override
    public MarketListing updateProductList(Long id,MarketListing listing) {
      MarketListing existing = marketListingRepository.findById(id)
              .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"LISTING NOT FOUND"));
        if (!authenticatedUserService.isListingOwner(existing)) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "Access denied");
        }
        existing.setProductName(listing.getProductName());
        existing.setProductCode(listing.getProductCode());
        existing.setQuantity(listing.getQuantity());
        existing.setPrice(listing.getPrice());

        return marketListingRepository.save(existing);
    }

    @Override
    public void deleteListing(Long id) {
        MarketListing existing = marketListingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "Listing not found"
                ));
        if (!authenticatedUserService.isListingOwner(existing) ) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN, "Access denied");
        }
        marketListingRepository.deleteById(id);

    }
}
