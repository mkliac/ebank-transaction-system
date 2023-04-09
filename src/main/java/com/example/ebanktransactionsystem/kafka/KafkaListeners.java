package com.example.ebanktransactionsystem.kafka;

import com.example.ebanktransactionsystem.model.Account;
import com.example.ebanktransactionsystem.model.Transaction;
import com.example.ebanktransactionsystem.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log
public class KafkaListeners {

    private final AccountRepository accountRepository;

    @KafkaListener(topics = "transaction", groupId = "myGroupId", containerFactory = "kafkaObjectListener")
    void listener(ConsumerRecord<String, Transaction> record){

        log.info("consumed transaction with UUID:" +
                record.key());
        Transaction transaction = record.value();
        String iban = transaction.getIban();
        Account account = accountRepository.getAccountByIban(iban);
        account.addTransaction(transaction);

        accountRepository.save(account);
    }
}
