package com.Farm.NASMS.Service;

import com.Farm.NASMS.Repository.AgrovetRepository;
import com.Farm.NASMS.Repository.UserRepository;
import com.Farm.NASMS.dto.AdminAgrovetResponse;
import com.Farm.NASMS.dto.AgrovetApplicationRequest;
import com.Farm.NASMS.dto.AgrovetApplicationResponse;
import com.Farm.NASMS.enums.AgrovetOperationalStatus;
import com.Farm.NASMS.enums.AgrovetStatus;
import com.Farm.NASMS.model.Agrovet;
import com.Farm.NASMS.model.User;
import com.walter.toolkit.security_starter.audit.Auditable;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class AgrovetServiceImpl implements AgrovetService{
    private final AgrovetRepository agrovetRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public AgrovetServiceImpl(AgrovetRepository agrovetRepository, UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.agrovetRepository=agrovetRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    @Transactional
    public AgrovetApplicationResponse apply(AgrovetApplicationRequest request) {
        if(userRepository.findByEmailAddress(request.getEmailAddress()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"EmailAddress is already registered!");
        }
        if(userRepository.findByUserName(request.getUserName()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"UserName exists!");
        }
        if(agrovetRepository.existsByBusinessNameIgnoreCase(request.getBusinessName().trim())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Business name already exists!");
        }
        User user  = new User();
        user.setUserName(request.getUserName().trim());
        user.setEmailAddress(request.getEmailAddress().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("AGROVET");
        user.setEnabled(false);
        User savedUser = userRepository.save(user);
        Agrovet agrovet = new Agrovet();
        agrovet.setUser(savedUser);
        agrovet.setBusinessName(request.getBusinessName());
        agrovet.setLocation(request.getLocation());
        agrovet.setAccountNumber(request.getAccountNumber().trim());
        agrovet.setStatus(AgrovetStatus.PENDING_APPROVAL);
        agrovet.setProductsOffered(request.getProductsOffered());
        Agrovet savedAgrovet = agrovetRepository.save(agrovet);
        return toApplicationResponse(savedAgrovet);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<AdminAgrovetResponse> getPendingApplications() {
        return agrovetRepository
                .findByStatus(AgrovetStatus.PENDING_APPROVAL)
                .stream()
                .map(this::toAdminResponse)
                .toList();
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<AdminAgrovetResponse> getApprovedAgrovets() {
        return agrovetRepository
                .findByStatus(AgrovetStatus.APPROVED)
                .stream()
                .map(this::toAdminResponse)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "AGROVET_APPROVED")
    public AdminAgrovetResponse approve(Long id) {
        Agrovet agrovet = getPendingAgrovet(id);
        agrovet.setStatus(AgrovetStatus.APPROVED);
        agrovet.setOperationalStatus(AgrovetOperationalStatus.ACTIVE);
        agrovet.setReviewedAt(LocalDateTime.now());
        if (agrovet.getUser() != null) {
            agrovet.getUser().setEnabled(true);
        }
        Agrovet savedAgrovet = agrovetRepository.save(agrovet);
        return toAdminResponse(savedAgrovet);
    }
    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "AGROVET_DEACTIVATED")
    public AdminAgrovetResponse deactivate(Long id) {
        Agrovet agrovet = agrovetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agrovet not found"));
        if (agrovet.getStatus() != AgrovetStatus.APPROVED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only approved agrovets can be deactivated");
        }

        if (agrovet.getOperationalStatus() == AgrovetOperationalStatus.INACTIVE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Agrovet is already inactive");
        }
        agrovet.setOperationalStatus(AgrovetOperationalStatus.INACTIVE);
        if(agrovet.getUser() !=null){
            agrovet.getUser().setEnabled(false);
        }
        Agrovet savedAgrovet = agrovetRepository.save(agrovet);
        return toAdminResponse(savedAgrovet);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "AGROVET_REACTIVATED")
    public AdminAgrovetResponse reactivate(Long id) {
        Agrovet agrovet = agrovetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agrovet not found"));
        if (agrovet.getStatus() != AgrovetStatus.APPROVED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only approved agrovets can be reactivated");
        }
        if (agrovet.getOperationalStatus() == AgrovetOperationalStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Agrovet is already active");
        }
        agrovet.setOperationalStatus(AgrovetOperationalStatus.ACTIVE);
        if (agrovet.getUser() != null) {
            agrovet.getUser().setEnabled(true);
        }
        Agrovet savedAgrovet = agrovetRepository.save(agrovet);
        return toAdminResponse(savedAgrovet);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action="AGROVET_REJECTED")
    public AdminAgrovetResponse reject(Long id) {
        Agrovet agrovet =getPendingAgrovet(id);
        agrovet.setStatus(AgrovetStatus.REJECTED);
        agrovet.setReviewedAt(LocalDateTime.now());
        if (agrovet.getUser() != null) {
            agrovet.getUser().setEnabled(false);
        }
        Agrovet savedAgrovet = agrovetRepository.save(agrovet);
        return toAdminResponse(savedAgrovet);
    }
    private Agrovet getPendingAgrovet(Long agrovetId) {
        Agrovet agrovet = agrovetRepository.findById(agrovetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agrovet application not found"));
        if (agrovet.getStatus() != AgrovetStatus.PENDING_APPROVAL) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This application has already been reviewed");
        }
        return agrovet;
    }
    private AgrovetApplicationResponse toApplicationResponse(Agrovet agrovet) {
        AgrovetApplicationResponse response = new AgrovetApplicationResponse();
        response.setId(agrovet.getId());
        response.setUserId(agrovet.getUser().getId());
        response.setUserName(agrovet.getUser().getUserName());
        response.setEmailAddress(agrovet.getUser().getEmailAddress());
        response.setBusinessName(agrovet.getBusinessName());
        response.setLocation(agrovet.getLocation());
        response.setStatus(agrovet.getStatus());
        response.setProductsOffered(agrovet.getProductsOffered());
        response.setCreatedAt(agrovet.getCreatedAt());
        return response;
    }
    private AdminAgrovetResponse toAdminResponse(Agrovet agrovet) {
        AdminAgrovetResponse response = new AdminAgrovetResponse();
        response.setId(agrovet.getId());
        response.setUserId(agrovet.getUser().getId());
        response.setUserName(agrovet.getUser().getUserName());
        response.setEmailAddress(agrovet.getUser().getEmailAddress());
        response.setBusinessName(agrovet.getBusinessName());
        response.setLocation(agrovet.getLocation());
        response.setAccountNumber(agrovet.getAccountNumber());
        response.setProductsOffered(agrovet.getProductsOffered());
        response.setStatus(agrovet.getStatus());
        response.setOperationalStatus(agrovet.getOperationalStatus());
        response.setCreatedAt(agrovet.getCreatedAt());
        response.setReviewedAt(agrovet.getReviewedAt());
        return response;
    }
}
