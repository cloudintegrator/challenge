package com.recargapay.entity.entity;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true,nullable = false)
    private String userName;

    @Column(unique = true,nullable = false)
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wallet_id")
    private WalletEntity walletEntity;

    @OneToMany(mappedBy = "senderEntity")
    private List<TransactionEntity> senderTransactionEntities;

    @OneToMany(mappedBy = "receiverEntity")
    private List<TransactionEntity> receiverTransactionEntities;
}
