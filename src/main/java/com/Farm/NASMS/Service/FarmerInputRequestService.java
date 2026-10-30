package com.Farm.NASMS.Service;

import com.Farm.NASMS.dto.FarmerInputRequestCreateDTO;
import com.Farm.NASMS.dto.FarmerInputRequestResponseDTO;
import com.Farm.NASMS.dto.FarmerInputRequestReviewDTO;

import java.util.List;

public interface FarmerInputRequestService {
    Long createRequest(FarmerInputRequestCreateDTO request);
    void reviewRequest(Long requestId, FarmerInputRequestReviewDTO reviewDTO);
    Long forwardRequest(Long requestId);
    List<FarmerInputRequestResponseDTO> getAllRequests();
    FarmerInputRequestResponseDTO getRequestById(Long requestId);
}
