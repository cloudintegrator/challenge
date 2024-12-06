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
                .balance(requestDTO.getBalance())
                .build();
        UserEntity userEntity = UserEntity.builder()
                .userName(requestDTO.getUserName())
                .email(requestDTO.getEmail())
                .walletEntity(walletEntity)
                .build();
        userRepository.save(userEntity);
        AppDTO.WalletResponseDTO responseDTO = AppDTO.WalletResponseDTO.builder()
                .walletId(walletEntity.getId())
                .balance(walletEntity.getBalance())
                .build();
        return responseDTO;
    }

    public AppDTO.WalletResponseDTO getBalance(String userName) {
        UserEntity userEntity = userRepository.findByUserName(userName);
        AppDTO.WalletResponseDTO responseDTO = AppDTO.WalletResponseDTO.builder()
                .userName(userName)
                .balance(userEntity.getWalletEntity().getBalance())
                .build();
        return responseDTO;
    }

    public AppDTO.WalletResponseDTO getHistoricalBalance(String userName, LocalDateTime dateTime) {
        UserEntity userEntity = userRepository.findByUserName(userName);
        List<TransactionEntity> transactionEntities = transactionRepository.findByUserId(userEntity.getId());
        return AppDTO.WalletResponseDTO.builder().build();
    }

    public void depositFunds(String userName, double balance) {
        UserEntity userEntity = userRepository.findByUserName(userName);
        if (null != userEntity) {
            WalletEntity walletEntity = userEntity.getWalletEntity();
            if (null != walletEntity) {
                walletEntity.setBalance(walletEntity.getBalance() + balance);
                walletRepository.save(walletEntity);
            }
        }
    }
}
