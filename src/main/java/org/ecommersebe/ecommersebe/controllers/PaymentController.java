package org.ecommersebe.ecommersebe.controllers;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ecommersebe.ecommersebe.models.payload.dto.payment.CreatePaymentRequest;
import org.ecommersebe.ecommersebe.models.payload.dto.payment.WebhookUrlDto;
import org.ecommersebe.ecommersebe.services.PayOSService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.payos.type.CheckoutResponseData;



@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PayOSService payOSService;

    @ApiResponse(responseCode = "201", description = "Http Status 201 Created")
    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/link")
    public ResponseEntity<CheckoutResponseData> createPaymentLink(@RequestBody @Valid CreatePaymentRequest request) {
        return ResponseEntity.ok(payOSService.createEmbeddedPaymentLink(request));
    }

    @PostMapping(path = "/confirm-webhook")
    public ResponseEntity<String> confirmWebhook(@RequestBody @Valid WebhookUrlDto webhookUrlDto) {
        return ResponseEntity.ok(payOSService.confirmWebhook(webhookUrlDto));
    }

    @PostMapping("/payos-transfer-handler")
    public ResponseEntity<String> payosTransferHandler(@RequestBody String rawBody) {
        try {
            payOSService.payOsTransferHandler(rawBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("OK");
    }
}
