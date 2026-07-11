package com.piyush.Ledgerflow.controller;

import com.piyush.Ledgerflow.dto.request.TransferRequest;
import com.piyush.Ledgerflow.dto.request.response.TransactionResponse;
import com.piyush.Ledgerflow.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> transfer(
            @Valid @RequestBody TransferRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        transactionService.transfer(request)
                );
    }

    @PreAuthorize("hasRole('SYSTEM')")
    @PostMapping("/system/initial-funds")
    public ResponseEntity<TransactionResponse> initialFunds(
            @Valid @RequestBody TransferRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        transactionService.initialFund(request)
                );
    }

}