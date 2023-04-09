package com.example.ebanktransactionsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    @Id
    private String iban;
    private Currency currency;

    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    private List<Transaction> transactions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "userId"
    )
    private User user;

    public void addTransaction(Transaction transaction){
        if(transactions == null) transactions = new ArrayList<>();
        transactions.add(transaction);
    }
}
