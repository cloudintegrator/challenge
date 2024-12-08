package com.recargapay.controller;

import com.recargapay.dto.AppDTO;
import com.recargapay.service.IWalletService;
import com.recargapay.utils.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private static final Logger logger= LogManager.getLogger(WalletController.class);
    @Autowired
    private IWalletService walletService;

    @PostMapping("/create")
    public ApiResponse<AppDTO.WalletResponseDTO> createWallet(@RequestBody AppDTO.WalletRequestDTO requestDTO) {
        logger.debug("******* Controller ********");
        return ApiResponse.success(walletService.createWallet(requestDTO), "Success");
    }

    @GetMapping("/{userName}/balance")
    public ApiResponse<AppDTO.WalletResponseDTO> getBalance(@PathVariable String userName) {
        return ApiResponse.success(walletService.getBalance(userName), "Success");
    }

    @GetMapping("/{userName}/historical-balance")
    public ApiResponse<AppDTO.WalletResponseDTO> getHistoricalBalance(@PathVariable String userName, @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        return ApiResponse.success(walletService.getHistoricalBalance(userName, localDate),"Success");
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
