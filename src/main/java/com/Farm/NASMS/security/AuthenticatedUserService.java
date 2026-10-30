package com.Farm.NASMS.security;

import com.Farm.NASMS.Repository.AgrovetRepository;
import com.Farm.NASMS.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;


import com.Farm.NASMS.Repository.FarmerRepository;

@Component
public class AuthenticatedUserService {
    private final FarmerRepository farmerRepository;
    private final AgrovetRepository agrovetRepository;
    public AuthenticatedUserService(FarmerRepository farmerRepository, AgrovetRepository agrovetRepository){
        this.farmerRepository = farmerRepository;
        this.agrovetRepository = agrovetRepository;
    }
    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null ||
                !authentication.isAuthenticated() ||
                !(authentication.getPrincipal() instanceof CustomUserDetails)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Unauthorized");
        }
        return ((CustomUserDetails) authentication.getPrincipal()).user();
    }
    public Farmer getCurrentFarmer(){
        User currentUser = getCurrentUser();
        return farmerRepository.findByUserId(currentUser.getId())
        .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Farmer not found"));
    }
    public boolean isAdmin(){
        return "ADMIN".equalsIgnoreCase(getCurrentUser().getRole());
    }
    public boolean isFarmer(){
        return "FARMER".equalsIgnoreCase(getCurrentUser().getRole());
    }
    public boolean isCurrentFarmer(Long farmerId){
        return isAdmin() || (isFarmer() && getCurrentFarmer().getId().equals(farmerId));
    }
    public boolean isCurrentFarmerNationalId(Long nationalId){
        return isAdmin() || (isFarmer() && getCurrentFarmer().getNationalId().equals(nationalId));
    }
    public boolean isLoanOwner(Loan loan){
        if(isAdmin()){
            return true;
        }
        return isFarmer() && loan.getFarmer() != null && loan.getFarmer().getId().equals(getCurrentFarmer().getId());
    }

    public boolean isCurrentBuyer(Long buyerId) {
        return isAdmin() || (isFarmer() && getCurrentFarmer().getId().equals(buyerId));
    }

    public boolean isCurrentSeller(Long sellerId) {
        return isAdmin() || (isFarmer() && getCurrentFarmer().getId().equals(sellerId));
    }
    public Agrovet getCurrentAgrovet() {
        User currentUser = getCurrentUser();

        if (!"AGROVET".equalsIgnoreCase(currentUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only Agrovets can access this resource");
        }
        return agrovetRepository
                .findByUser_EmailAddress(currentUser.getEmailAddress())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agrovet profile not found"));
    }
}
