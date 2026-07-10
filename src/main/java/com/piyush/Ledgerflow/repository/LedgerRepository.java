package com.piyush.Ledgerflow.repository;


import com.piyush.Ledgerflow.entity.Account;
import com.piyush.Ledgerflow.entity.Ledger;
import com.piyush.Ledgerflow.enums.LedgerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface LedgerRepository extends JpaRepository<Ledger, Long> {

    @Query("""
            SELECT COALESCE(SUM(
                CASE
                    WHEN l.type = com.piyush.ledgerflow.enums.LedgerType.CREDIT
                    THEN l.amount
                    ELSE -l.amount
                END
            ),0)
            FROM Ledger l
            WHERE l.account = :account
            """)
    BigDecimal getAccountBalance(@Param("account") Account account);

}
