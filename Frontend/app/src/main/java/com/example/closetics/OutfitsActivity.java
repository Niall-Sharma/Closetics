package com.example.closetics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OutfitsActivity extends AppCompatActivity {
    private final String GET_OUTFITS_URL = MainActivity.SERVER_URL + "/outfits";

    private Button addOutfitButton;
    private ListView outfitsList;
    private OutfitsListAdapter adapter;

    private ArrayList<Long> outfitsIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfits);

        addOutfitButton = findViewById(R.id.outfits_add_button);
        outfitsList = findViewById(R.id.outfits_list);

        // Initialize the adapter with an empty list (data will be added later)
        adapter = new OutfitsListAdapter(this, new ArrayList<>());
        outfitsList.setAdapter(adapter);

        addOutfitButton.setOnClickListener(v -> {

        });

        populateOutfitsList(UserManager.getUserID(getApplicationContext()));
    }

    private void populateOutfitsList(long userId) {
        OutfitManager.getOutfitsRequest(getApplicationContext(), userId, GET_OUTFITS_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());

                        outfitsIds = new ArrayList<>();

                        // parse the JSON array and add data to the adapter
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject outfit = response.getJSONObject(i);

                                // save outfit id for click handler
                                long id = outfit.getLong("outfitId");
                                outfitsIds.add(id);

                                String name = outfit.getString("outfitName");
                                JSONArray clothes = outfit.getJSONArray("outfitItems");
                                List<String> clothesNames = new ArrayList<>();

                                // add all outfit's clothes names to the list
                                for (int j = 0; j < clothes.length(); j++) {
                                    try {
                                        JSONObject clothing = clothes.getJSONObject(j);
                                        String clothingName = clothing.getString("clothingItemName");
                                        clothesNames.add(clothingName); // make this http req by id if needed
                                    } catch (JSONException e) {
                                        Log.e("JSON Error", e.toString());
                                    }
                                }

                                // create list item and add it to the adapter
                                OutfitsListItem item = new OutfitsListItem(name, clothesNames);
                                adapter.add(item);

                            } catch (JSONException e) {
                                Log.e("JSON Error", e.toString());
                            }
                        }

                        outfitsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // open edit outfit activity with outfit id as a parameter
//                                Object listItem = outfitsList.getItemAtPosition(position);
                                long selectedId = outfitsIds.get(position);
                                Intent intent = new Intent(OutfitsActivity.this, EditOutfitActivity.class);
                                intent.putExtra("OUTFIT_ID", selectedId);
                                startActivity(intent);
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                });
    }
}