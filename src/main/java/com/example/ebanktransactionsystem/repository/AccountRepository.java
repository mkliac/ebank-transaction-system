package com.example.ebanktransactionsystem.repository;

import com.example.ebanktransactionsystem.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Account getAccountByIban(String iban);
}
