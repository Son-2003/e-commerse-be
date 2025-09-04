package org.ecommersebe.ecommersebe.services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ecommersebe.ecommersebe.models.payload.dto.payment.CreatePaymentRequest;
import org.ecommersebe.ecommersebe.models.payload.dto.payment.WebhookUrlDto;
import vn.payos.type.CheckoutResponseData;

public interface PayOSService {
    CheckoutResponseData createEmbeddedPaymentLink(CreatePaymentRequest request);
    String confirmWebhook(WebhookUrlDto webhookUrlDto);
    void payOsTransferHandler(ObjectNode body) throws Exception;
}
