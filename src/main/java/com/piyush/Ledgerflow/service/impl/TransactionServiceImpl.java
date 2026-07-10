package com.piyush.Ledgerflow.service.impl;

import com.piyush.Ledgerflow.repository.AccountRepository;
import com.piyush.Ledgerflow.repository.LedgerRepository;
import com.piyush.Ledgerflow.repository.TransactionRepository;
import com.piyush.Ledgerflow.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final LedgerRepository ledgerRepository;
    private final EmailService emailService;

}
