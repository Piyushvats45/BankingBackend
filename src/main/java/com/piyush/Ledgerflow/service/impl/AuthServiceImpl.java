package com.piyush.Ledgerflow.service.impl;


import com.piyush.Ledgerflow.dto.request.LoginRequest;
import com.piyush.Ledgerflow.dto.request.RegisterRequest;
import com.piyush.Ledgerflow.dto.request.response.AuthResponse;
import com.piyush.Ledgerflow.entity.BlacklistedToken;
import com.piyush.Ledgerflow.entity.User;
import com.piyush.Ledgerflow.repository.BlacklistedTokenRepository;
import com.piyush.Ledgerflow.repository.UserRepository;
import com.piyush.Ledgerflow.security.CustomUserDetails;
import com.piyush.Ledgerflow.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final BlacklistedTokenRepository blacklistRepository;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User already exists with this email.");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(
                new CustomUserDetails(user)
        );

        emailService.sendRegistrationEmail(
                user.getEmail(),
                user.getName()
        );

        return AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .token(token)
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(

                        request.getEmail(),

                        request.getPassword()

                )

        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        String token = jwtService.generateToken(
                new CustomUserDetails(user)
        );

        return AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .token(token)
                .build();
    }

    @Override
    public void logout(String token) {

        BlacklistedToken blacklistedToken =
                BlacklistedToken.builder()
                        .token(token)
                        .build();

        blacklistRepository.save(blacklistedToken);
    }
}
