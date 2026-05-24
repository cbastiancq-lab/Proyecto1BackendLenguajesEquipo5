package com.ecommerce.proyecto.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.proyecto.business.service.AuthService;
import com.ecommerce.proyecto.domain.dto.UserDto;
import com.ecommerce.proyecto.domain.request.LoginRequest;
import com.ecommerce.proyecto.domain.request.RegisterRequest;
import com.ecommerce.proyecto.domain.response.LoginResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

	@GetMapping("/test")
    public String test() {
        return "FUNCIONA";
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        System.out.println("ENTRO AL LOGIN");

        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterRequest request) {

        System.out.println("ENTRO AL REGISTER");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

}
