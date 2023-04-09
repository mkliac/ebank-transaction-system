package com.example.ebanktransactionsystem.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionPageResponse {
    private List<TransactionResponse> transactions;
    private float debits;
    private float credits;
    private String baseCurrency;
}
