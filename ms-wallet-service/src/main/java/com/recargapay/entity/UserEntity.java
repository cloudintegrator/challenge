package com.recargapay.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wallet_id")
    private WalletEntity walletEntity;

    @OneToMany(mappedBy = "sender")
    private List<TransactionEntity> sentTransactions;

    @OneToMany(mappedBy = "receiver")
    private List<TransactionEntity> receivedTransactions;

}
