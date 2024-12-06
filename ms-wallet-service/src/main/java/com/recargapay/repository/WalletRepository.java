package com.recargapay.repository;

import com.recargapay.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<WalletEntity,Long> {
}
