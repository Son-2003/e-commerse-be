package org.ecommersebe.ecommersebe.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.ecommersebe.ecommersebe.models.constants.AppConstants;
import org.ecommersebe.ecommersebe.models.entities.Token;
import org.ecommersebe.ecommersebe.models.entities.User;
import org.ecommersebe.ecommersebe.models.enums.EntityStatus;
import org.ecommersebe.ecommersebe.models.enums.RoleType;
import org.ecommersebe.ecommersebe.models.exception.ECommerseException;
import org.ecommersebe.ecommersebe.models.exception.ResourceNotFoundException;
import org.ecommersebe.ecommersebe.models.payload.dto.auth.JWTAuthResponse;
import org.ecommersebe.ecommersebe.models.payload.dto.auth.SignInRequest;
import org.ecommersebe.ecommersebe.models.payload.dto.auth.SignUpRequest;
import org.ecommersebe.ecommersebe.models.payload.dto.user.UserResponse;
import org.ecommersebe.ecommersebe.repositories.TokenRepository;
import org.ecommersebe.ecommersebe.repositories.UserRepository;
import org.ecommersebe.ecommersebe.security.JwtTokenProvider;
import org.ecommersebe.ecommersebe.services.AuthService;
import org.ecommersebe.ecommersebe.services.EmailService;
import org.ecommersebe.ecommersebe.utils.AutomaticGeneratedPassword;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ModelMapper mapper;

    @Override
    public JWTAuthResponse signInUser(SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getEmailOrPhone(), signInRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByEmailOrPhone(signInRequest.getEmailOrPhone(), signInRequest.getEmailOrPhone()).orElseThrow(
                () -> new ResourceNotFoundException("User")
        );
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        revokeAllTokenByUser(user);
        saveUserToken(accessToken, refreshToken, user);
        return new JWTAuthResponse(accessToken, refreshToken, "User login was successful");
    }

    @Override
    public JWTAuthResponse signUpCustomer(SignUpRequest signupDto) {
        User user = setUpUser(signupDto, RoleType.CUSTOMER);
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        user = userRepository.save(user);

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        saveUserToken(accessToken, refreshToken, user);

        return new JWTAuthResponse(accessToken, refreshToken,"User registration was successful");
    }

    @Override
    public JWTAuthResponse signUpStaff(SignUpRequest signupDto) {
        User user = setUpUser(signupDto, RoleType.STAFF);
        String randomPassword = AutomaticGeneratedPassword.generateRandomPassword();
        user.setPassword(passwordEncoder.encode(randomPassword));
        user = userRepository.save(user);

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        saveUserToken(accessToken, refreshToken, user);

        String content = "<html>" +
                "<head>" +
                "<style>" +
                "table { width: 100%; border-collapse: collapse; }" +
                "th, td { padding: 10px; border: 1px solid #ddd; text-align: left; }" +
                "th { background-color: #f2f2f2; }" +
                "body { font-family: Arial, sans-serif; }" +
                ".highlight { font-weight: bold; color: red; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<p class='greeting'>Hi, " + user.getFullName() + ",</p>" +
                "<p>Your SkedEat system account has been successfully created.</p>" +
                "<p>Please log into the system with the following information:</p>" +
                "<table>" +
                "<tr><th>Username</th><td>" + user.getEmail() + "</td></tr>" +
                "<tr><th>Password</th><td class='highlight'>" + randomPassword + "</td></tr>" +
                "</table>" +
                "<p><strong>Note:</strong> Please change your password after logging in.</p>" +
                "</body>" +
                "</html>";
        emailService.sendEmail(user.getEmail(), "[SkedEat] - Account Successfully Created", content);

        return new JWTAuthResponse(accessToken, refreshToken,"User registration was successful");
    }

    @Override
    public UserResponse getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userRepository.findByEmailOrPhone(userName, userName)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        return mapper.map(user, UserResponse.class);
    }

    @Override
    public ResponseEntity<JWTAuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        // extract the token from authorization header
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        // extract username from token
        String email = jwtTokenProvider.getUsernameFromJwt(token);

        // check if the user exist in database
        User user = userRepository.findByEmailOrPhone(email, email)
                .orElseThrow(()->new RuntimeException("No user found"));

        // check if the token is valid
        if(jwtTokenProvider.isValidRefreshToken(token, user.getEmail())) {
            // generate access token
            String accessToken = jwtTokenProvider.generateAccessToken(user);
            String refreshToken = jwtTokenProvider.generateRefreshToken(user);

            revokeAllTokenByUser(user);
            saveUserToken(accessToken, refreshToken, user);

            return new ResponseEntity<>(new JWTAuthResponse(accessToken, refreshToken, "New token generated"), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmailOrPhone(username, username)
                .orElseThrow(() -> new ECommerseException(HttpStatus.NOT_FOUND, "User cannot found!"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ECommerseException(HttpStatus.BAD_REQUEST, "Old password does not match!");
        }
        if(!newPassword.matches(AppConstants.PASSWORD_REGEX))
            throw new ECommerseException(HttpStatus.BAD_REQUEST,
                    "Password must have at least 8 characters with at least one uppercase letter, one number, and one special character (!@#$%^&*).");
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private User setUpUser(SignUpRequest signUpRequest, RoleType roleType) {
        // add check if email already exists
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ECommerseException(HttpStatus.BAD_REQUEST, "Email is already exist!");
        }

        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setFullName(signUpRequest.getFullName());
        user.setPassword(signUpRequest.getPassword());
        user.setPhone(signUpRequest.getPhone());
        user.setRole(roleType);
        user.setImage(signUpRequest.getImage());
        user.setStatus(EntityStatus.ACTIVE);

        return user;
    }

    private void saveUserToken(String accessToken, String refreshToken, User user) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllByUser_Id(user.getId());
        if(validTokens.isEmpty()) {
            return;
        }
        validTokens.forEach(t-> t.setLoggedOut(true));

        tokenRepository.saveAll(validTokens);
    }
}
