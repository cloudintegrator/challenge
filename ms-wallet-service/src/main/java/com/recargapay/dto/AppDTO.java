package com.recargapay.dto;

import lombok.Builder;
import lombok.Data;

public class AppDTO {
    @Builder
    @Data
    public static class WalletRequestDTO {
        private String userName;
        private String email;
        private double amount;
    }
    @Data
    @Builder
    public static class WalletResponseDTO {
        private Long walletId;
        private String userName;
        private double amount;
    }

    @Data
    @Builder
    public static class DepositWithdrawRequestDTO {
        private String userName;
        private double amount;
    }

    @Data
    @Builder
    public static class TransferRequestDTO {
        private String senderUserName;
        private String receiverUserName;
        private double amount;
    }


}
