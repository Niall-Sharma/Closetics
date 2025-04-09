package com.example.closetics.outfits;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.R;
import com.example.closetics.UserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SelectClothesActivity extends AppCompatActivity {

    private Button doneButton;
    private ListView clothesList;
    private TextView noClothesText;
    private SelectClothesListAdapter adapter;

    private long outfitId;
    private JSONObject outfitJsonObject;
    private Set<Long> initialOutfitClothes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_clothes);

        doneButton = findViewById(R.id.select_clothes_done_button);
        clothesList = findViewById(R.id.select_clothes_list);
        noClothesText = findViewById(R.id.select_clothes_no_clothes_text);
        noClothesText.setVisibility(TextView.GONE);

        // Initialize the adapter with an empty list (data will be added later)
        adapter = new SelectClothesListAdapter(this, new ArrayList<>());
        clothesList.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        outfitId = -1;
        if (extras != null) {
            outfitId = extras.getLong("OUTFIT_ID", -1);
        }
        initialOutfitClothes = new HashSet<>();

        doneButton.setOnClickListener(v -> {
            Intent intent = new Intent(SelectClothesActivity.this, EditOutfitActivity.class);
            intent.putExtra("OUTFIT_ID", outfitId);
            startActivity(intent);
        });

        if (outfitId == -1) {
            Log.e("ID Error", "Outfit id is -1 in SelectClothesActivity");
        } else {
            getOutfitInfo();
        }
    }

    private void getOutfitInfo() {
        OutfitManager.getOutfitRequest(getApplicationContext(), outfitId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", "Successfully got outfit: " + response.toString());

                        outfitJsonObject = response;

                        try {
                            JSONArray clothes = response.getJSONArray("outfitItems");
                            for (int i = 0; i < clothes.length(); i++) {
                                try {
                                    // fill initial outfit clothes set
                                    long clothingId = clothes.getJSONObject(i).getLong("clothesId");
                                    initialOutfitClothes.add(clothingId);
                                } catch (JSONException e) {
                                    Log.e("JSON Error", "Cannot parse clothig: " + e.toString());
                                }
                            }
                        } catch (JSONException e1) {
                            Log.e("JSON Error", "Outfit doesn't contain outfitItems array: " + e1.toString());
                        }

                        populateClothesList(UserManager.getUserID(getApplicationContext()));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error getting outfit" + outfitId + ": " + error.toString());
                    }
                });
    }

    private void populateClothesList(long userId) {
        OutfitManager.getAllUserClothesRequest(getApplicationContext(), userId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", "Successfully got clothes of user " + userId + ": " + response.toString());

                        if (response.length() == 0) {
                            noClothesText.setVisibility(TextView.VISIBLE);
                        }

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject clothing = response.getJSONObject(i);

                                long clothingId = clothing.getLong("clothesId");
                                boolean initiallyChecked = initialOutfitClothes.contains(clothingId);
                                String name = clothing.has("itemName") ? clothing.getString("itemName") : null;
                                String color = clothing.has("color") ? clothing.getString("color") : null;
                                int typeId = clothing.has("clothingType") ? clothing.getInt("clothingType") : -1;
                                int specialTypeId = clothing.has("specialType") ? clothing.getInt("specialType") : -1;

                                // create and add item
                                SelectClothesListItem item = new SelectClothesListItem(getApplicationContext(), clothing,
                                        clothingId, outfitId, name, color, typeId, specialTypeId, initiallyChecked);
                                adapter.add(item);
                            } catch (JSONException e) {
                                Log.e("JSON Error", "Error parsing clothing: " + e.toString());
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error getting clothes of user " + userId + ": " + error.toString());
                    }
                });
    }
}