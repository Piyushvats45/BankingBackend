package com.piyush.Ledgerflow.dto.request.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthResponse {

    private Long id;
    private String name;
    private String email;
    private String token;
}
