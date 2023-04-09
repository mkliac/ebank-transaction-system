package com.example.ebanktransactionsystem;

import com.example.ebanktransactionsystem.model.*;
import com.example.ebanktransactionsystem.service.AuthenticationService;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
@Log
public class EbankTransactionSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankTransactionSystemApplication.class, args);
	}

	@Bean
	CommandLineRunner run(
			AuthenticationService authenticationService,
			PasswordEncoder passwordEncoder,
			KafkaTemplate<String, Transaction> kafkaTemplate) {
		return args -> {
			authenticationService.resetUserDb();
			User user = initMyUser(passwordEncoder);
			authenticationService.addUser(user);

			produceRandomTransactionsForEachAc(
					user.getAccounts(),
					100,
					LocalDate.of(2022, 1, 1),
					500,
					500,
					kafkaTemplate
			);
		};
	}

	private User initMyUser(PasswordEncoder passwordEncoder){

		log.info("initialize a user");

		ArrayList<Account> accounts = new ArrayList<>();

		for (Currency currency : Currency.values()) {
			Account account = Account.builder()
					.iban(currency.name() + "0-0000-0000-0000-0000-0")
					.currency(currency)
					.build();

			accounts.add(account);
		}
		User user = User.builder()
				.userId("0123456789")
				.password(passwordEncoder.encode("1234"))
				.role(Role.USER)
				.accounts(accounts)
				.build();

		return user;
	}

	private void produceRandomTransactionsForEachAc(
			List<Account> accounts,
			int transactionPerAc,
			LocalDate startDate,
			int maxWithdrawAmount,
			int maxDepositAmount,
			KafkaTemplate<String, Transaction> kafkaTemplate
			){

		log.info("produce and send random transactions for " +
				accounts.size() + " accounts");

		for(Account account : accounts) {

			Currency currency = account.getCurrency();

			for (int i = 0; i < transactionPerAc; i++) {

				long minDay = startDate.toEpochDay();
				long maxDay = LocalDate.now().toEpochDay();
				long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
				int amount = ThreadLocalRandom.current().nextInt(-maxWithdrawAmount, maxDepositAmount);

				Transaction transaction = Transaction.builder()
						.uuid(UUID.randomUUID())
						.date(LocalDate.ofEpochDay(randomDay))
						.amount(amount)
						.description((amount >= 0 ? "Deposit " : "Withdraw ") + currency.name())
						.iban(account.getIban())
						.build();
				kafkaTemplate.send("transaction", String.valueOf(transaction.getUuid()), transaction);
			}
		}
	}
}
