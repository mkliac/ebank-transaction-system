package com.example.ebanktransactionsystem.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    @NotNull(message = "Year is required")
    @Min(value = 0, message = "The value of year must be positive")
    private int year;
    @NotNull(message = "Month is required")
    @Min(value = 1, message = "The value of month must be greater than 0")
    @Max(value = 12, message = "The value of month must be smaller than 13")
    private int month;
    @Min(value = 1, message = "The value of pageSize must be greater than 0")
    private int pageSize = 10;
    @Min(value = 1, message = "The value of pageNumber must be greater than 0")
    private int pageNumber = 1;
    private String baseCurrency = "HKD";
}
