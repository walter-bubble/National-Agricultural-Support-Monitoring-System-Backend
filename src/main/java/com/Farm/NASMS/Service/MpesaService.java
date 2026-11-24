package com.Farm.NASMS.Service;

import com.Farm.NASMS.dto.LoanPaymentResponse;
import com.Farm.NASMS.dto.MpesaCallbackRequest;
import com.Farm.NASMS.dto.MpesaStkRequest;
import com.Farm.NASMS.dto.MpesaStkResponse;

import java.util.List;

public interface MpesaService {
    MpesaStkResponse initiateStkPush(Long loanId, MpesaStkRequest request);
    void processCallback(MpesaCallbackRequest request);
    LoanPaymentResponse getPaymentStatus(String checkoutRequestId);
    List<LoanPaymentResponse> getPaymentsByLoan(Long loanId);
}
