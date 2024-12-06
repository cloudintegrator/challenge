package com.recargapay;
import com.recargapay.entity.TransactionEntity;
import com.recargapay.entity.UserEntity;
import com.recargapay.entity.WalletEntity;
import com.recargapay.repository.TransactionRepository;
import com.recargapay.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindBySenderUserId() {
        // Arrange: Create users
        UserEntity sender = UserEntity.builder()
                .userName("senderUser")
                .email("sender@example.com")
                .walletEntity(WalletEntity.builder().amount(0).build()) // Assigning empty wallet for now
                .build();

        UserEntity receiver = UserEntity.builder()
                .userName("receiverUser")
                .email("receiver@example.com")
                .walletEntity(WalletEntity.builder().amount(0).build())
                .build();

        userRepository.save(sender);
        userRepository.save(receiver);

        // Create a transaction entity associated with the sender and receiver
        TransactionEntity transaction = TransactionEntity.builder()
                .date(LocalDateTime.now())
                .amount(100.0)
                .senderEntity(sender)
                .receiverEntity(receiver)
                .build();

        transactionRepository.save(transaction);

        // Act: Call the custom query to retrieve transactions by sender user ID
        List<TransactionEntity> transactions = transactionRepository.findBySenderUserId(sender.getId());

        // Assert: Verify the list is not empty and contains the correct transaction
        assertThat(transactions).isNotEmpty();
        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getSenderEntity().getUserName()).isEqualTo("senderUser");
        assertThat(transactions.get(0).getReceiverEntity().getUserName()).isEqualTo("receiverUser");
        assertThat(transactions.get(0).getAmount()).isEqualTo(100.0);
    }

    @Test
    void testFindBySenderUserId_NoTransactions() {
        // Arrange: Create a user with no transactions
        UserEntity sender = UserEntity.builder()
                .userName("senderUser")
                .email("sender@example.com")
                .walletEntity(WalletEntity.builder().amount(0).build())
                .build();
        userRepository.save(sender);

        // Act: Call the custom query for a user with no transactions
        List<TransactionEntity> transactions = transactionRepository.findBySenderUserId(sender.getId());

        // Assert: Verify the result is empty as no transactions were made
        assertThat(transactions).isEmpty();
    }
}
