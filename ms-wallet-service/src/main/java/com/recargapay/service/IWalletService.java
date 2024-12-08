package com.recargapay.service;

import com.recargapay.dto.AppDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


public interface IWalletService {

    AppDTO.WalletResponseDTO createWallet(AppDTO.WalletRequestDTO requestDTO);
    AppDTO.WalletResponseDTO getBalance(String userName);
    AppDTO.WalletResponseDTO getHistoricalBalance(String userName, LocalDate date);
    AppDTO.WalletResponseDTO depositFunds(String userName, double amount);
    void withdrawFunds(String userName, double amount);
    AppDTO.WalletResponseDTO transferFunds(String senderUser, String receiverUser, double amount);
}
