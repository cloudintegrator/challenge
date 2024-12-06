package com.recargapay.controller;

import com.recargapay.dto.AppDTO;
import com.recargapay.service.WalletService;
import com.recargapay.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/create")
    public ApiResponse<AppDTO.WalletResponseDTO> createWallet(@RequestBody AppDTO.WalletRequestDTO requestDTO) {
        return ApiResponse.success(walletService.createWallet(requestDTO), "Success");
    }

    @GetMapping("/{userName}/balance")
    public ApiResponse<AppDTO.WalletResponseDTO> getBalance(@PathVariable String userName) {
        return ApiResponse.success(walletService.getBalance(userName), "Success");
    }

    @GetMapping("/{userName}/historical-balance")
    public void getHistoricalBalance(@PathVariable String userName, @RequestParam String dateTime) {
        walletService.getHistoricalBalance(userName, LocalDateTime.parse(dateTime));
    }

    @PostMapping("/{userId}/deposit")
    public ApiResponse<AppDTO.WalletResponseDTO> depositFunds(@RequestBody AppDTO.DepositWithdrawRequestDTO requestDTO) {
        return ApiResponse.success(walletService.depositFunds(requestDTO.getUserName(), requestDTO.getAmount()), "Success");
    }

    @PostMapping("/{userId}/withdraw")
    public void withdrawFunds(@RequestBody AppDTO.DepositWithdrawRequestDTO requestDTO) {
        walletService.withdrawFunds(requestDTO.getUserName(), requestDTO.getAmount());
    }

    @PostMapping("/transfer")
    public ApiResponse<AppDTO.WalletResponseDTO> transferFunds(@RequestBody AppDTO.TransferRequestDTO requestDTO) {
        return ApiResponse.success(walletService.transferFunds(requestDTO.getSenderUserName(), requestDTO.getReceiverUserName(), requestDTO.getAmount()), "Success");
    }

}
