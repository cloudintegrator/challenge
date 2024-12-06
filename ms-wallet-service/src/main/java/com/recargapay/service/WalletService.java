package com.recargapay.service;

import com.recargapay.entity.UserEntity;
import com.recargapay.entity.WalletEntity;
import com.recargapay.repository.UserRepository;
import com.recargapay.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    public Long createWallet() {
        WalletEntity walletEntity = WalletEntity.builder()
                .balance(100)
                .build();
        UserEntity userEntity = UserEntity.builder()
                .userName("Anupam")
                .email("anupamgogoi@gmail.com")
                .walletEntity(walletEntity)
                .build();
        userRepository.save(userEntity);
        return walletEntity.getId();
    }

    public void checkWallet(Long id){
        Optional<WalletEntity> walletEntity=walletRepository.findById(id);
        System.out.println(walletEntity.get().getBalance());
    }
}
