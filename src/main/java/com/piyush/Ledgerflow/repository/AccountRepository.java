package com.piyush.Ledgerflow.repository;



import com.piyush.Ledgerflow.entity.Account;
import com.piyush.Ledgerflow.entity.User;
import com.piyush.Ledgerflow.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByUser(User user);

    Optional<Account> findByIdAndUser(Long accountId, User user);

    List<Account> findByUserAndStatus(User user, AccountStatus status);

}