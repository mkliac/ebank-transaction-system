package com.example.ebanktransactionsystem.service;

import com.example.ebanktransactionsystem.controller.TransactionRequest;
import com.example.ebanktransactionsystem.controller.TransactionPageResponse;
import com.example.ebanktransactionsystem.controller.TransactionResponse;
import com.example.ebanktransactionsystem.model.Account;
import com.example.ebanktransactionsystem.model.Transaction;
import com.example.ebanktransactionsystem.model.User;
import com.example.ebanktransactionsystem.repository.AccountRepository;
import com.example.ebanktransactionsystem.repository.TransactionRepository;
import com.example.ebanktransactionsystem.repository.UserRepository;
import com.example.ebanktransactionsystem.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log
public class TransactionService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final ExchangeService exchangeService;

    public TransactionPageResponse getTransactions(
            TransactionRequest request,
            String token
    ) throws IOException {

        String userId = jwtService.extractUserId(token);
        User user = userRepository.getUserByUserId(userId).get();
        List<Account> accounts = user.getAccounts();

        log.info("get relevant transactions from user:" +
                userId + " with request: " + request);

        Pageable pageConfig = PageRequest.of(
                request.getPageNumber() - 1,
                request.getPageSize());

        List<Transaction> transactions =
                transactionRepository.findByIbansFkWithinYearAndMonth(
                        accounts.stream().map(a -> a.getIban())
                                .collect(Collectors.toList()),
                        request.getYear(), request.getMonth(),
                        pageConfig).getContent();


        List<TransactionResponse> transactionResponses = createTransactionResponses(
                transactions
        );

        Map<String, Float> values =
                exchangeService.getDebitsAndCreditsFromTransactions(transactions, request.getBaseCurrency());

        TransactionPageResponse transactionPageResponse =
                TransactionPageResponse.builder()
                        .transactions(transactionResponses)
                        .debits(values.get("debits"))
                        .credits(values.get("credits"))
                        .baseCurrency(request.getBaseCurrency())
                        .build();

        return transactionPageResponse;
    }

    private List<TransactionResponse> createTransactionResponses(
        List<Transaction> transactions
    ){

        log.info("prepare response from transaction with size:" +
                transactions.size());

        ArrayList<TransactionResponse> container = new ArrayList<>();

        for(final Transaction transaction : transactions){

            String iban = transaction.getIban();
            Account account = accountRepository.getAccountByIban(iban);

            TransactionResponse transactionResponse = new TransactionResponse().builder()
                    .uuid(transaction.getUuid())
                    .amount(account.getCurrency().name() + " " +
                            transaction.getAmount())
                    .iban(account.getIban())
                    .date(transaction.getDate())
                    .description(transaction.getDescription())
                    .build();

            container.add(transactionResponse);
        }

        return container;
    }
}
