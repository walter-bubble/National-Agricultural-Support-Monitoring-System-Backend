package com.Farm.NASMS.Service;

import com.Farm.NASMS.dto.AdminAgrovetResponse;
import com.Farm.NASMS.dto.AgrovetApplicationRequest;
import com.Farm.NASMS.dto.AgrovetApplicationResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface AgrovetService {
AgrovetApplicationResponse apply(@Valid AgrovetApplicationRequest request);
List<AdminAgrovetResponse> getPendingApplications();
List<AdminAgrovetResponse> getApprovedAgrovets();
AdminAgrovetResponse deactivate(Long id);
AdminAgrovetResponse reactivate(Long id);
AdminAgrovetResponse approve(Long id);
AdminAgrovetResponse reject(Long id);
}
