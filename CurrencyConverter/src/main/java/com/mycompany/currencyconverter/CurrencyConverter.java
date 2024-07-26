/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.currencyconverter;

import java.math.BigDecimal;
import java.util.Scanner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import java.io.IOException;

public class CurrencyConverter {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        try {
            System.out.println("Type currency to convert from: ");
            String convertFrom = scan.nextLine().toUpperCase(); 
            System.out.println("Type currency to convert to: ");
            String convertTo = scan.nextLine().toUpperCase(); 
            System.out.println("Type quantity to convert: ");
            BigDecimal quantity = scan.nextBigDecimal();

            String apiKey = "9e7bdfce97d0f13dfbbb7c40"; 
            String urlString = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + convertFrom;

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(urlString)
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String stringResponse = response.body().string();
                System.out.println("API Response: " + stringResponse);

                JSONObject jsonObject = new JSONObject(stringResponse);
                

                // Check if the response contains an error
                if (!jsonObject.getString("result").equals("success")) {
                    System.out.println("Error: " + jsonObject.getString("error-type"));
                    return;
                }

                JSONObject ratesObject = jsonObject.getJSONObject("conversion_rates");

                // Check if the currency conversion exists in the rates
                if (!ratesObject.has(convertTo)) {
                    System.out.println("Error: The conversion rate for " + convertTo + " is not available.");
                    return;
                }

                BigDecimal rate = ratesObject.getBigDecimal(convertTo);
                BigDecimal result = rate.multiply(quantity);
                System.out.println("Result: " + result);
            }
        } catch (IOException e) {
            System.out.println("Error fetching conversion rate: " + e.getMessage());
        } finally {
            scan.close();
        }
    }
}

