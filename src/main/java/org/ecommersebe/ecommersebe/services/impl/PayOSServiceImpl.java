package org.ecommersebe.ecommersebe.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.ecommersebe.ecommersebe.config.PayOSConfig;
import org.ecommersebe.ecommersebe.models.entities.Order;
import org.ecommersebe.ecommersebe.models.entities.OrderDetail;
import org.ecommersebe.ecommersebe.models.entities.PaymentHistory;
import org.ecommersebe.ecommersebe.models.entities.Product;
import org.ecommersebe.ecommersebe.models.enums.PaymentStatus;
import org.ecommersebe.ecommersebe.models.exception.PayOSException;
import org.ecommersebe.ecommersebe.models.exception.ResourceNotFoundException;
import org.ecommersebe.ecommersebe.models.payload.dto.order.CartItem;
import org.ecommersebe.ecommersebe.models.payload.dto.payment.CreatePaymentRequest;
import org.ecommersebe.ecommersebe.models.payload.dto.payment.WebhookUrlDto;
import org.ecommersebe.ecommersebe.repositories.PaymentRepository;
import org.ecommersebe.ecommersebe.repositories.ProductRepository;
import org.ecommersebe.ecommersebe.services.EmailService;
import org.ecommersebe.ecommersebe.services.PayOSService;
import org.ecommersebe.ecommersebe.utils.CurrencyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.webhooks.Webhook;
import vn.payos.model.webhooks.WebhookData;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PayOSServiceImpl implements PayOSService {
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final EmailService emailService;
    private final PayOS payOS;

    @Override
    public CreatePaymentLinkResponse createEmbeddedPaymentLink(CreatePaymentRequest request) {
        int totalItemsAmount = 0;
        for (CartItem cartItem : request.getCartItems()) {
            Product product = productRepository.findById(cartItem.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", cartItem.getId()));

            int unitPrice = Math.round(product.getPrice() * 1000);
            totalItemsAmount += (unitPrice * cartItem.getQuantity());
        }

        int shippingFee = 10000;
        long finalTotalAmount = totalItemsAmount + shippingFee;

        CreatePaymentLinkRequest paymentData = CreatePaymentLinkRequest.builder()
                .orderCode(request.getOrderCode())
                .amount(finalTotalAmount)
                .description("Thanh toan don hang")
                .returnUrl(request.getReturnUrl())
                .cancelUrl(request.getCancelUrl())
                .build();

        try {
            return payOS.paymentRequests().create(paymentData);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo payment link: " + e.getMessage(), e);
        }
    }

    @Override
    public String confirmWebhook(WebhookUrlDto webhookUrlDto) {
        try {
            payOS.webhooks().confirm(webhookUrlDto.getWebhookUrl());
            return "Webhook confirmed successfully";
        } catch (Exception e) {
            throw new PayOSException(e.getMessage());
        }
    }

    @Override
    public void payOsTransferHandler(ObjectNode body) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Webhook webhookBody = objectMapper.treeToValue(body, Webhook.class);
            if (webhookBody.getSuccess() == null) {
                webhookBody.setSuccess(true);
            }

            WebhookData data = payOS.webhooks().verify(webhookBody);

            PaymentHistory paymentHistory = paymentRepository
                    .findByTransactionId(data.getOrderCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Payment"));
            Order order = paymentHistory.getOrder();
            if (data.getCode().equals("00") && webhookBody.getSuccess()) {
                paymentHistory.setStatus(PaymentStatus.PAID);
                paymentHistory.setDescription("PayOS Bank Payment - Order #" + order.getId());
            } else {
                paymentHistory.setStatus(PaymentStatus.UNPAID);
            }
            paymentRepository.save(paymentHistory);

            String formattedDate = order.getCreatedDate()
                    .format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));

            // Email builder (Giữ nguyên logic cực kỳ hoàn chỉnh của bạn)
            StringBuilder productsHtml = new StringBuilder();
            for (OrderDetail detail : order.getOrderDetails()) {
                productsHtml.append("<tr>")
                        .append("<td style='text-align:center;'>")
                        .append("<img src=\"").append(detail.getProduct().getImage().split(",")[0])
                        .append("\" width='60' height='60' style='border-radius:8px; display:block;'/>")
                        .append("</td>")
                        .append("<td style='text-align:center;'>").append(detail.getProduct().getName()).append("</td>")
                        .append("<td style='text-align:center;'>").append(detail.getQuantity()).append("</td>")
                        .append("<td style='text-align:center;'>").append(CurrencyUtils.formatPrice(detail.getProduct().getPrice())).append("</td>")
                        .append("<td style='text-align:center;'>").append(CurrencyUtils.formatPrice(detail.getTotalPrice())).append("</td>")
                        .append("</tr>");
            }

            String content = "<html><head><style>"
                    + "body {font-family:'Segoe UI',Roboto,Arial,sans-serif; background:#f4f6f9; margin:0; padding:20px;}"
                    + ".container {max-width:720px; margin:auto; background:#fff; border-radius:14px; overflow:hidden; box-shadow:0 4px 20px rgba(0,0,0,0.08);}"
                    + ".header {background: linear-gradient(135deg, #2c2c2c, #000000); padding:24px; color:#fff; text-align:center;}"
                    + ".header h2 {margin:0; font-size:22px;}"
                    + ".status-box {margin:20px 0; padding:15px; background: #f0f0f0; border-left:5px solid #000000; border-radius:8px; font-size:15px; color:#222; font-weight:bold;}"
                    + "table {width:100%; border-collapse:collapse; margin-top:20px;}"
                    + "th,td {padding:12px; border-bottom:1px solid #eee; font-size:14px;}"
                    + "th {background:#f9fafb; color:#555; text-transform:uppercase; font-size:12px; letter-spacing:0.5px; text-align: left;}"
                    + ".footer {background:#f9fafb; padding:18px; text-align:center; font-size:13px; color:#777; margin-top:30px;}"
                    + ".product-img {\n" +
                    "      border-radius: 8px;\n" +
                    "      display: block;\n" +
                    "    }"
                    + ".text-center { text-align: center; }"
                    + ".text-right { text-align: right; }"
                    + "</style></head><body>"
                    + "<div class='container'>"
                    + "<div class='header'><h2>🎉 Đơn hàng #" + order.getId() + " đã được xác nhận</h2></div>"
                    + "<div style='padding:24px;'>"
                    + "<p>Xin chào <b>" + order.getFullName() + "</b>,</p>"
                    + "<p>Cảm ơn bạn đã đặt hàng tại <b>ECommerse</b>! Dưới đây là thông tin chi tiết:</p>"
                    + "<div class='status-box'>Trạng thái: " + order.getStatus() + "</div>"
                    + "<table>"
                    + "<tr><th>Mã đơn hàng</th><td>" + order.getId() + "</td></tr>"
                    + "<tr><th>Ngày đặt</th><td>" + formattedDate + "</td></tr>"
                    + "<tr><th>Địa chỉ giao hàng</th><td>" + order.getAddress().split("//")[0] + ", " + order.getAddress().split("//")[1] + "</td></tr>"
                    + "<tr><th>Tổng giá trị</th><td><b>" + CurrencyUtils.formatPrice(order.getTotalAmount()) + "</b></td></tr>"
                    + "</table>"
                    + "<h3>Danh sách sản phẩm</h3>"
                    + "<table>"
                    + "<tr><th></th><th style='text-align:center;'>Sản phẩm</th><th style='text-align:center;'>SL</th><th style='text-align:center;'>Đơn giá</th><th style='text-align:center;'>Thành tiền</th></tr>"
                    + productsHtml
                    + "</table>"
                    + "<div class='footer'>Cảm ơn bạn đã mua sắm tại <b>ECommerse</b>.<br/>Chúng tôi sẽ sớm liên hệ về giao hàng.</div>"
                    + "</div></div></body></html>";

            emailService.sendEmail(
                    order.getEmail(),
                    "[ECommerse] - Xác nhận đơn hàng #" + order.getId(),
                    content
            );

        } catch (Exception e) {
            throw new PayOSException("Error handling PayOS webhook: " + e.getMessage());
        }
    }
}