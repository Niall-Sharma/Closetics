package com.example.closetics.outfits;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.MainActivity;
import com.example.closetics.R;
import com.example.closetics.UserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OutfitsActivity extends AppCompatActivity {

    private Button addOutfitButton;
    private TextView noOutfitsText;
    private Button backButton;
    private ListView outfitsList;
    private OutfitsListAdapter adapter;

    private List<Long> outfitIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfits);

        addOutfitButton = findViewById(R.id.outfits_add_button);
        outfitsList = findViewById(R.id.outfits_list);
        noOutfitsText = findViewById(R.id.outfits_no_outfits_text);
        backButton = findViewById(R.id.outfits_back_button);

        // Initialize the adapter with an empty list (data will be added later)
        adapter = new OutfitsListAdapter(this, new ArrayList<>());
        outfitsList.setAdapter(adapter);

        addOutfitButton.setOnClickListener(v -> {
            Intent intent = new Intent(OutfitsActivity.this, EditOutfitActivity.class);
            startActivity(intent);
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(OutfitsActivity.this, MainActivity.class);
            startActivity(intent);
        });

        outfitsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // open EditOutfitActivity with outfit id as a parameter
//                Object listItem = outfitsList.getItemAtPosition(position);
                long selectedId = outfitIds.get(position);
                Intent intent = new Intent(OutfitsActivity.this, EditOutfitActivity.class);
                intent.putExtra("OUTFIT_ID", selectedId);
                startActivity(intent);
            }
        });

        populateOutfitsList(UserManager.getUserID(getApplicationContext()));
    }

    private void populateOutfitsList(long userId) {
        OutfitManager.getAllOutfitsRequest(getApplicationContext(), userId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());

                        // hide no outfits text if there are outfits
                        if (response.length() > 0) {
                            noOutfitsText.setVisibility(TextView.GONE);
                        }

                        outfitIds = new ArrayList<>();

                        // parse the JSON array and add data to the adapter
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject outfit = response.getJSONObject(i);

                                long id = outfit.getLong("outfitId");
                                String name = outfit.getString("outfitName");

                                // create list item, but not add it bc clothes aren't ready
                                OutfitsListItem item = new OutfitsListItem(outfit, id, name);

                                getAllClothesAndSave(id, item);
                            } catch (JSONException e) {
                                Log.e("JSON Error", e.toString());
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                });
    }

    private void getAllClothesAndSave(long outfitId, OutfitsListItem item) {
        OutfitManager.getAllOutfitItemsRequest(getApplicationContext(), outfitId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());

                        List<JSONObject> clothes = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject clothing = response.getJSONObject(i);
                                clothes.add(clothing);
                            } catch (JSONException e) {
                                Log.e("JSON Error", e.toString());
                            }
                        }

                        // set clothes list and add item to the adapter
                        item.setClothes(clothes);
                        adapter.add(item);

                        // save id for click handling
                        // add id at the same time as item to keep correct order in-between callbacks
                        outfitIds.add(outfitId);
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