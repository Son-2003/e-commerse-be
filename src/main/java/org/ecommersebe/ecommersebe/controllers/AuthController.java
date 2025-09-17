package org.ecommersebe.ecommersebe.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ecommersebe.ecommersebe.models.payload.dto.auth.JWTAuthResponse;
import org.ecommersebe.ecommersebe.models.payload.dto.auth.PasswordChangeRequest;
import org.ecommersebe.ecommersebe.models.payload.dto.auth.SignInRequest;
import org.ecommersebe.ecommersebe.models.payload.dto.auth.SignUpRequest;
import org.ecommersebe.ecommersebe.models.payload.dto.user.UserResponse;
import org.ecommersebe.ecommersebe.services.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Sign in customer",
            description = "Sign in customer by Email, Phone"
    )
    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> signInUser(@Valid @RequestBody SignInRequest signInRequest){
        JWTAuthResponse jwtAuthResponse = authService.signInUser(signInRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtAuthResponse.getAccessToken());
        return new ResponseEntity<>(jwtAuthResponse, headers, HttpStatus.OK);
    }

    @Operation(
            summary = "Sign in admin, staff",
            description = "Sign in admin, staff by Email, Phone"
    )
    @PostMapping("/login/admin")
    public ResponseEntity<JWTAuthResponse> signInAdmin(@Valid @RequestBody SignInRequest signInRequest){
        JWTAuthResponse jwtAuthResponse = authService.signInAdmin(signInRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtAuthResponse.getAccessToken());
        return new ResponseEntity<>(jwtAuthResponse, headers, HttpStatus.OK);
    }

    @Operation(
            summary = "Sign up customer"
    )
    @PostMapping(value = "/register/customer")
    public ResponseEntity<JWTAuthResponse> signUpCustomer(@Valid @RequestBody SignUpRequest signUpRequest){
        JWTAuthResponse response = authService.signUpCustomer(signUpRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Sign up staff"
    )
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/register/staff")
    public ResponseEntity<JWTAuthResponse> signUpStaff(@Valid @RequestBody SignUpRequest signUpRequest){
        JWTAuthResponse response = authService.signUpStaff(signUpRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequest request) {
        authService.changePassword(request.getOldPassword(), request.getNewPassword());
        return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
    }

    @Operation(
            summary = "Refresh token"
    )
    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/refresh-token")
    public ResponseEntity<JWTAuthResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return authService.refreshToken(request, response);
    }

    @Operation(
            summary = "Get Info User"
    )
    @SecurityRequirement(name = "Bear Authentication")
    @GetMapping("/info")
    public ResponseEntity<UserResponse> getInfo() {
        return ResponseEntity.ok(authService.getUserInfo());
    }
}
