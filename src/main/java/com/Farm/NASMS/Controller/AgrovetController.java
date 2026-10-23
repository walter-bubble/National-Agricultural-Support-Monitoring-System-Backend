package com.Farm.NASMS.Controller;

import com.Farm.NASMS.Service.AgrovetService;
import com.Farm.NASMS.dto.AdminAgrovetResponse;
import com.Farm.NASMS.dto.AgrovetApplicationRequest;
import com.Farm.NASMS.dto.AgrovetApplicationResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/agrovets")
public class AgrovetController {
    private final AgrovetService agrovetService;
    public AgrovetController(AgrovetService agrovetService){
        this.agrovetService = agrovetService;
    }
    @PostMapping("/apply")
    public ResponseEntity<AgrovetApplicationResponse> apply(@Valid @RequestBody AgrovetApplicationRequest request){
        AgrovetApplicationResponse response = agrovetService.apply(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/admin/approval")
    public ResponseEntity<List<AdminAgrovetResponse>> getPendingApplications(){
        return ResponseEntity.ok(agrovetService.getPendingApplications());
    }
    @GetMapping("/admin/approved")
    public ResponseEntity<List<AdminAgrovetResponse>> getApprovedAgrovets() {
        return ResponseEntity.ok(agrovetService.getApprovedAgrovets());
    }
    @PutMapping("/admin/{id}/approve")
    public ResponseEntity<AdminAgrovetResponse> approve(@PathVariable Long id){
        return ResponseEntity.ok(agrovetService.approve(id));
    }
    @PutMapping("/admin/{id}/reject")
    public ResponseEntity<AdminAgrovetResponse> reject (@PathVariable Long id){
        return ResponseEntity.ok(agrovetService.reject(id));
    }
    @PutMapping("/admin/{id}/deactivate")
    public ResponseEntity<AdminAgrovetResponse> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(agrovetService.deactivate(id));
    }
    @PutMapping("/admin/{id}/reactivate")
    public ResponseEntity<AdminAgrovetResponse> reactivate(@PathVariable Long id) {
        return ResponseEntity.ok(agrovetService.reactivate(id));
    }
}
