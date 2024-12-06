package com.recargapay.repository;

import com.recargapay.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    @Query("SELECT t FROM TransactionEntity t WHERE t.senderEntity.id = :user_id")
    List<TransactionEntity> findBySenderUserId(Long user_id);
}
