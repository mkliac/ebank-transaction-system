package com.example.ebanktransactionsystem.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    private int year;
    private int month;
    private int pageSize = 10;
    private int pageNumber = 1;
    private String baseCurrency = "HKD";
}
