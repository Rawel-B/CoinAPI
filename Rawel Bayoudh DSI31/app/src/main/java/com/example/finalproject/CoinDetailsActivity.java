package com.example.finalproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CoinDetailsActivity extends AppCompatActivity {

    private TextView textCoinName, textCoinId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_details);

        textCoinName = findViewById(R.id.textCoinName);
        textCoinId = findViewById(R.id.textCoinId);

        String coinId = getIntent().getStringExtra("COIN_ID");

        new FetchCoinDetailsTask().execute(coinId);
    }

    private class FetchCoinDetailsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... coinIds) {
            if (coinIds.length == 0) {
                return null;
            }

            String coinId = coinIds[0];
            String apiUrl = "https://api.coingecko.com/api/v3/coins/" + coinId;

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try (InputStreamReader reader = new InputStreamReader(urlConnection.getInputStream())) {
                    int data = reader.read();
                    StringBuilder result = new StringBuilder();
                    while (data != -1) {
                        result.append((char) data);
                        data = reader.read();
                    }
                    return result.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonResult) {
            if (jsonResult != null) {
                JsonObject jsonObject = new Gson().fromJson(jsonResult, JsonObject.class);

                String name = jsonObject.getAsJsonPrimitive("name").getAsString();
                String id = jsonObject.getAsJsonPrimitive("id").getAsString();
                JsonObject marketDataObject = jsonObject.getAsJsonObject("market_data");

                JsonObject currentPriceObject = marketDataObject.getAsJsonObject("current_price");

                // Extract individual currencies from current_price
                double aed = currentPriceObject.getAsJsonPrimitive("aed").getAsDouble();
                double ars = currentPriceObject.getAsJsonPrimitive("ars").getAsDouble();

                textCoinName.setText(name);
                textCoinId.setText("AED: " + aed + "\nARS: " + ars);

            } else {
            }
        }
    }
}
