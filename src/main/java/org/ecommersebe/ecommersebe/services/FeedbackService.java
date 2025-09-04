package org.ecommersebe.ecommersebe.services;

import org.ecommersebe.ecommersebe.models.payload.dto.feedback.FeedbackRequest;
import org.ecommersebe.ecommersebe.models.payload.dto.feedback.FeedbackResponse;
import org.ecommersebe.ecommersebe.models.payload.dto.feedback.FeedbackSummaryResponse;
import org.ecommersebe.ecommersebe.models.payload.dto.feedback.SearchFeedbackRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FeedbackService {
    List<FeedbackResponse> create(List<FeedbackRequest> request, Long userId);
    Page<FeedbackResponse> getAll(int pageNo, int pageSize, String sortBy, String sortDir, SearchFeedbackRequest request);
    FeedbackSummaryResponse getSummaryFeedback(SearchFeedbackRequest request);
    List<FeedbackResponse> getAllOfOrder(Long orderId);
    FeedbackResponse update(FeedbackRequest request);
    FeedbackResponse delete(Long id);
}
