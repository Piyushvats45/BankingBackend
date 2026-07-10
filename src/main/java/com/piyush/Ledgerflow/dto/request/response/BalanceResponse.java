package com.piyush.Ledgerflow.dto.request.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BalanceResponse {

    private Long accountId;

    private BigDecimal balance;
}
