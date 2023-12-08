package com.example.finalproject;

import org.json.JSONArray;
import org.json.JSONObject;

public class CoinParser {

    public static void parseCoinList(String json) {
        try {
            JSONArray coinsArray = new JSONArray(json);

            for (int i = 0; i < coinsArray.length(); i++) {
                JSONObject coinObject = coinsArray.getJSONObject(i);

                // Extract data from the coinObject (e.g., name, symbol, etc.)
                String name = coinObject.getString("name");
                String symbol = coinObject.getString("symbol");

                // Use the data as needed (e.g., display in the UI)
                System.out.println("Name: " + name + ", Symbol: " + symbol);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}