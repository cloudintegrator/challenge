package com.recargapay.controller;

import com.recargapay.dto.AppDTO;
import com.recargapay.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(MockitoExtension.class)
public class WalletControllerTest {

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
    }

    @Test
    void testCreateWallet() throws Exception {
        // Arrange
        AppDTO.WalletRequestDTO requestDTO = AppDTO.WalletRequestDTO.builder()
                .userName("testUser")
                .email("test@example.com")
                .amount(1000.0)
                .build();

        AppDTO.WalletResponseDTO responseDTO = AppDTO.WalletResponseDTO.builder()
                .walletId(1L)
                .amount(1000.0)
                .build();

        when(walletService.createWallet(any(AppDTO.WalletRequestDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/wallet/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\": \"testUser\", \"email\": \"test@example.com\", \"amount\": 1000.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.walletId").value(1))
                .andExpect(jsonPath("$.data.amount").value(1000.0))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void testGetBalance() throws Exception {
        // Arrange
        AppDTO.WalletResponseDTO responseDTO = AppDTO.WalletResponseDTO.builder()
                .userName("testUser")
                .amount(1000.0)
                .build();

        when(walletService.getBalance("testUser")).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/wallet/testUser/balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userName").value("testUser"))
                .andExpect(jsonPath("$.data.amount").value(1000.0))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void testGetHistoricalBalance() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/wallet/testUser/historical-balance")
                        .param("date", "2024-12-06"))
                .andExpect(status().isOk());
    }

    @Test
    void testDepositFunds() throws Exception {
        // Arrange
        AppDTO.WalletResponseDTO responseDTO = AppDTO.WalletResponseDTO.builder()
                .amount(1500.0)
                .build();

        AppDTO.DepositWithdrawRequestDTO requestDTO = AppDTO.DepositWithdrawRequestDTO.builder()
                .userName("testUser")
                .amount(500.0)
                .build();

        when(walletService.depositFunds("testUser", 500.0)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/wallet/testUser/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\": \"testUser\", \"amount\": 500.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.amount").value(1500.0))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void testWithdrawFunds() throws Exception {
        // Arrange
        AppDTO.DepositWithdrawRequestDTO requestDTO = AppDTO.DepositWithdrawRequestDTO.builder()
                .userName("testUser")
                .amount(500.0)
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/wallet/testUser/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\": \"testUser\", \"amount\": 500.0}"))
                .andExpect(status().isOk());
    }

    @Test
    void testTransferFunds() throws Exception {
        // Arrange
        AppDTO.WalletResponseDTO responseDTO = AppDTO.WalletResponseDTO.builder()
                .amount(1000.0)
                .build();

        AppDTO.TransferRequestDTO requestDTO = AppDTO.TransferRequestDTO.builder()
                .senderUserName("testUser")
                .receiverUserName("receiverUser")
                .amount(500.0)
                .build();

        when(walletService.transferFunds("testUser", "receiverUser", 500.0)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/wallet/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"senderUserName\": \"testUser\", \"receiverUserName\": \"receiverUser\", \"amount\": 500.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.amount").value(1000.0))
                .andExpect(jsonPath("$.message").value("Success"));
    }
}
