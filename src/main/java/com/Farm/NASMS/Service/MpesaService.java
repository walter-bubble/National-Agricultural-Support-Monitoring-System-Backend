package com.Farm.NASMS.Service;

import com.Farm.NASMS.dto.MpesaCallbackRequest;
import com.Farm.NASMS.dto.MpesaStkRequest;
import com.Farm.NASMS.dto.MpesaStkResponse;

public interface MpesaService {
    MpesaStkResponse initiateStkPush(Long loanId, MpesaStkRequest request);
    void processCallback(MpesaCallbackRequest request);
}
