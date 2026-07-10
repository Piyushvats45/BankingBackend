package com.piyush.Ledgerflow.service;


import com.piyush.Ledgerflow.dto.request.response.AccountResponse;
import com.piyush.Ledgerflow.dto.request.response.BalanceResponse;
import com.piyush.Ledgerflow.dto.request.response.CreateAccountResponse;

import java.util.List;

public interface AccountService {

    CreateAccountResponse createAccount();

    List<AccountResponse> getUserAccounts();

    BalanceResponse getBalance(Long accountId);

}