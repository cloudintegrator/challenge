package com.recargapay.service;

import com.recargapay.dto.AppDTO;
import com.recargapay.entity.TransactionEntity;
import com.recargapay.entity.UserEntity;
import com.recargapay.entity.WalletEntity;
import com.recargapay.repository.TransactionRepository;
import com.recargapay.repository.UserRepository;
import com.recargapay.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {
    @Mock
    private WalletRepository walletRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private WalletService walletService;

    private UserEntity userEntity;
    private WalletEntity walletEntity;

    @BeforeEach
    void setUp() {
        walletEntity = WalletEntity.builder().amount(1000.0).build();
        userEntity = UserEntity.builder()
                .userName("testUser")
                .email("test@example.com")
                .walletEntity(walletEntity)
                .build();
    }

    @Test
    void testCreateWallet() {
        // Arrange
        AppDTO.WalletRequestDTO requestDTO = AppDTO.WalletRequestDTO.builder()
                .userName("testUser")
                .email("test@example.com")
                .amount(1000.0)
                .build();

        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        // Act
        AppDTO.WalletResponseDTO responseDTO = walletService.createWallet(requestDTO);

        // Assert
        assertThat(responseDTO).isNotNull();
        //assertThat(responseDTO.getWalletId()).isNotNull();
        assertThat(responseDTO.getAmount()).isEqualTo(1000.0);
    }

    @Test
    void testGetBalance() {
        // Arrange
        when(userRepository.findByUserName("testUser")).thenReturn(userEntity);

        // Act
        AppDTO.WalletResponseDTO responseDTO = walletService.getBalance("testUser");

        // Assert
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getAmount()).isEqualTo(1000.0);
        assertThat(responseDTO.getUserName()).isEqualTo("testUser");
    }

    @Test
    void testGetBalance_UserNotFound() {
        // Arrange
        when(userRepository.findByUserName("nonExistingUser")).thenReturn(null);

        // Act
        Throwable thrown = catchThrowable(() -> walletService.getBalance("nonExistingUser"));

        // Assert
        assertThat(thrown).isInstanceOf(RuntimeException.class).hasMessage("User does not exist");
    }

    @Test
    void testDepositFunds() {
        // Arrange
        when(userRepository.findByUserName("testUser")).thenReturn(userEntity);

        // Act
        AppDTO.WalletResponseDTO responseDTO = walletService.depositFunds("testUser", 500.0);

        // Assert
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getAmount()).isEqualTo(1500.0);
    }

    @Test
    void testDepositFunds_UserNotFound() {
        // Arrange
        when(userRepository.findByUserName("nonExistingUser")).thenReturn(null);

        // Act
        Throwable thrown = catchThrowable(() -> walletService.depositFunds("nonExistingUser", 500.0));

        // Assert
        assertThat(thrown).isInstanceOf(RuntimeException.class).hasMessage("User does not exist");
    }

    @Test
    void testWithdrawFunds() {
        // Arrange
        when(userRepository.findByUserName("testUser")).thenReturn(userEntity);

        // Act
        walletService.withdrawFunds("testUser", 500.0);

        // Assert
        assertThat(userEntity.getWalletEntity().getAmount()).isEqualTo(500.0);
        verify(walletRepository).save(walletEntity); // Verifying if wallet save was called
    }

    @Test
    void testWithdrawFunds_InsufficientBalance() {
        // Arrange
        when(userRepository.findByUserName("testUser")).thenReturn(userEntity);

        // Act
        Throwable thrown = catchThrowable(() -> walletService.withdrawFunds("testUser", 1500.0));

        // Assert
        assertThat(thrown).isInstanceOf(RuntimeException.class).hasMessage("User does not have balance to withdraw");
    }

    @Test
    void testTransferFunds_Success() {
        // Arrange
        UserEntity receiverUserEntity = UserEntity.builder().userName("receiverUser").email("receiver@example.com").walletEntity(WalletEntity.builder().amount(0.0).build()).build();
        when(userRepository.findByUserName("testUser")).thenReturn(userEntity);
        when(userRepository.findByUserName("receiverUser")).thenReturn(receiverUserEntity);

        when(walletRepository.save(any(WalletEntity.class))).thenReturn(walletEntity);

        // Act
        AppDTO.WalletResponseDTO responseDTO = walletService.transferFunds("testUser", "receiverUser", 500.0);

        // Assert
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getAmount()).isEqualTo(500.0);
        verify(transactionRepository).save(any(TransactionEntity.class));
    }

    @Test
    void testTransferFunds_InsufficientBalance() {
        // Arrange
        UserEntity receiverUserEntity = UserEntity.builder().userName("receiverUser").email("receiver@example.com").walletEntity(WalletEntity.builder().amount(500.0).build()).build();
        when(userRepository.findByUserName("testUser")).thenReturn(userEntity);
        when(userRepository.findByUserName("receiverUser")).thenReturn(receiverUserEntity);

        // Act
        Throwable thrown = catchThrowable(() -> walletService.transferFunds("testUser", "receiverUser", 1500.0));

        // Assert
        assertThat(thrown).isInstanceOf(RuntimeException.class).hasMessage("Sender does not have enough balance");
    }

    @Test
    void testTransferFunds_UserNotFound() {
        // Arrange
        when(userRepository.findByUserName("nonExistingUser")).thenReturn(null);

        // Act
        Throwable thrown = catchThrowable(() -> walletService.transferFunds("nonExistingUser", "receiverUser", 500.0));

        // Assert
        assertThat(thrown).isInstanceOf(RuntimeException.class).hasMessage("Either of sender or receiver does not exist");
    }
}
