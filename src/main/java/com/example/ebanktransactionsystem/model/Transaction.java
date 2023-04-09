package com.example.ebanktransactionsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    @Id
    private UUID uuid;
    private LocalDate date;
    private int amount;
    private String description;
    private String iban;
}
