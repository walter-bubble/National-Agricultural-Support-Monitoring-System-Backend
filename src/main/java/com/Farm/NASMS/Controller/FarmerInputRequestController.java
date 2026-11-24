package com.Farm.NASMS.Controller;

import com.Farm.NASMS.Service.FarmerInputRequestService;
import com.Farm.NASMS.dto.FarmerInputRequestCreateDTO;
import com.Farm.NASMS.dto.FarmerInputRequestReviewDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/farmer/input-requests")
public class FarmerInputRequestController {
    private final FarmerInputRequestService requestService;

    public FarmerInputRequestController(FarmerInputRequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createRequest(@Valid @RequestBody FarmerInputRequestCreateDTO requestDTO) {
        Long requestId = requestService.createRequest(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "message", "Input request submitted successfully",
                        "requestId", requestId,
                        "status", "SUBMITTED"));
    }
    @PutMapping("/{requestId}/review")
    public ResponseEntity<Map<String, String>> reviewRequest(@PathVariable Long requestId, @RequestBody FarmerInputRequestReviewDTO reviewDTO) {
        requestService.reviewRequest(requestId, reviewDTO);
        String status = reviewDTO.approved()
                ? "APPROVED"
                : "REJECTED";
        return ResponseEntity.ok(Map.of(
                "message", "Input request reviewed successfully",
                "requestId", requestId.toString(),
                "status", status
        ));
    }
    @PostMapping("/{requestId}/forward")
    public ResponseEntity<Map<String, Object>> forwardRequest(
            @PathVariable Long requestId) {

        Long forwardedRequestId = requestService.forwardRequest(requestId);

        return ResponseEntity.ok(Map.of(
                "message", "Input request forwarded to Agrovet(s) successfully",
                "requestId", forwardedRequestId,
                "status", "FORWARDED"
        ));
    }
    @GetMapping
    public ResponseEntity<?> getAllRequests() {
        return ResponseEntity.ok(requestService.getAllRequests());
    }
    @GetMapping("/{requestId}")
    public ResponseEntity<?> getRequestById(@PathVariable Long requestId) {
        return ResponseEntity.ok(requestService.getRequestById(requestId));
    }
    @GetMapping("/mine")
    public ResponseEntity<?> getMyRequests() {
        return ResponseEntity.ok(requestService.getMyRequests());
    }
}
