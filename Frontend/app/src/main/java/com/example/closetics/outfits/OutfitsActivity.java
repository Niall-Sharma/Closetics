package com.example.closetics.outfits;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.DashboardFragment;
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
    private Boolean setDashboard = false;

    private int outfitsNumber;
    private boolean isSetDayOutfit, isTomorrow;

    private List<Long> outfitIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfits);

        addOutfitButton = findViewById(R.id.outfits_add_button);
        outfitsList = findViewById(R.id.outfits_list);
        noOutfitsText = findViewById(R.id.outfits_no_outfits_text);
        backButton = findViewById(R.id.outfits_back_button);

        outfitsNumber = -1;

        // More Ashten's code
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isSetDayOutfit = true;
            isTomorrow = bundle.getBoolean("setTomorrow");
        } else {
            isSetDayOutfit = false;
        }

        // Initialize the adapter with an empty list (data will be added later)
        if (isSetDayOutfit) {
            adapter = new OutfitsListAdapter(this, new ArrayList<>(), this, isTomorrow);
        } else {
            adapter = new OutfitsListAdapter(this, new ArrayList<>(), this);
        }
        outfitsList.setAdapter(adapter);

        addOutfitButton.setOnClickListener(v -> {
            // user tier limit
            if (outfitsNumber == -1) return; // not loaded yet
            switch (UserManager.getUserTier(getApplicationContext())) {
                case 0:
                    if (outfitsNumber >= 15) {
                        Toast.makeText(getApplicationContext(), "Limit of 15 Outfits reached. Higher User Tier required to Add more.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    break;
                case 1:
                    if (outfitsNumber >= 30) {
                        Toast.makeText(getApplicationContext(), "Limit of 30 Outfits reached. Higher User Tier required to Add more.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    break;
            }

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


        //Ashten's code, outfits activity is reused in the dashboard with slight modifications!
        if (isSetDayOutfit) {
            String start  = "Choose ";
            setDashboard = true;
            addOutfitButton.setVisibility(View.INVISIBLE);
            if (isTomorrow) {
                String s = start + "Tomorrow's Outfit";
                noOutfitsText.setText(s);
                noOutfitsText.setVisibility(View.VISIBLE);
            }else {
                String s = start + "Today's Outfit";
                noOutfitsText.setText(s);
                noOutfitsText.setVisibility(View.VISIBLE);
            }
            //Clicking an outfit now sends that outfit ID to dashboard!
            outfitsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //Send outfit ID to main instead!
                    long selectedId = outfitIds.get(position);
                    if (isTomorrow) {
                        OutfitManager.saveTomorrowDailyOutfit(OutfitsActivity.this, selectedId);
                    }
                    else {
                        OutfitManager.saveCurrentDailyOutfit(OutfitsActivity.this, selectedId);
                        DashboardFragment.addWornToday(getApplicationContext(), selectedId);
                    }

                    Intent intent = new Intent(OutfitsActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });


            }



        populateOutfitsList(UserManager.getUserID(getApplicationContext()));
    }

    private void populateOutfitsList(long userId) {
        OutfitManager.getAllOutfitsRequest(getApplicationContext(), userId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());

                        outfitsNumber = response.length();

                        // hide no outfits text if there are outfits
                        if (response.length() > 0 && !setDashboard) {
                            noOutfitsText.setVisibility(TextView.GONE);
                        } else {
                            noOutfitsText.setVisibility(TextView.VISIBLE);
                        }

                        outfitIds = new ArrayList<>();

                        // parse the JSON array and add data to the adapter
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject outfit = response.getJSONObject(i);

                                long id = outfit.getLong("outfitId");
                                String name = outfit.getString("outfitName");
                                boolean isFavorite = outfit.getBoolean("favorite");

                                // create list item, but not add it bc clothes aren't ready
                                OutfitsListItem item = new OutfitsListItem(outfit, id, name, isFavorite);

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