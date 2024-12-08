package com.recargapay;

import com.recargapay.entity.UserEntity;
import com.recargapay.entity.WalletEntity;
import com.recargapay.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUserName() {
        // Arrange: Create and save a user with related entities
        WalletEntity wallet = WalletEntity.builder()
                .amount(1000.0)
                .build();

        UserEntity user = UserEntity.builder()
                .userName("testUser")
                .email("test@example.com")
                .walletEntity(wallet)
                .build();

        userRepository.save(user);

        // Act: Fetch the user by username
        UserEntity fetchedUser = userRepository.findByUserName("testUser");

        // Assert: Verify the fetched user and related entities
        assertThat(fetchedUser).isNotNull();
        assertThat(fetchedUser.getUserName()).isEqualTo("testUser");
        assertThat(fetchedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(fetchedUser.getWalletEntity()).isNotNull();
        assertThat(fetchedUser.getWalletEntity().getAmount()).isEqualTo(1000.0);
    }

    @Test
    void testFindByUserName_NoSuchUser() {
        UserEntity fetchedUser = userRepository.findByUserName("nonExistentUser");
        assertThat(fetchedUser).isNull();
    }
}
