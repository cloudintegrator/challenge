package com.recargapay.repository;

import com.recargapay.entity.UserEntity;
import com.recargapay.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    UserEntity findByUsername(String userName);

}
