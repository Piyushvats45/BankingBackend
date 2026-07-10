package com.piyush.Ledgerflow.service;


import com.piyush.Ledgerflow.dto.request.response.AuthResponse;
import com.piyush.Ledgerflow.dto.request.LoginRequest;
import com.piyush.Ledgerflow.dto.request.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    void logout(String token);

}
