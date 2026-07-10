package com.piyush.Ledgerflow.dto.request.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResponse {

    private Long transactionId;

    private String status;

    private String message;

}
