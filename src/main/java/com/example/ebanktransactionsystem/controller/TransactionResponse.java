package com.example.ebanktransactionsystem.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private UUID uuid;
    private String amount;
    private String iban;
    private LocalDate date;
    private String description;
}
