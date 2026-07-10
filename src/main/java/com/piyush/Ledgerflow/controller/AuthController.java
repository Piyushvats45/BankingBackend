package com.piyush.Ledgerflow.controller;


import com.piyush.Ledgerflow.dto.request.LoginRequest;
import com.piyush.Ledgerflow.dto.request.RegisterRequest;
import com.piyush.Ledgerflow.dto.request.response.AuthResponse;
import com.piyush.Ledgerflow.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        AuthResponse response = authService.register(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authHeader
    ) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok("Already logged out.");
        }

        authService.logout(authHeader.substring(7));

        return ResponseEntity.ok("Logged out successfully.");
    }

}
