package com.piyush.Ledgerflow.service.impl;


import com.piyush.Ledgerflow.dto.request.response.AccountResponse;
import com.piyush.Ledgerflow.dto.request.response.BalanceResponse;
import com.piyush.Ledgerflow.dto.request.response.CreateAccountResponse;
import com.piyush.Ledgerflow.entity.Account;
import com.piyush.Ledgerflow.entity.User;
import com.piyush.Ledgerflow.enums.AccountStatus;
import com.piyush.Ledgerflow.exception.AccountNotFoundException;
import com.piyush.Ledgerflow.repository.AccountRepository;
import com.piyush.Ledgerflow.repository.LedgerRepository;
import com.piyush.Ledgerflow.security.CustomUserDetails;
import com.piyush.Ledgerflow.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final LedgerRepository ledgerRepository;

    @Override
    public CreateAccountResponse createAccount() {

        User user = getCurrentUser();

        Account account = Account.builder()
                .user(user)
                .currency("INR")
                .status(AccountStatus.ACTIVE)
                .build();

        accountRepository.save(account);

        return CreateAccountResponse.builder()
                .accountId(account.getId())
                .status(account.getStatus().name())
                .currency(account.getCurrency())
                .build();
    }

    @Override
    public List<AccountResponse> getUserAccounts() {

        User user = getCurrentUser();

        return accountRepository.findByUser(user)
                .stream()
                .map(account -> AccountResponse.builder()
                        .accountId(account.getId())
                        .status(account.getStatus().name())
                        .currency(account.getCurrency())
                        .build())
                .toList();
    }

    @Override
    public BalanceResponse getBalance(Long accountId) {

        User user = getCurrentUser();

        Account account = accountRepository
                .findByIdAndUser(accountId, user)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found."));

        BigDecimal balance =
                ledgerRepository.getAccountBalance(account);

        return BalanceResponse.builder()
                .accountId(account.getId())
                .balance(balance)
                .build();
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        return userDetails.getUser();
    }

}
