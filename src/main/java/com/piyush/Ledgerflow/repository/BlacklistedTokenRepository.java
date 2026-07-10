package com.piyush.Ledgerflow.repository;


import com.piyush.Ledgerflow.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {

    boolean existsByToken(String token);

}
