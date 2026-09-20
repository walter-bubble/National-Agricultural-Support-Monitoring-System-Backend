package com.Farm.NASMS.Service;

import com.Farm.NASMS.Config.MpesaProperties;
import com.Farm.NASMS.Repository.LoanPaymentRepository;
import com.Farm.NASMS.Repository.LoanRepository;
import com.Farm.NASMS.dto.MpesaCallbackRequest;
import com.Farm.NASMS.dto.MpesaStkRequest;
import com.Farm.NASMS.dto.MpesaStkResponse;
import com.Farm.NASMS.enums.LoanStatus;
import com.Farm.NASMS.enums.PaymentMethod;
import com.Farm.NASMS.enums.PaymentStatus;
import com.Farm.NASMS.model.Loan;
import com.Farm.NASMS.model.LoanPayment;
import com.Farm.NASMS.security.AuthenticatedUserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class MpesaServiceImpl implements MpesaService{
    private final LoanRepository loanRepository;
    private final AuthenticatedUserService authenticatedUserService;
    private final MpesaProperties mpesaProperties;
    private final RestClient restClient;
    private final LoanPaymentRepository loanPaymentRepository;

    public MpesaServiceImpl(LoanRepository loanRepository, AuthenticatedUserService authenticatedUserService, MpesaProperties mpesaProperties, RestClient.Builder restClientBuilder, LoanPaymentRepository loanPaymentRepository) {
        this.loanRepository = loanRepository;
        this.authenticatedUserService = authenticatedUserService;
        this.mpesaProperties = mpesaProperties;
        this.restClient = restClientBuilder.build();
        this.loanPaymentRepository = loanPaymentRepository;
    }

    @Override
    public MpesaStkResponse initiateStkPush(Long loanId, MpesaStkRequest request) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Loan does not exist"));
        if(!authenticatedUserService.isLoanOwner(loan)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access denied");
        }
        BigDecimal amount = request.getAmount();
        if(amount == null || amount.compareTo(BigDecimal.ZERO)<=0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Amount must greater than zero");
        }
        if(amount.compareTo(loan.getRemainingBalance()) > 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Amount exceeds the remaining loan balance");
        }
        String phoneNumber = formatPhoneNumber(request.getPhoneNumber());
        String accessToken = getAccessToken();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String password = generatePassword(timestamp);
        Map<String, Object> stkRequest = new HashMap<>();
        stkRequest.put("BusinessShortCode", mpesaProperties.getShortcode());
        stkRequest.put("Password", password);
        stkRequest.put("Timestamp", timestamp);
        stkRequest.put("TransactionType", "CustomerPayBillOnline");
        stkRequest.put("Amount", amount.intValue());
        stkRequest.put("PartyA", phoneNumber);
        stkRequest.put("PartyB", mpesaProperties.getShortcode());
        stkRequest.put("PhoneNumber", phoneNumber);
        stkRequest.put("CallBackURL", mpesaProperties.getCallbackUrl());
        stkRequest.put("AccountReference", "LOAN-" + loan.getId());
        stkRequest.put("TransactionDesc", "NASMS Loan Payment");
        Map response = restClient.post()
                .uri(mpesaProperties.getStkUrl())
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                )
                .contentType(MediaType.APPLICATION_JSON)
                .body(stkRequest)
                .retrieve()
                .body(Map.class);

        if (response == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "No response received from M-Pesa"
            );
        }

        String merchantRequestId =
                String.valueOf(
                        response.get("MerchantRequestID")
                );

        String checkoutRequestId =
                String.valueOf(
                        response.get("CheckoutRequestID")
                );
        if (checkoutRequestId == null ||
                checkoutRequestId.isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Checkout request ID is missing"
            );
        }

        String responseCode =
                String.valueOf(
                        response.get("ResponseCode")
                );

        String responseDescription =
                String.valueOf(
                        response.get("ResponseDescription")
                );

        String customerMessage =
                String.valueOf(
                        response.get("CustomerMessage")
                );
        if ("0".equals(responseCode)) {

            LoanPayment payment = new LoanPayment();

            payment.setLoan(loan);
            payment.setAmountToPay(amount);

            payment.setStatus(PaymentStatus.PENDING);

            payment.setMerchantRequestId(merchantRequestId);

            payment.setCheckoutRequestId(checkoutRequestId);

            payment.setPaymentMethod(PaymentMethod.M_PESA);

            loanPaymentRepository.save(payment);
        }
        MpesaStkResponse stkResponse =
                new MpesaStkResponse();

        stkResponse.setMerchantRequestId(
                merchantRequestId
        );

        stkResponse.setCheckoutRequestId(
                checkoutRequestId
        );

        stkResponse.setResponseCode(
                responseCode
        );

        stkResponse.setResponseDescription(
                responseDescription
        );

        stkResponse.setCustomerMessage(
                customerMessage
        );

        return stkResponse;
    }

    private String getAccessToken() {

        String credentials =
                mpesaProperties.getConsumerKey()
                        + ":"
                        + mpesaProperties.getConsumerSecretKey();

        String encodedCredentials =
                Base64.getEncoder()
                        .encodeToString(
                                credentials.getBytes(
                                        StandardCharsets.UTF_8
                                )
                        );

        Map response = restClient.get()
                .uri(mpesaProperties.getOauthUrl())
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Basic " + encodedCredentials
                )
                .retrieve()
                .body(Map.class);

        if (response == null ||
                response.get("access_token") == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Unable to obtain M-Pesa access token"
            );
        }

        return String.valueOf(
                response.get("access_token")
        );
    }

    private String generatePassword(String timestamp) {

        String data =
                mpesaProperties.getShortcode()
                        + mpesaProperties.getPassKey()
                        + timestamp;

        return Base64.getEncoder()
                .encodeToString(
                        data.getBytes(StandardCharsets.UTF_8)
                );
    }

    private String formatPhoneNumber(String phoneNumber) {

        if (phoneNumber == null ||
                phoneNumber.isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Phone number is required"
            );
        }
        phoneNumber = phoneNumber.trim();

        if (phoneNumber.startsWith("07") ||
                phoneNumber.startsWith("01")) {

            return "254" + phoneNumber.substring(1);
        }

        if (phoneNumber.startsWith("+254")) {
            return phoneNumber.substring(1);
        }

        if (phoneNumber.startsWith("254")) {
            return phoneNumber;
        }

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid Kenyan phone number"
        );
    }
    @Override
    @Transactional
    public void processCallback(MpesaCallbackRequest request) {
        if (request == null ||
                request.getBody() == null ||
                request.getBody().getStkCallback() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid M-Pesa callback"
            );
        }

        MpesaCallbackRequest.StkCallback callback =
                request.getBody().getStkCallback();

        String checkoutRequestId =
                callback.getCheckoutRequestId();

        LoanPayment payment =
                loanPaymentRepository
                        .findByCheckoutRequestId(checkoutRequestId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Payment does not exist"
                                )
                        );

        // Prevent duplicate callbacks from processing the payment twice
        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            return;
        }

        // Payment failed or was cancelled
        if (callback.getResultCode() != 0) {

            payment.setStatus(PaymentStatus.FAILED);

            loanPaymentRepository.save(payment);

            return;
        }

        // Payment was successful
        BigDecimal paymentAmount =
                payment.getAmountToPay();

        Loan loan =
                payment.getLoan();

        BigDecimal previousBalance =
                loan.getRemainingBalance();

        BigDecimal previousTotalPaid =
                loan.getTotalPayment()
                        .subtract(previousBalance);

        BigDecimal newBalance =
                previousBalance.subtract(paymentAmount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Payment exceeds remaining loan balance"
            );
        }

        BigDecimal totalAmountPaid =
                previousTotalPaid.add(paymentAmount);

        String receiptNumber =
                getMetadataValue(
                        callback,
                        "MpesaReceiptNumber"
                );

        // Update payment
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setMpesaReceiptNumber(receiptNumber);
        payment.setTransactionCode(receiptNumber);
        payment.setPreviousTotal(previousTotalPaid);
        payment.setTotalAmountPaid(totalAmountPaid);
        payment.setRemainingBalance(newBalance);

        // Update loan
        loan.setRemainingBalance(newBalance);

        if (newBalance.compareTo(BigDecimal.ZERO) == 0) {
            loan.setStatus(LoanStatus.COMPLETED);
        }

        loanRepository.save(loan);
        loanPaymentRepository.save(payment);
    }
    private String getMetadataValue(
            MpesaCallbackRequest.StkCallback callback,
            String name) {

        if (callback.getCallbackMetadata() == null ||
                callback.getCallbackMetadata().getItems() == null) {

            return null;
        }

        for (MpesaCallbackRequest.CallbackItem item :
                callback.getCallbackMetadata().getItems()) {

            if (name.equals(item.getName())) {

                return String.valueOf(item.getValue());
            }
        }

        return null;
    }
}

