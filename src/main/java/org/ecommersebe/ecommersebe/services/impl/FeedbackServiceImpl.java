package org.ecommersebe.ecommersebe.services.impl;

import lombok.RequiredArgsConstructor;
import org.ecommersebe.ecommersebe.models.entities.Feedback;
import org.ecommersebe.ecommersebe.models.entities.Order;
import org.ecommersebe.ecommersebe.models.entities.Product;
import org.ecommersebe.ecommersebe.models.entities.User;
import org.ecommersebe.ecommersebe.models.enums.EntityStatus;
import org.ecommersebe.ecommersebe.models.enums.OrderStatus;
import org.ecommersebe.ecommersebe.models.exception.ECommerseException;
import org.ecommersebe.ecommersebe.models.exception.ResourceNotFoundException;
import org.ecommersebe.ecommersebe.models.payload.dto.feedback.FeedbackRequest;
import org.ecommersebe.ecommersebe.models.payload.dto.feedback.FeedbackResponse;
import org.ecommersebe.ecommersebe.models.payload.dto.feedback.FeedbackSummaryResponse;
import org.ecommersebe.ecommersebe.models.payload.dto.feedback.SearchFeedbackRequest;
import org.ecommersebe.ecommersebe.repositories.FeedbackRepository;
import org.ecommersebe.ecommersebe.repositories.OrderRepository;
import org.ecommersebe.ecommersebe.repositories.ProductRepository;
import org.ecommersebe.ecommersebe.repositories.UserRepository;
import org.ecommersebe.ecommersebe.services.FeedbackService;
import org.ecommersebe.ecommersebe.utils.GenericSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final FeedbackRepository feedbackRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<FeedbackResponse> create(List<FeedbackRequest> request, Long orderId) {
        List<Feedback> feedbacks = new ArrayList<>();

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmailOrPhone(userName, userName)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        if(!order.getStatus().equals(OrderStatus.COMPLETED) || order.isFeedback()){
            throw new ECommerseException(HttpStatus.BAD_REQUEST,  "You are not allowed to rate this order!");
        }else {
            order.setFeedback(true);
            orderRepository.save(order);

            for(FeedbackRequest feedbackRequest : request) {
                Product product = productRepository.findById(feedbackRequest.getProductId()).orElseThrow(
                        () -> new ResourceNotFoundException("Product", "id", feedbackRequest.getProductId())
                );
                Feedback feedbackCreate = new Feedback();
                feedbackCreate.setComment(feedbackRequest.getComment());
                feedbackCreate.setRating(feedbackRequest.getRating());
                feedbackCreate.setImage(feedbackRequest.getImage());
                feedbackCreate.setUser(user);
                feedbackCreate.setOrder(order);
                feedbackCreate.setProduct(product);

                feedbacks.add(feedbackCreate);
            }

            feedbackRepository.saveAll(feedbacks);
        }
        return feedbacks.stream().map(feedback -> modelMapper.map(feedback, FeedbackResponse.class)).toList();
    }

    @Override
    public Page<FeedbackResponse> getAll(int pageNo, int pageSize, String sortBy, String sortDir, SearchFeedbackRequest request) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Specification<Feedback> specification = Specification.allOf();

        if (request != null) {
            specification = specification.and(specification(toSearchParams(request)));
        }

        if (request.getProductId() != null && request.getProductId() > 0) {
            specification = specification.and(GenericSpecification.joinFieldEqual("product", "id", request.getProductId()));
        }

        Page<Feedback> pageResults = feedbackRepository.findAll(specification, pageable);
        return pageResults.map(f -> modelMapper.map(f, FeedbackResponse.class));
    }

    @Override
    public FeedbackSummaryResponse getSummaryFeedback(SearchFeedbackRequest request) {
        Specification<Feedback> specification = Specification.allOf();

        if (request != null) {
            specification = specification.and(specification(toSearchParams(request)));
        }

        if (request.getProductId() != null && request.getProductId() > 0) {
            specification = specification.and(GenericSpecification.joinFieldEqual("product", "id", request.getProductId()));
        }

        List<Feedback> allFeedbacks = feedbackRepository.findAll(specification);
        long totalFeedback = allFeedbacks.size();
        double averageRating = totalFeedback > 0
                ? allFeedbacks.stream().mapToInt(Feedback::getRating).average().orElse(0.0)
                : 0.0;

        long oneStar = allFeedbacks.stream().filter(f -> f.getRating() == 1).count();
        long twoStar = allFeedbacks.stream().filter(f -> f.getRating() == 2).count();
        long threeStar = allFeedbacks.stream().filter(f -> f.getRating() == 3).count();
        long fourStar = allFeedbacks.stream().filter(f -> f.getRating() == 4).count();
        long fiveStar = allFeedbacks.stream().filter(f -> f.getRating() == 5).count();

        return new FeedbackSummaryResponse(
                averageRating,
                totalFeedback,
                oneStar,
                twoStar,
                threeStar,
                fourStar,
                fiveStar
        );
    }


    @Override
    public List<FeedbackResponse> getAllOfOrder(Long orderId) {
        List<Feedback> feedbacks = feedbackRepository.findByOrderId((orderId));
        return feedbacks.stream().map(feedback -> {
            FeedbackResponse response = modelMapper.map(feedback, FeedbackResponse.class);

            // Tìm OrderDetail tương ứng của product trong đơn hàng
            Order order = feedback.getOrder();
            Product product = feedback.getProduct();

            if (order != null && product != null && order.getOrderDetails() != null) {
                order.getOrderDetails().stream()
                        .filter(detail -> detail.getProduct().getId().equals(product.getId()))
                        .findFirst()
                        .ifPresent(detail -> response.setQuantity(detail.getQuantity()));
            }

            if (order != null && product != null && order.getOrderDetails() != null) {
                order.getOrderDetails().stream()
                        .filter(detail -> detail.getProduct().getId().equals(product.getId()))
                        .findFirst()
                        .ifPresent(detail -> response.setTotalPrice(detail.getTotalPrice()));
            }

            return response;
        }).toList();    }

    @Override
    public FeedbackResponse update(FeedbackRequest request) {
        Feedback feedback = feedbackRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Feedback"));
        if(feedback.isUpdated()){
            throw new ECommerseException(HttpStatus.BAD_REQUEST,  "You are not allowed to update this order!");
        }else {
            feedback.setRating(request.getRating());
            feedback.setComment(request.getComment());
            feedback.setImage(request.getImage());
            feedback.setUpdated(true);
            return modelMapper.map(feedbackRepository.save(feedback), FeedbackResponse.class);
        }
    }

    @Override
    public FeedbackResponse delete(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback"));

        feedback.setStatus(EntityStatus.INACTIVE);
        return modelMapper.map(feedbackRepository.save(feedback), FeedbackResponse.class);
    }

    private Specification<Feedback> specification(Map<String, Object> searchParams) {
        List<Specification<Feedback>> specs = new ArrayList<>();

        searchParams.forEach((key, value) -> {
            switch (key) {
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
                case "status":
                        specs.add(GenericSpecification.fieldIn(key, (Collection<?>) value));
                    break;
                case "rating":
                    specs.add(GenericSpecification.fieldEqual(key, (Integer) value));
                    break;
                case "searchText":
                    String text = (String) value;

                    Specification<Feedback> searchSpec1 =
                            GenericSpecification.joinFieldContains("user", "fullName", text);
                    Specification<Feedback> searchSpec2 =
                            GenericSpecification.joinFieldContains("user", "email", text);
                    Specification<Feedback> searchSpec3 =
                            GenericSpecification.joinFieldContains("product", "name", text);

                    // kết hợp cả hai điều kiện OR
                    Specification<Feedback> combinedSpec = searchSpec1.or(searchSpec2).or(searchSpec3);

                    specs.add(combinedSpec);
                    break;
            }
        });

        Specification<Feedback> result = null;
        for (Specification<Feedback> spec : specs) {
            result = (result == null) ? spec : result.and(spec);
        }

        return result;
    }

    private Map<String, Object> toSearchParams(SearchFeedbackRequest request) {
        Map<String, Object> params = new HashMap<>();

        if (request.getDateFrom() != null) {
            params.put("dateFrom", request.getDateFrom());
        }

        if (request.getDateTo() != null) {
            params.put("dateTo", request.getDateTo());
        }

        if (request.getSearchText() != null && !request.getSearchText().isBlank()) {
            params.put("searchText", request.getSearchText().trim());
        }

        if (request.getRating() != 0) {
            params.put("rating", request.getRating());
        }

        if (request.getStatuses() != null && !request.getStatuses().isEmpty()) {
            params.put("status", request.getStatuses());
        }

        return params;
    }
}
