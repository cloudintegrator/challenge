package com.recargapay.controller;

import com.recargapay.dto.AppDTO;
import com.recargapay.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/create")
    public AppDTO.WalletResponseDTO createWallet(@RequestBody AppDTO.WalletRequestDTO requestDTO) {
        return walletService.createWallet(requestDTO);
    }

    @GetMapping("/{userName}/balance")
    public AppDTO.WalletResponseDTO getBalance(@PathVariable String userName) {
        return walletService.getBalance(userName);
    }

    @GetMapping("/{userId}/historical-balance")
    public AppDTO.WalletResponseDTO getHistoricalBalance(@PathVariable String userName, @RequestParam String dateTime) {
        return walletService.getHistoricalBalance(userName, LocalDateTime.parse(dateTime));
    }

    @PostMapping("/{userId}/deposit")
    public void depositFunds(@RequestBody AppDTO.DepositWithdrawRequestDTO requestDTO) {
        walletService.depositFunds(requestDTO.getUserName(), requestDTO.getAmount());
    }

    @PostMapping("/{userId}/withdraw")
    public void withdrawFunds(@RequestBody AppDTO.DepositWithdrawRequestDTO requestDTO) {
        walletService.withdrawFunds(requestDTO.getUserName(), requestDTO.getAmount());
    }

    @PostMapping("/transfer")
    public void transferFunds(@RequestBody AppDTO.TransferRequestDTO requestDTO) {
        walletService.transferFunds(requestDTO.getSenderUserName(), requestDTO.getReceiverUserName(), requestDTO.getAmount());
    }

}
