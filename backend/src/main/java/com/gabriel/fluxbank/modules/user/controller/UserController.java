package com.gabriel.fluxbank.modules.user.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.fluxbank.modules.user.dto.request.CreateUserRequest;
import com.gabriel.fluxbank.modules.user.dto.response.UserResponse;
import com.gabriel.fluxbank.modules.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody CreateUserRequest request
    ) {
        UserResponse response = userService.register(request);

        URI location = URI.create(
                "/api/users/" + response.id()
        );

        return ResponseEntity
                .created(location)
                .body(response);
    }
}