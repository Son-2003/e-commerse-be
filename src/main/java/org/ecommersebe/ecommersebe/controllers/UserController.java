package org.ecommersebe.ecommersebe.controllers;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ecommersebe.ecommersebe.models.constants.AppConstants;
import org.ecommersebe.ecommersebe.models.payload.dto.user.UserRequest;
import org.ecommersebe.ecommersebe.models.payload.dto.user.UserResponse;
import org.ecommersebe.ecommersebe.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @RequestParam(name = "keyword") @Nullable String keyword
    ) {
        return ResponseEntity.ok(userService.search(pageNo, pageSize, sortBy, sortDir, keyword));
    }

    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')" )
    @GetMapping("{id}")
    public ResponseEntity<UserResponse> getUserDetail(@PathVariable Long id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @PutMapping
    public ResponseEntity<UserResponse> updateInfo(@RequestBody @Valid UserRequest request) {
        return ResponseEntity.ok(userService.update(request));
    }

    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')" )
    @GetMapping("count-active-users")
    public ResponseEntity<Integer> getNumberOfCustomersInSystem() {
        return ResponseEntity.ok(userService.getNumberOfActiveUsersInSystem());
    }
}
