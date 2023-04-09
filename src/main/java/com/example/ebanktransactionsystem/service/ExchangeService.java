package com.example.ebanktransactionsystem.service;

import com.example.ebanktransactionsystem.model.Transaction;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log
public class ExchangeService {

    private String urlEndpoint = "https://v6.exchangerate-api.com/v6/7349c0709df43de245f8579a/latest/";

    private JsonObject getExchangeRate(String baseCurrency) throws IOException {

        log.info("get exchange rates with baseCurrency:" + baseCurrency);

        URL url = new URL(urlEndpoint + baseCurrency);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream responseStream = connection.getInputStream();

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject)jsonParser.parse(
                new InputStreamReader(responseStream, "UTF-8"));
        return jsonObject.get("conversion_rates").getAsJsonObject();
    }

    public Map<String, Float> getDebitsAndCreditsFromTransactions(
            List<Transaction> transactions, String baseCurrency) throws IOException {

        JsonObject exchangeRates = getExchangeRate(baseCurrency);

        log.info("get debits and credits from " +
                transactions.size() + " in " + baseCurrency);

        float debits = 0;
        float credits = 0;

        for(Transaction transaction : transactions){

            float amount = transaction.getAmount();
            String description = transaction.getDescription();
            String currencyType =
                    description.substring(description.length()-3);

            float exchangeRate =
                    exchangeRates.get(currencyType).getAsFloat();
            if(amount > 0) debits += amount / exchangeRate;
            else credits += amount / exchangeRate;
        }

        HashMap<String, Float> values = new HashMap<>();
        values.put("debits", debits);
        values.put("credits", credits);
        return values;
    }
}
