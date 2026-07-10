package com.piyush.Ledgerflow.service;


import com.piyush.Ledgerflow.dto.request.TransferRequest;
import com.piyush.Ledgerflow.dto.request.response.TransactionResponse;

public interface TransactionService {

    TransactionResponse transfer(TransferRequest request);

    TransactionResponse initialFund(TransferRequest request);

}