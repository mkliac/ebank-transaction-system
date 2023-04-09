package com.example.ebanktransactionsystem.repository;

import com.example.ebanktransactionsystem.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query(value = "select t from Transaction t where t.iban in ?1" +
            " and year(t.date) = ?2 and month(t.date) = ?3",
            countQuery = "select count(*) from Transaction t where t.iban in ?1" +
                    " and year(t.date) = ?2 and month(t.date) = ?3")
    Page<Transaction> findByIbansFkWithinYearAndMonth(List<String> ibans, int year, int month, Pageable pageable);
}
