package org.ecommersebe.ecommersebe.services.impl;

import lombok.RequiredArgsConstructor;
import org.ecommersebe.ecommersebe.models.entities.*;
import org.ecommersebe.ecommersebe.models.enums.OrderStatus;
import org.ecommersebe.ecommersebe.models.enums.PaymentStatus;
import org.ecommersebe.ecommersebe.models.enums.PaymentType;
import org.ecommersebe.ecommersebe.models.exception.ResourceNotFoundException;
import org.ecommersebe.ecommersebe.models.payload.dto.order.*;
import org.ecommersebe.ecommersebe.repositories.OrderDetailRepository;
import org.ecommersebe.ecommersebe.repositories.OrderRepository;
import org.ecommersebe.ecommersebe.repositories.PaymentRepository;
import org.ecommersebe.ecommersebe.repositories.UserRepository;
import org.ecommersebe.ecommersebe.services.EmailService;
import org.ecommersebe.ecommersebe.services.OrderDetailService;
import org.ecommersebe.ecommersebe.services.OrderService;
import org.ecommersebe.ecommersebe.utils.CurrencyUtils;
import org.ecommersebe.ecommersebe.utils.GenericSpecification;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;
    private final UserRepository userRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PaymentRepository paymentRepository;
    private final EmailService emailService;
    private final ModelMapper modelMapper;

    @Override
    public OrderResponse get(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order"));

        return mapToOrderResponse(order);
    }

    @Override
    public OrderResponse update(Long orderId, OrderStatus status, String note) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order"));
        order.setStatus(status);
        order.setNote(note);

        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));

        // Build product rows
        StringBuilder productsHtml = new StringBuilder();
        for (OrderDetail detail : order.getOrderDetails()) {
            productsHtml.append("<tr>")
                    .append("<td style='text-align:center;'>")
                    .append("<img src='").append(detail.getProduct().getImage().split(",")[0])
                    .append("' width='60' height='60' style='border-radius:8px;'/>")
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
                + "th {background:#f9fafb; color:#555; text-transform:uppercase; font-size:12px; letter-spacing:0.5px; text-align:left;}"
                + ".footer {background:#f9fafb; padding:18px; text-align:center; font-size:13px; color:#777; margin-top:30px;}"
                + "</style></head><body>"
                + "<div class='container'>"
                + "<div class='header'><h2>🔔 Cập nhật trạng thái đơn hàng #" + order.getId() + "</h2></div>"
                + "<div style='padding:24px;'>"
                + "<p>Xin chào <b>" + order.getUser().getFullName() + "</b>,</p>"
                + "<p>Đơn hàng của bạn vừa được cập nhật:</p>"
                + "<div class='status-box'>Trạng thái: " + status.name() + "</div>"
                + (note != null && !note.isEmpty() ? "<p><b>Ghi chú:</b> " + note + "</p>" : "")
                + "<h3>Chi tiết đơn hàng</h3>"
                + "<table>"
                + "<tr><th>Mã đơn hàng</th><td>" + order.getId() + "</td></tr>"
                + "<tr><th>Ngày cập nhật</th><td>" + formattedDate + "</td></tr>"
                + "<tr><th>Địa chỉ giao hàng</th><td>" + order.getAddress() + "</td></tr>"
                + "<tr><th>Tổng giá trị</th><td><b>" + CurrencyUtils.formatPrice(order.getTotalAmount()) + "</b></td></tr>"
                + "</table>"
                + "<h3>Danh sách sản phẩm</h3>"
                + "<table>"
                + "<tr><th></th><th style='text-align:center;'>Sản phẩm</th><th style='text-align:center;'>SL</th><th style='text-align:center;'>Đơn giá</th><th style='text-align:center;'>Thành tiền</th></tr>"
                + productsHtml
                + "</table>"
                + "<div class='footer'>Cảm ơn bạn đã mua sắm tại <b>ECommerse</b>.<br/>Chúng tôi sẽ tiếp tục gửi thông báo khi có thay đổi.</div>"
                + "</div></div></body></html>";

        emailService.sendEmail(
                order.getUser().getEmail(),
                "[ECommerse] - Cập nhật trạng thái đơn hàng #" + order.getId(),
                content
        );

        return mapToOrderResponse(orderRepository.save(order));
    }

    @Override
    public OrderResponse add(OrderRequest request) {
        // Create Order
        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        order.setFeedback(false);
        order.setAddress(request.getAddress());
        order.setEmail(request.getEmail());
        order.setFullName(request.getFullName());
        order.setPhone(request.getPhone());
        order.setOrderCode(generateOrderCode());

        // Create OrderDetails
        List<OrderDetail> orderDetails = orderDetailService.create(request.getCartItems(), order);
        float totalAmount = orderDetails.stream()
                .map(OrderDetail::getTotalPrice)
                .reduce(0f, Float::sum);

        order.setTotalAmount(totalAmount);

        // Find customer
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        if(!userName.equals("anonymousUser")) {
            User user = userRepository.findByEmailOrPhone(userName, userName)
                    .orElseThrow(() -> new ResourceNotFoundException("User"));
            order.setUser(user);
        }else {
            order.setUser(null);
        }

        // Save Order and OrderDetails
        Order orderCreated = orderRepository.save(order);
        orderDetailRepository.saveAll(orderDetails);
        order.setOrderDetails(new HashSet<>(orderDetails));

        paymentRepository.save(PaymentHistory.builder()
                .transactionId(order.getOrderCode())
                .amount(order.getTotalAmount())
                .description("Cash on Delivery - Order #" + orderCreated.getId())
                .transactionDateTime(LocalDateTime.now())
                .status(PaymentStatus.UNPAID)
                .type(request.getType())
                .order(order)
                .build());

        if(request.getType() == PaymentType.CASH){
            // Format ngày theo dd-MM-yyyy HH:mm:ss
            String formattedDate = order.getCreatedDate()
                    .format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));

            // Build email content with product list
            StringBuilder productsHtml = new StringBuilder();
            for (OrderDetail detail : orderDetails) {
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
                    + "<p>Xin chào <b>" + request.getFullName() + "</b>,</p>"
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

            // Send email
            emailService.sendEmail(
                    request.getEmail(),
                    "[ECommerse] - Xác nhận đơn hàng #" + order.getId(),
                    content
            );
        }

        return mapToOrderResponse(orderCreated);
    }

    @Override
    public Page<OrderResponse> searchOrders(int pageNo, int pageSize, String sortBy, String sortDir, SearchOrderRequest request, boolean isCustomer) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Specification<Order> specification = Specification.allOf();
        if (request != null) {
            specification = specification.and(specification(toSearchParams(request)));
        }

        if(isCustomer) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            User user = userRepository.findByEmailOrPhone(userName, userName)
                    .orElseThrow(() -> new ResourceNotFoundException("User"));

            specification = specification.and(GenericSpecification.joinFieldEqual("user", "id", user.getId()));
        }

        Page<Order> pageResults = orderRepository.findAll(specification, pageable);

        return pageResults.map(this::mapToOrderResponse);
    }

    @Override
    public OrderInfo getOrderInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userRepository.findByEmailOrPhone(userName, userName)
                .orElseThrow(() -> new ResourceNotFoundException("User"));

        List<Order> result = orderRepository.findByUserIdAndStatus(user.getId(), OrderStatus.COMPLETED);

        int numberOfOrder = result.size();

        float totalPrice = (float) result.stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();

        return new OrderInfo(numberOfOrder, totalPrice);
    }

    private Specification<Order> specification(Map<String, Object> searchParams) {
        List<Specification<Order>> specs = new ArrayList<>();

        searchParams.forEach((key, value) -> {
            switch (key) {
                case "status":
                    specs.add(GenericSpecification.fieldIn(key, (Collection<?>) value));
                    break;
                case "dateFrom":
                    if (searchParams.containsKey("dateTo")) {
                        specs.add(GenericSpecification.fieldBetween("createdDate", (LocalDateTime) searchParams.get("dateFrom"), (LocalDateTime) searchParams.get("dateTo")));
                    } else {
                        specs.add(GenericSpecification.fieldGreaterThan("createdDate", (LocalDateTime) value));
                    }
                    break;
                case "dateTo":
                    if (!searchParams.containsKey("dateFrom")) {
                        specs.add(GenericSpecification.fieldLessThan("createdDate", (LocalDateTime) value));
                    }
                    break;
                case "searchText":
                    String text = (String) value;

                    Specification<Order> searchSpec1 =
                            GenericSpecification.joinFieldContains("user", "fullName", text);
                    Specification<Order> searchSpec2 =
                            GenericSpecification.joinFieldContains("user", "email", text);
                    Specification<Order> searchSpec3 =
                            GenericSpecification.leftJoinFieldContains("orderDetails","product", "name", text);

                    // kết hợp cả hai điều kiện OR
                    Specification<Order> combinedSpec = searchSpec1.or(searchSpec2).or(searchSpec3);

                    specs.add(combinedSpec);
                    break;
            }
        });

        Specification<Order> result = null;
        for (Specification<Order> spec : specs) {
            result = (result == null) ? spec : result.and(spec);
        }

        return result;
    }

    private Map<String, Object> toSearchParams(SearchOrderRequest request) {
        Map<String, Object> params = new HashMap<>();

        if (request.getDateFrom() != null) {
            params.put("dateFrom", request.getDateFrom());
        }

        if (request.getDateTo() != null) {
            params.put("dateTo", request.getDateTo());
        }

        if (request.getStatuses() != null && !request.getStatuses().isEmpty()) {
            params.put("status", request.getStatuses());
        }

        if (request.getSearchText() != null && !request.getSearchText().isBlank()) {
            params.put("searchText", request.getSearchText().trim());
        }

        return params;
    }

    public OrderResponse mapToOrderResponse(Order order){
        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);

        Type listType = new TypeToken<List<OrderDetailResponse>>() {}.getType();
        orderResponse.setOrderDetails(modelMapper.map(order.getOrderDetails(), listType));

        return orderResponse;
    }

    private Long generateOrderCode() {
        String raw = "" + System.currentTimeMillis() ;
        return Long.parseLong(raw.substring(0, Math.min(raw.length(), 18)));
    }
}
