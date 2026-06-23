package com.gabriel.fluxbank.config;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FluxBankSessionCookieConfig {

    private static final String SESSION_COOKIE_NAME = "FLUXBANK_SESSION";

    @Bean
    public ServletContextInitializer sessionCookieInitializer() {
        return servletContext -> {
            jakarta.servlet.SessionCookieConfig sessionCookieConfig =
                    servletContext.getSessionCookieConfig();

            sessionCookieConfig.setName(SESSION_COOKIE_NAME);
            sessionCookieConfig.setHttpOnly(true);
            sessionCookieConfig.setSecure(false);
            sessionCookieConfig.setPath("/");
        };
    }
}