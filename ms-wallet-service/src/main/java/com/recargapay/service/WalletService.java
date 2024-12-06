package com.recargapay.service;

import com.recargapay.dto.AppDTO;
import com.recargapay.entity.TransactionEntity;
import com.recargapay.entity.UserEntity;
import com.recargapay.entity.WalletEntity;
import com.recargapay.repository.TransactionRepository;
import com.recargapay.repository.UserRepository;
import com.recargapay.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;


    public AppDTO.WalletResponseDTO createWallet(AppDTO.WalletRequestDTO requestDTO) {
        WalletEntity walletEntity = WalletEntity.builder()
                .amount(requestDTO.getAmount())
                .build();
        UserEntity userEntity = UserEntity.builder()
                .userName(requestDTO.getUserName())
                .email(requestDTO.getEmail())
                .walletEntity(walletEntity)
                .build();
        userRepository.save(userEntity);
        AppDTO.WalletResponseDTO responseDTO = AppDTO.WalletResponseDTO.builder()
                .walletId(walletEntity.getId())
                .amount(walletEntity.getAmount())
                .build();
        return responseDTO;
    }

    public AppDTO.WalletResponseDTO getBalance(String userName) {
        UserEntity userEntity = userRepository.findByUserName(userName);
        AppDTO.WalletResponseDTO responseDTO = AppDTO.WalletResponseDTO.builder()
                .userName(userName)
                .amount(userEntity.getWalletEntity().getAmount())
                .build();
        return responseDTO;
    }

    public AppDTO.WalletResponseDTO getHistoricalBalance(String userName, LocalDateTime dateTime) {
        UserEntity userEntity = userRepository.findByUserName(userName);
        if (null != userEntity) {
            List<TransactionEntity> transactionEntities = transactionRepository.findBySenderUserId(userEntity.getId());
            return AppDTO.WalletResponseDTO.builder().build();
        } else {
            return null;
        }
    }

    public AppDTO.WalletResponseDTO depositFunds(String userName, double amount) {
        UserEntity userEntity = userRepository.findByUserName(userName);
        if (null != userEntity) {
            WalletEntity walletEntity = userEntity.getWalletEntity();
            if (null != walletEntity) {
                walletEntity.setAmount(walletEntity.getAmount() + amount);
                walletRepository.save(walletEntity);
            }
            userEntity = userRepository.findByUserName(userName);
            AppDTO.WalletResponseDTO responseDTO = AppDTO.WalletResponseDTO.builder()
                    .amount(userEntity.getWalletEntity().getAmount())
                    .build();
            return responseDTO;
        } else {
            return null;
        }
    }

    @Transactional
    public void withdrawFunds(String userName, double amount) {
        UserEntity userEntity = userRepository.findByUserName(userName);
        if (null != userEntity) {
            WalletEntity walletEntity = userEntity.getWalletEntity();
            if (walletEntity.getAmount() < amount) {
                System.out.println("No amount");
            } else {
                walletEntity.setAmount(walletEntity.getAmount() - amount);
                walletRepository.save(walletEntity);
            }
        }
    }

    @Transactional
    public AppDTO.WalletResponseDTO transferFunds(String senderUser, String receiverUser, double amount) {
        UserEntity senderUserEntity = userRepository.findByUserName(senderUser);
        UserEntity receiverUserEntity = userRepository.findByUserName(receiverUser);
        AppDTO.WalletResponseDTO responseDTO = null;

        if (null != senderUserEntity && null != receiverUserEntity) {
            WalletEntity senderWalletEntity = senderUserEntity.getWalletEntity();
            WalletEntity receiverWalletEntity = receiverUserEntity.getWalletEntity();

            if (null != senderWalletEntity && null != receiverWalletEntity) {
                if (senderWalletEntity.getAmount() >= amount) {
                    withdrawFunds(senderUser, amount);
                    depositFunds(receiverUser, amount);

                    TransactionEntity transactionEntity = TransactionEntity.builder()
                            .amount(amount)
                            .date(LocalDateTime.now())
                            .senderEntity(senderUserEntity)
                            .receiverEntity(receiverUserEntity)
                            .build();
                    transactionRepository.save(transactionEntity);
                    responseDTO = AppDTO.WalletResponseDTO.builder()
                            .amount(senderUserEntity.getWalletEntity().getAmount())
                            .build();
                }
            }
        }
        return responseDTO;
    }
}
