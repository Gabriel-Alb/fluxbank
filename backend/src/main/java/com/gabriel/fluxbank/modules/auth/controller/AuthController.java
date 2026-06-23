package com.gabriel.fluxbank.modules.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.fluxbank.modules.auth.dto.request.EmailVerificationRequest;
import com.gabriel.fluxbank.modules.auth.dto.request.EmailVerificationResendRequest;
import com.gabriel.fluxbank.modules.auth.dto.request.LoginRequest;
import com.gabriel.fluxbank.modules.auth.dto.response.CsrfTokenResponse;
import com.gabriel.fluxbank.modules.auth.dto.response.EmailVerificationResendResponse;
import com.gabriel.fluxbank.modules.auth.dto.response.EmailVerificationResponse;
import com.gabriel.fluxbank.modules.auth.dto.response.LoginResponse;
import com.gabriel.fluxbank.modules.auth.dto.response.SessionStatusResponse;
import com.gabriel.fluxbank.modules.auth.service.AuthService;
import com.gabriel.fluxbank.modules.auth.service.EmailVerificationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final EmailVerificationService emailVerificationService;

    @GetMapping("/csrf")
    public ResponseEntity<CsrfTokenResponse> csrf(
            CsrfToken csrfToken
    ) {
        return ResponseEntity.ok(
                new CsrfTokenResponse(
                        csrfToken.getHeaderName(),
                        csrfToken.getParameterName(),
                        csrfToken.getToken()
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest servletRequest
    ) {
        return ResponseEntity.ok(
                authService.login(request, servletRequest)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<SessionStatusResponse> me(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                authService.getCurrentSession(authentication)
        );
    }

    @GetMapping("/session")
    public ResponseEntity<SessionStatusResponse> session(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                authService.getCurrentSession(authentication)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    ) {
        authService.logout(servletRequest, servletResponse);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/email-verification/verify")
    public ResponseEntity<EmailVerificationResponse> verifyEmail(
            @Valid @RequestBody EmailVerificationRequest request
    ) {
        return ResponseEntity.ok(
                emailVerificationService.verifyEmail(request.token())
        );
    }

    @PostMapping("/email-verification/resend")
    public ResponseEntity<EmailVerificationResendResponse> resendEmailVerification(
            @Valid @RequestBody EmailVerificationResendRequest request
    ) {
        return ResponseEntity.ok(
                emailVerificationService.resendVerificationEmail(
                        request.email()
                )
        );
    }
}