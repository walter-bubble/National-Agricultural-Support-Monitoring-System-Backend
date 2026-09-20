package com.Farm.NASMS.Service;

import com.Farm.NASMS.model.MarketTransaction;
import com.Farm.NASMS.Repository.MarketTransactionRepository;
import com.Farm.NASMS.security.AuthenticatedUserService;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MarketTransactionImpl implements MarketTransactionService{
    private final AuthenticatedUserService authenticatedUserService;
    private MarketTransactionRepository marketTransactionRepository;
    public MarketTransactionImpl(MarketTransactionRepository marketTransactionRepository,AuthenticatedUserService authenticatedUserService){
        this.marketTransactionRepository = marketTransactionRepository;
        this.authenticatedUserService = authenticatedUserService;
    }

    @Override
    public MarketTransaction createTransaction(MarketTransaction transaction) {
        if (!authenticatedUserService.isAdmin() && !authenticatedUserService.isCurrentBuyer(transaction.getBuyerId())){
            throw new ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN,"Access denied");
        }
        return marketTransactionRepository.save(transaction);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<MarketTransaction> getAllTransactions() {
        return marketTransactionRepository.findAll();
    }

    @Override
    public List<MarketTransaction> getTransactionBySellerId(Long sellerId) {
        if(!authenticatedUserService.isAdmin() && !authenticatedUserService.isCurrentSeller(sellerId)){
            throw new ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN,"Access denied");
        }
        return marketTransactionRepository.findBySellerId(sellerId);
    }

    @Override
    public List<MarketTransaction> getTransactionByBuyerId(Long buyerId) {
        if(!authenticatedUserService.isAdmin() && !authenticatedUserService.isCurrentBuyer(buyerId)){
            throw new ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN,"Acess denied");
        }
        return marketTransactionRepository.findByBuyerId(buyerId);
    }

    @Override
    public Optional<MarketTransaction> getTransactionById(Long id) {
        return marketTransactionRepository.findById(id);
    }

    @Override
    public MarketTransaction updateTransaction(Long id,MarketTransaction marketTransaction) {
        MarketTransaction existing = marketTransactionRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Transaction not found"));

        if(!authenticatedUserService.isTransactionOwner(existing) && !authenticatedUserService.isAdmin()){
            throw new ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN,"Access denied");
        }
        existing.setSellerType(marketTransaction.getSellerType());
        existing.setPrice(marketTransaction.getPrice());
        existing.setQuantityRequested(marketTransaction.getQuantityRequested());
        existing.setProductName(marketTransaction.getProductName());
        existing.setProductCode(marketTransaction.getProductCode());

        return marketTransactionRepository.save(existing);
    }

    @Override
    public void deleteTransaction(Long id) {
        MarketTransaction existing = marketTransactionRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Transaction not found"));
        if(!authenticatedUserService.isTransactionOwner(existing) && !authenticatedUserService.isAdmin()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access denied");
        }
        marketTransactionRepository.delete(existing);

    }
}
