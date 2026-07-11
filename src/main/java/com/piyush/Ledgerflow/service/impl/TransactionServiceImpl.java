package com.piyush.Ledgerflow.service.impl;

import com.piyush.Ledgerflow.dto.request.TransferRequest;
import com.piyush.Ledgerflow.dto.request.response.TransactionResponse;
import com.piyush.Ledgerflow.entity.Account;
import com.piyush.Ledgerflow.entity.Ledger;
import com.piyush.Ledgerflow.entity.Transaction;
import com.piyush.Ledgerflow.entity.User;
import com.piyush.Ledgerflow.enums.AccountStatus;
import com.piyush.Ledgerflow.enums.LedgerType;
import com.piyush.Ledgerflow.enums.TransactionStatus;
import com.piyush.Ledgerflow.exception.AccountNotFoundException;
import com.piyush.Ledgerflow.exception.InsufficientBalanceException;
import com.piyush.Ledgerflow.exception.TransactionException;
import com.piyush.Ledgerflow.repository.AccountRepository;
import com.piyush.Ledgerflow.repository.LedgerRepository;
import com.piyush.Ledgerflow.repository.TransactionRepository;
import com.piyush.Ledgerflow.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final LedgerRepository ledgerRepository;
    private final EmailService emailService;

    @Override
    public TransactionResponse transfer(
            TransferRequest request) {

        User currentUser = SecurityUtil.getCurrentUser();

        Account fromAccount =
                validateSenderAccount(
                        request.getFromAccountId(),
                        currentUser
                );

        Account toAccount =
                validateReceiverAccount(
                        request.getToAccountId()
                );

        validateIdempotency(
                request.getIdempotencyKey()
        );

        validateAccountStatus(
                fromAccount,
                toAccount
        );

        validateBalance(
                fromAccount,
                request.getAmount()
        );

        Transaction transaction =
                createPendingTransaction(
                        request,
                        fromAccount,
                        toAccount
                );

        createLedgerEntries(
                transaction,
                fromAccount,
                toAccount,
                request.getAmount()
        );

        completeTransaction(transaction);

        emailService.sendTransactionEmail(
                currentUser.getEmail(),
                currentUser.getName(),
                request.getAmount(),
                toAccount.getId().toString()
        );

        return TransactionResponse.builder()
                .transactionId(transaction.getId())
                .status(transaction.getStatus().name())
                .message("Transaction completed successfully.")
                .build();

    }
    private Account validateSenderAccount(
            Long accountId,
            User currentUser) {

        return accountRepository
                .findByIdAndUser(accountId, currentUser)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Sender account not found."
                        ));
    }
    private Account validateReceiverAccount(
            Long accountId) {

        return accountRepository
                .findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Receiver account not found."
                        ));
    }
    private void validateIdempotency(
            String key) {

        transactionRepository
                .findByIdempotencyKey(key)
                .ifPresent(transaction -> {

                    switch (transaction.getStatus()) {

                        case COMPLETED ->
                                throw new TransactionException(
                                        "Transaction already completed."
                                );

                        case PENDING ->
                                throw new TransactionException(
                                        "Transaction already in progress."
                                );

                        case FAILED ->
                                throw new TransactionException(
                                        "Previous transaction failed."
                                );

                        case REVERSED ->
                                throw new TransactionException(
                                        "Transaction reversed."
                                );

                    }

                });

    }
    private void validateAccountStatus(
            Account from,
            Account to) {

        if (from.getStatus() != AccountStatus.ACTIVE ||
                to.getStatus() != AccountStatus.ACTIVE) {

            throw new TransactionException(
                    "Both accounts must be ACTIVE."
            );

        }

    }
    private void validateBalance(
            Account account,
            BigDecimal amount) {

        BigDecimal balance =
                ledgerRepository
                        .getAccountBalance(account);

        if (balance.compareTo(amount) < 0) {

            throw new InsufficientBalanceException(
                    "Insufficient balance."
            );

        }

    }
    private Transaction createPendingTransaction(
            TransferRequest request,
            Account fromAccount,
            Account toAccount
    ) {

        Transaction transaction = Transaction.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(request.getAmount())
                .status(TransactionStatus.PENDING)
                .idempotencyKey(request.getIdempotencyKey())
                .build();

        return transactionRepository.save(transaction);
    }
    private void createLedgerEntries(
            Transaction transaction,
            Account fromAccount,
            Account toAccount,
            BigDecimal amount
    ) {

        Ledger debitEntry = Ledger.builder()
                .account(fromAccount)
                .transaction(transaction)
                .amount(amount)
                .type(LedgerType.DEBIT)
                .build();

        Ledger creditEntry = Ledger.builder()
                .account(toAccount)
                .transaction(transaction)
                .amount(amount)
                .type(LedgerType.CREDIT)
                .build();

        ledgerRepository.save(debitEntry);
        ledgerRepository.save(creditEntry);
    }
    private void completeTransaction(Transaction transaction) {

        transaction.setStatus(TransactionStatus.COMPLETED);

        transactionRepository.save(transaction);
    }
    @Override
    public TransactionResponse initialFund(
            TransferRequest request) {

        User systemUser = SecurityUtil.getCurrentUser();

        Account systemAccount =
                accountRepository
                        .findByUserAndStatus(
                                systemUser,
                                AccountStatus.ACTIVE
                        )
                        .stream()
                        .findFirst()
                        .orElseThrow(() ->
                                new AccountNotFoundException(
                                        "System account not found."
                                ));

        request.setFromAccountId(systemAccount.getId());

        return transfer(request);
    }
}
