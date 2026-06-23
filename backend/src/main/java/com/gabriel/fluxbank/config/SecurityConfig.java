package com.gabriel.fluxbank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((
                                request,
                                response,
                                authException
                        ) -> response.sendError(
                                HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.getReasonPhrase()
                        ))
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/health"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/users"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/login"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/auth/me"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/auth/session"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/logout"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/email-verification/verify"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/email-verification/resend"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .logout(logout -> logout.disable())
                .build();
    }
}