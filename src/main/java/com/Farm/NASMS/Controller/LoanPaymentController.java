package com.Farm.NASMS.Controller;

import com.Farm.NASMS.Service.MpesaService;
import com.Farm.NASMS.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

@RestController
@RequestMapping("/api/loan-payments")
public class LoanPaymentController {
    private final MpesaService mpesaService;
    @Value("${mpesa.callback.secret}")
    private String callbackSecret;

    @Value("${mpesa.callback.enforce-ip:true}")
    private boolean enforceSafaricomIp;

    // Official Safaricom Daraja outbound callback IPs
    @Value("${mpesa.callback.allowed-ips:196.201.214.200,196.201.214.206,196.201.213.114,196.201.214.207,196.201.214.208,196.201.213.44,196.201.212.127,196.201.212.138,196.201.212.129,196.201.212.136,196.201.212.74,196.201.212.69}")
    private List<String> allowedSafaricomIps;

    public LoanPaymentController(MpesaService mpesaService){
        this.mpesaService = mpesaService;
    }
    @PostMapping("/mpesa/{loanId}")
    public ResponseEntity<MpesaStkResponse> initiateMpesaPayment(@PathVariable Long loanId, @Valid @RequestBody MpesaStkRequest request) {
        MpesaStkResponse response = mpesaService.initiateStkPush(loanId, request);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/mpesa/status/{checkoutRequestId}")
    public ResponseEntity<LoanPaymentResponse> getMpesaPaymentStatus(@PathVariable String checkoutRequestId) {
        return ResponseEntity.ok(mpesaService.getPaymentStatus(checkoutRequestId));
    }
    @GetMapping("/loan/{loanId}")
    public ResponseEntity<List<LoanPaymentResponse>> getPaymentsForLoan(@PathVariable Long loanId) {
        return ResponseEntity.ok(mpesaService.getPaymentsByLoan(loanId));
    }
    @PostMapping("/mpesa/callback/{secret}")
    public ResponseEntity<Void> mpesaCallback(@PathVariable String secret, @RequestBody MpesaCallbackRequest request, HttpServletRequest httpRequest) {
        if (!isValidSecret(secret)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid callback token");
        }
        if (enforceSafaricomIp && !isSafaricomIp(httpRequest)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized callback source IP");
        }
        mpesaService.processCallback(request);
        return ResponseEntity.ok().build();
    }
    private boolean isValidSecret(String providedSecret) {
        if (providedSecret == null || callbackSecret == null) {
            return false;
        }
        return MessageDigest.isEqual(
                providedSecret.getBytes(StandardCharsets.UTF_8),
                callbackSecret.getBytes(StandardCharsets.UTF_8)
        );
    }
    private boolean isSafaricomIp(HttpServletRequest request) {
        String remoteIp = request.getRemoteAddr();
        return allowedSafaricomIps.contains(remoteIp);
    }
}
