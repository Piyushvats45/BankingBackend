package com.piyush.Ledgerflow.entity;

import com.piyush.Ledgerflow.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status = AccountStatus.ACTIVE;

    @Column(nullable = false)
    private String currency = "INR";

    @OneToMany(mappedBy = "account")
    private List<Ledger> ledgerEntries = new ArrayList<>();

    @OneToMany(mappedBy = "fromAccount")
    private List<Transaction> sentTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "toAccount")
    private List<Transaction> receivedTransactions = new ArrayList<>();

}
