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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Service
public class WalletService implements IWalletService {
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
        if (null != userEntity) {
            AppDTO.WalletResponseDTO responseDTO = AppDTO.WalletResponseDTO.builder()
                    .userName(userName)
                    .amount(userEntity.getWalletEntity().getAmount())
                    .build();
            return responseDTO;
        } else {
            throw new RuntimeException("User does not exist");
        }

    }

    public AppDTO.WalletResponseDTO getHistoricalBalance(String userName, LocalDate date) {
        UserEntity userEntity = userRepository.findByUserName(userName);
        if (null != userEntity) {
            List<TransactionEntity> transactionEntities = transactionRepository.findByUserAndDate(userEntity.getId(), date);
            return AppDTO.WalletResponseDTO.builder()
                    .amount(transactionEntities.stream().findFirst().get().getAmount())
                    .build();
        } else {
            throw new RuntimeException("User does not exist");
        }
    }

    public AppDTO.WalletResponseDTO depositFunds(String userName, double amount) {
        UserEntity userEntity = userRepository.findByUserName(userName);
        if (null != userEntity) {
            WalletEntity walletEntity = userEntity.getWalletEntity();
            if (null != walletEntity) {
                walletEntity.setAmount(walletEntity.getAmount() + amount);
                walletRepository.save(walletEntity);
                AppDTO.WalletResponseDTO responseDTO = AppDTO.WalletResponseDTO.builder()
                        .amount(userEntity.getWalletEntity().getAmount())
                        .build();
                return responseDTO;
            } else {
                throw new RuntimeException("User does not have a wallet.");
            }

        } else {
            throw new RuntimeException("User does not exist");
        }
    }

    @Transactional
    public void withdrawFunds(String userName, double amount) {
        UserEntity userEntity = userRepository.findByUserName(userName);
        if (null != userEntity) {
            WalletEntity walletEntity = userEntity.getWalletEntity();
            if (walletEntity.getAmount() < amount) {
                throw new RuntimeException("User does not have balance to withdraw");
            } else {
                walletEntity.setAmount(walletEntity.getAmount() - amount);
                walletRepository.save(walletEntity);
            }
        } else {
            throw new RuntimeException("User does not exist");
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
                            .date(LocalDate.now())
                            .time(LocalTime.now())
                            .senderEntity(senderUserEntity)
                            .senderBalance(senderUserEntity.getWalletEntity().getAmount())
                            .receiverEntity(receiverUserEntity)
                            .receiverBalance(receiverUserEntity.getWalletEntity().getAmount())
                            .build();
                    transactionRepository.save(transactionEntity);
                    responseDTO = AppDTO.WalletResponseDTO.builder()
                            .amount(senderUserEntity.getWalletEntity().getAmount())
                            .build();
                } else {
                    throw new RuntimeException("Sender does not have enough balance");
                }
            } else {
                throw new RuntimeException("Either sender or receiver's wallet does not exist");
            }
        } else {
            throw new RuntimeException("Either of sender or receiver does not exist");
        }
        return responseDTO;
    }
}
