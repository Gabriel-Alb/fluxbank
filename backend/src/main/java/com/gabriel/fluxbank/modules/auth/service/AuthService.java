package com.gabriel.fluxbank.modules.auth.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabriel.fluxbank.exception.BusinessException;
import com.gabriel.fluxbank.modules.auth.dto.request.LoginRequest;
import com.gabriel.fluxbank.modules.auth.dto.response.AuthenticatedUserResponse;
import com.gabriel.fluxbank.modules.auth.dto.response.LoginResponse;
import com.gabriel.fluxbank.modules.auth.dto.response.SessionStatusResponse;
import com.gabriel.fluxbank.modules.auth.mapper.AuthMapper;
import com.gabriel.fluxbank.modules.user.entity.User;
import com.gabriel.fluxbank.modules.user.enums.UserStatus;
import com.gabriel.fluxbank.modules.user.repository.UserRepository;
import com.gabriel.fluxbank.modules.user.util.UserInputNormalizer;
import com.gabriel.fluxbank.shared.security.DataProtectionService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Service
public class AuthService {

    private static final String INVALID_CREDENTIALS_MESSAGE =
            "Invalid email or password";

    private static final String SESSION_COOKIE_NAME =
            "FLUXBANK_SESSION";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DataProtectionService dataProtectionService;
    private final AuthMapper authMapper;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            DataProtectionService dataProtectionService,
            AuthMapper authMapper
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.dataProtectionService = dataProtectionService;
        this.authMapper = authMapper;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(
            LoginRequest request,
            HttpServletRequest servletRequest
    ) {
        String normalizedEmail = UserInputNormalizer.normalizeEmail(
                request.email()
        );

        byte[] emailLookupHash =
                dataProtectionService.createLookupHash(normalizedEmail);

        User user = userRepository
                .findByEmailLookupHash(emailLookupHash)
                .orElseThrow(this::invalidCredentials);

        if (!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash()
        )) {
            throw invalidCredentials();
        }

        validateUserCanAuthenticate(user);

        createAuthenticatedSession(user, servletRequest);

        return new LoginResponse(
                authMapper.toAuthenticatedUserResponse(user)
        );
    }

    @Transactional(readOnly = true)
    public SessionStatusResponse getCurrentSession(
            Authentication authentication
    ) {
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getPrincipal() == null) {
            return new SessionStatusResponse(false, null);
        }

        UUID userId = extractUserId(authentication);

        AuthenticatedUserResponse user = userRepository
                .findById(userId)
                .map(authMapper::toAuthenticatedUserResponse)
                .orElse(null);

        if (user == null) {
            return new SessionStatusResponse(false, null);
        }

        return new SessionStatusResponse(true, user);
    }

    public void logout(
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    ) {
        SecurityContextHolder.clearContext();

        HttpSession session = servletRequest.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        expireSessionCookie(servletResponse);
    }

    private void createAuthenticatedSession(
            User user,
            HttpServletRequest servletRequest
    ) {
        HttpSession session = servletRequest.getSession(true);

        servletRequest.changeSessionId();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        user.getId().toString(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );

        SecurityContext securityContext =
                SecurityContextHolder.createEmptyContext();

        securityContext.setAuthentication(authentication);

        SecurityContextHolder.setContext(securityContext);

        session.setAttribute(
                HttpSessionSecurityContextRepository
                        .SPRING_SECURITY_CONTEXT_KEY,
                securityContext
        );
    }

    private void expireSessionCookie(
            HttpServletResponse servletResponse
    ) {
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        servletResponse.addCookie(cookie);
    }

    private UUID extractUserId(Authentication authentication) {
        try {
            return UUID.fromString(authentication.getPrincipal().toString());
        } catch (IllegalArgumentException exception) {
            throw new BusinessException(
                    "Invalid authenticated session",
                    HttpStatus.UNAUTHORIZED
            );
        }
    }

    private void validateUserCanAuthenticate(User user) {
        if (user.getStatus() == UserStatus.SUSPENDED) {
            throw new BusinessException(
                    "User account is suspended",
                    HttpStatus.FORBIDDEN
            );
        }

        if (user.getStatus() == UserStatus.DEACTIVATED) {
            throw new BusinessException(
                    "User account is deactivated",
                    HttpStatus.FORBIDDEN
            );
        }
    }

    private BusinessException invalidCredentials() {
        return new BusinessException(
                INVALID_CREDENTIALS_MESSAGE,
                HttpStatus.UNAUTHORIZED
        );
    }
}