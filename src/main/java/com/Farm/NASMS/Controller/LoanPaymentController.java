package com.Farm.NASMS.Controller;

import com.Farm.NASMS.Service.LoanPaymentService;
import com.Farm.NASMS.Service.MpesaService;
import com.Farm.NASMS.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loan-payments")
public class LoanPaymentController {
    private final LoanPaymentService loanPaymentService;
    private final MpesaService mpesaService;
    public LoanPaymentController(LoanPaymentService loanPaymentService, MpesaService mpesaService){
        this.loanPaymentService=loanPaymentService;
        this.mpesaService = mpesaService;
    }
    @PostMapping("/{id}")
    public ResponseEntity<LoanPaymentResponse> payLoan(@PathVariable Long id, @RequestBody LoanPaymentRequest request){
        LoanPaymentResponse loanPayment = loanPaymentService.makePayment(id,request);
        return ResponseEntity.ok(loanPayment);
    }
    @PostMapping("/mpesa/{loanId}")
    public ResponseEntity<MpesaStkResponse> initiateMpesaPayment(
            @PathVariable Long loanId,
            @RequestBody MpesaStkRequest request) {

        MpesaStkResponse response =
                mpesaService.initiateStkPush(
                        loanId,
                        request
                );

        return ResponseEntity.ok(response);
    }
    @PostMapping("/mpesa/callback")
    public ResponseEntity<Void> mpesaCallback(
            @RequestBody MpesaCallbackRequest request) {

        mpesaService.processCallback(request);

        return ResponseEntity.ok().build();
    }
}
