package org.ecommersebe.ecommersebe.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ecommersebe.ecommersebe.models.payload.dto.auth.JWTAuthResponse;
import org.ecommersebe.ecommersebe.models.payload.dto.auth.SignInRequest;
import org.ecommersebe.ecommersebe.models.payload.dto.auth.SignUpRequest;
import org.ecommersebe.ecommersebe.models.payload.dto.user.UserResponse;
import org.springframework.http.ResponseEntity;


public interface AuthService{
    JWTAuthResponse signInUser(SignInRequest loginDto);
    JWTAuthResponse signUpCustomer(SignUpRequest signupDto);
    JWTAuthResponse signUpStaff(SignUpRequest signupDto);
    UserResponse getUserInfo();
    ResponseEntity<JWTAuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response);
    void changePassword(String oldPassword, String newPassword);
}
