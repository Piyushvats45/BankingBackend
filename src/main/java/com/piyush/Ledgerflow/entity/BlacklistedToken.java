package com.piyush.Ledgerflow.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blacklisted_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlacklistedToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 1000)
    private String token;

}
