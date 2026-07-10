package com.piyush.Ledgerflow.controller;

import com.piyush.Ledgerflow.dto.request.response.AccountResponse;
import com.piyush.Ledgerflow.dto.request.response.BalanceResponse;
import com.piyush.Ledgerflow.dto.request.response.CreateAccountResponse;
import com.piyush.Ledgerflow.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<CreateAccountResponse> createAccount() {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.createAccount());
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAccounts() {

        return ResponseEntity.ok(
                accountService.getUserAccounts()
        );
    }

    @GetMapping("/balance/{accountId}")
    public ResponseEntity<BalanceResponse> getBalance(
            @PathVariable Long accountId) {

        return ResponseEntity.ok(
                accountService.getBalance(accountId)
        );
    }

}
