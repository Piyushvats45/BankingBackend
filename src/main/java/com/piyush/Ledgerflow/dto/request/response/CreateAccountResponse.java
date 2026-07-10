package com.piyush.Ledgerflow.dto.request.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateAccountResponse {

    private Long accountId;
    private String status;
    private String currency;

}
