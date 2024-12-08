package com.recargapay;

import com.recargapay.entity.UserEntity;
import com.recargapay.entity.WalletEntity;
import com.recargapay.repository.UserRepository;
import com.recargapay.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class WalletRepositoryTest {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveWallet() {
        WalletEntity wallet = WalletEntity.builder()
                .amount(1000.0)
                .build();

        UserEntity user = UserEntity.builder()
                .userName("testUser")
                .email("test@example.com")
                .walletEntity(wallet)
                .build();

        userRepository.save(user);
        WalletEntity savedWallet=user.getWalletEntity();

        // Assert: Verify the wallet is saved
        assertThat(savedWallet).isNotNull();
        assertThat(savedWallet.getId()).isNotNull();
        assertThat(savedWallet.getAmount()).isEqualTo(1000.0);
    }

    @Test
    void testFindWalletById() {
        // Arrange: Save a wallet
        WalletEntity wallet = WalletEntity.builder()
                .amount(2000.0)
                .build();
        WalletEntity savedWallet = walletRepository.save(wallet);

        // Act: Retrieve the wallet by ID
        Optional<WalletEntity> retrievedWallet = walletRepository.findById(savedWallet.getId());

        // Assert: Verify the wallet is retrieved
        assertThat(retrievedWallet).isPresent();
        assertThat(retrievedWallet.get().getAmount()).isEqualTo(2000.0);
    }

    @Test
    void testDeleteWallet() {
        // Arrange: Save a wallet
        WalletEntity wallet = WalletEntity.builder()
                .amount(500.0)
                .build();
        WalletEntity savedWallet = walletRepository.save(wallet);

        // Act: Delete the wallet
        walletRepository.deleteById(savedWallet.getId());

        // Assert: Verify the wallet is deleted
        Optional<WalletEntity> deletedWallet = walletRepository.findById(savedWallet.getId());
        assertThat(deletedWallet).isEmpty();
    }

    @Test
    void testSaveWalletWithUserEntity() {
        // Arrange: Create a WalletEntity
        WalletEntity wallet = WalletEntity.builder()
                .amount(3000.0)
                .build();

        // Create a UserEntity associated with the wallet
        UserEntity user = UserEntity.builder()
                .userName("testUser")
                .email("test@example.com")
                .walletEntity(wallet)
                .build();

        // Act: Save the wallet
        WalletEntity savedWallet = walletRepository.save(wallet);

        // Assert: Verify the wallet is saved
        assertThat(savedWallet).isNotNull();
        assertThat(savedWallet.getId()).isNotNull();
        assertThat(savedWallet.getAmount()).isEqualTo(3000.0);
    }
}
