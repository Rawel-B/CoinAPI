package com.example.finalproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView coinListView;
    private List<String> coinNames;
    private ArrayAdapter<String> adapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coinListView = findViewById(R.id.coinListView);
        coinNames = new ArrayList<>();
        searchView = findViewById(R.id.searchView);

        // Perform the network request in a background thread
        new CoinFetchTask().execute();

        // Set up search functionality
        setupSearchView();
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform the search based on the query
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the list as the user types
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });
    }

    private void performSearch(String query) {
        List<String> filteredList = new ArrayList<>();
        for (String coinName : coinNames) {
            // Compare the query with each coin name (case-insensitive)
            if (coinName.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(coinName);
            }
        }

        // Update the UI with the filtered list
        updateUI(filteredList);
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
                parseCoinList(result);

                if (!coinNames.isEmpty()) {
                    updateUI();
                } else {
                    // Handle the case when the list is empty (e.g., display a message)
                    Toast.makeText(MainActivity.this, "No coins found", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Handle the case when the API call fails (e.g., display an error message)
                Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
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
    }

    // Ensure this method is defined in your MainActivity class
    private void updateUI(List<String> dataList)
    {
        adapter = new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1, dataList);
        coinListView.setAdapter(adapter);
    }
    private void updateUI(){
        updateUI(coinNames);
    }
}
