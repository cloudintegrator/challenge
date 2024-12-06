package com.recargapay.entity.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


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
    private LocalDateTime date;

    @Column(nullable = false)
    private double amount;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserEntity senderEntity;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private UserEntity receiverEntity;
}
