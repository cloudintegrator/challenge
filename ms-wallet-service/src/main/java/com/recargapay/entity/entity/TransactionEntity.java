package com.recargapay.entity.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private double senderBalance;

    @Column(nullable = false)
    private double receiverBalance;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserEntity senderEntity;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private UserEntity receiverEntity;
}
