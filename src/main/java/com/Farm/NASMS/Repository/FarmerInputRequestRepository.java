package com.Farm.NASMS.Repository;

import com.Farm.NASMS.enums.FarmerInputRequestStatus;
import com.Farm.NASMS.model.FarmerInputRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FarmerInputRequestRepository extends JpaRepository<FarmerInputRequest, Long> {
    List<FarmerInputRequest> findByStatus(FarmerInputRequestStatus status);
    List<FarmerInputRequest> findAllByOrderByCreatedAtDesc();
    Optional<FarmerInputRequest> findById(Long id);
}
