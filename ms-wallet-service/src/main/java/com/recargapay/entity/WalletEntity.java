package com.recargapay.entity;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "wallets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double balance;

    @OneToOne(mappedBy = "walletEntity", fetch = FetchType.EAGER)
    private UserEntity userEntity;

}
