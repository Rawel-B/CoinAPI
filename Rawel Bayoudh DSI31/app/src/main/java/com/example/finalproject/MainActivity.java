package com.example.finalproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView coinListView;
    private List<String> coinNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coinListView = findViewById(R.id.coinListView);
        coinNames = new ArrayList<>();

        // Perform the network request in a background thread
        new CoinFetchTask().execute();
    }

    private class CoinFetchTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return CoinGeckoApi.getCoinList();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                // Parse the JSON data and populate the list
                parseCoinList(result);

                // Update the UI with the list of coin names
                updateUI();
            }
        }

        private void parseCoinList(String json) {
            try {
                JSONArray coinsArray = new JSONArray(json);

                for (int i = 0; i < coinsArray.length(); i++) {
                    JSONObject coinObject = coinsArray.getJSONObject(i);

                    // Extract data from the coinObject (e.g., name, symbol, etc.)
                    String name = coinObject.getString("name");
                    coinNames.add(name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void updateUI() {
            // Create an ArrayAdapter to display the coin names
            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                    android.R.layout.simple_list_item_1, coinNames);

            // Set the adapter on the ListView
            coinListView.setAdapter(adapter);
        }
    }
}
