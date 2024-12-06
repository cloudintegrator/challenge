package com.recargapay.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

public class AppDTO {
    @Builder
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class WalletRequestDTO {
        private String userName;
        private String email;
        private double amount;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class WalletResponseDTO {
        private Long walletId;
        private String userName;
        private double amount;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DepositWithdrawRequestDTO {
        private String userName;
        private double amount;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TransferRequestDTO {
        private String senderUserName;
        private String receiverUserName;
        private double amount;
    }


}
