package com.Farm.NASMS.Repository;

import com.Farm.NASMS.enums.AgrovetStatus;
import com.Farm.NASMS.model.Agrovet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgrovetRepository extends JpaRepository<Agrovet,Long> {
    Optional<Agrovet> findById(Long userId);
    boolean existsByBusinessNameIgnoreCase(String businessName);
    List<Agrovet>findByStatus(AgrovetStatus status);
    Optional<Agrovet> findByUser_EmailAddress(String emailAddress);


}
