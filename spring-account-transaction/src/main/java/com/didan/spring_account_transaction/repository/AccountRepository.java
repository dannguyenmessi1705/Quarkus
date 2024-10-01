package com.didan.spring_account_transaction.repository;

import com.didan.spring_account_transaction.entity.AccountEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String> {
  Optional<AccountEntity> findAccountEntityByAccountNumber(String accountNumber);
}
