package com.recargapay.service;

import com.recargapay.dto.AppDTO;
import com.recargapay.entity.TransactionEntity;
import com.recargapay.entity.UserEntity;
import com.recargapay.entity.WalletEntity;
import com.recargapay.repository.TransactionRepository;
import com.recargapay.repository.UserRepository;
import com.recargapay.repository.WalletRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class WalletService implements IWalletService {
    private static final Logger logger = LogManager.getLogger(WalletService.class);

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;


    public AppDTO.WalletResponseDTO createWallet(AppDTO.WalletRequestDTO requestDTO) {
        logger.info("********** Creating wallet with payload: " + requestDTO);
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
        logger.info("********** Checking balance for: " + userName);
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
        logger.info("********** Creating historical balance for: " + userName + " for date: " + date.toString());
        UserEntity userEntity = userRepository.findByUserName(userName);
        if (null != userEntity) {
            List<TransactionEntity> transactionEntities = transactionRepository.findByUserAndDate(userEntity.getId(), date);
            return AppDTO.WalletResponseDTO.builder()
                    .amount(transactionEntities.stream().findFirst().get().getSenderBalance())
                    .build();
        } else {
            throw new RuntimeException("User does not exist");
        }
    }

    public AppDTO.WalletResponseDTO depositFunds(String userName, double amount) {
        logger.info("********** Depositing funds for: " + userName + " with value: " + amount);
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
    public AppDTO.WalletResponseDTO withdrawFunds(String userName, double amount) {
        logger.info("********** Withdrawing amount of " + amount + " from: " + userName);
        UserEntity userEntity = userRepository.findByUserName(userName);
        if (null != userEntity) {
            WalletEntity walletEntity = userEntity.getWalletEntity();
            if (walletEntity.getAmount() < amount) {
                throw new RuntimeException("User does not have balance to withdraw");
            } else {
                walletEntity.setAmount(walletEntity.getAmount() - amount);
                walletRepository.save(walletEntity);
                AppDTO.WalletResponseDTO responseDTO = AppDTO.WalletResponseDTO.builder()
                        .amount(userEntity.getWalletEntity().getAmount())
                        .build();
                return responseDTO;
            }
        } else {
            throw new RuntimeException("User does not exist");
        }
    }

    @Transactional
    public AppDTO.WalletResponseDTO transferFunds(String senderUser, String receiverUser, double amount) {
        logger.info("********** Transferring");
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
