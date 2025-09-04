package org.ecommersebe.ecommersebe.controllers;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ecommersebe.ecommersebe.models.constants.AppConstants;
import org.ecommersebe.ecommersebe.models.payload.dto.feedback.FeedbackRequest;
import org.ecommersebe.ecommersebe.models.payload.dto.feedback.FeedbackResponse;
import org.ecommersebe.ecommersebe.models.payload.dto.feedback.FeedbackSummaryResponse;
import org.ecommersebe.ecommersebe.models.payload.dto.feedback.SearchFeedbackRequest;
import org.ecommersebe.ecommersebe.services.FeedbackService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/search")
    public ResponseEntity<Page<FeedbackResponse>> getAll(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @RequestBody @Nullable SearchFeedbackRequest request
    ) {
        return ResponseEntity.ok(feedbackService.getAll(pageNo, pageSize, sortBy, sortDir, request));
    }

    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/summary")
    public ResponseEntity<FeedbackSummaryResponse> getSummaryFeedback(
            @RequestBody @Nullable SearchFeedbackRequest request
    ) {
        return ResponseEntity.ok(feedbackService.getSummaryFeedback(request));
    }

    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<FeedbackResponse>> getAllOfOrder(@PathVariable long orderId) {
        return ResponseEntity.ok(feedbackService.getAllOfOrder(orderId));
    }

    @ApiResponse(responseCode = "201", description = "Http Status 201 Created")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<List<FeedbackResponse>> createFeedback(@RequestBody @Valid List<FeedbackRequest> requests, @RequestParam Long orderId) {
        return ResponseEntity.ok(feedbackService.create(requests, orderId));
    }

    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping()
    public ResponseEntity<FeedbackResponse> update(@RequestBody @Valid FeedbackRequest request){
        return ResponseEntity.ok(feedbackService.update(request));
    }

    @ApiResponse(responseCode = "200", description = "Http Status 200 SUCCESS")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    @DeleteMapping()
    public ResponseEntity<FeedbackResponse> delete(@RequestParam Long id){
        return ResponseEntity.ok( feedbackService.delete(id));
    }
}
