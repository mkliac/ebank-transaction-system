package com.example.ebanktransactionsystem.controller;

import com.example.ebanktransactionsystem.service.TransactionService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/transaction")
@RequiredArgsConstructor
@SecurityScheme(
        name = "bearerAuth",
        scheme = "bearer",
        bearerFormat = "JWT",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER
)
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/get")
    public ResponseEntity<TransactionPageResponse> getTransactions(
            TransactionRequest request,
            @RequestHeader("Authorization") String token
    ) throws IOException {
        System.out.println(token);
        return ResponseEntity.ok(transactionService
                .getTransactions(request, token.substring(7)));
    }
}
