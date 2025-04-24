package com.example.closetics.outfits;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.R;
import com.example.closetics.UserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditOutfitActivity extends AppCompatActivity {

    private EditText outfitNameEdit;
    private Button addClothingButton;
    private Button deleteButton;
    private Button doneButton;
    private ListView clothesList;
    private EditOutfitClothingListAdapter adapter;

    private JSONObject outfitJsonObject;
    private long outfitId;
    private String outfitName;
    private boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_outfit);

        outfitNameEdit = findViewById(R.id.edit_outfit_name_edit);
        addClothingButton = findViewById(R.id.edit_outfit_add_clothing_button);
        deleteButton = findViewById(R.id.edit_outfit_delete_button);
        doneButton = findViewById(R.id.edit_outfit_done_button);
        clothesList = findViewById(R.id.edit_outfit_clothes_list);

        // Initialize the adapter with an empty list (data will be added later)
        adapter = new EditOutfitClothingListAdapter(this, new ArrayList<>());
        clothesList.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        outfitId = -1;
        if (extras != null) {
            outfitId = extras.getLong("OUTFIT_ID", -1);
        }
        outfitName = null;
        outfitJsonObject = null;

        addClothingButton.setOnClickListener(v -> {
            updateStoredJsonAndSaveChanges();

            Intent intent = new Intent(EditOutfitActivity.this, SelectClothesActivity.class);
            intent.putExtra("OUTFIT_ID", outfitId);
            startActivity(intent);
        });

        deleteButton.setOnClickListener(v -> {
            deleteOutfit();
        });

        doneButton.setOnClickListener(v -> {
            updateStoredJsonAndSaveChanges();

            Intent intent = new Intent(EditOutfitActivity.this, OutfitsActivity.class);
            startActivity(intent);
        });

        if (outfitId == -1) {
            createOutfit("New Outfit");
        } else {
            populateOutfitInfo();
            populateClothesList();
        }
    }

    private void populateOutfitInfo() {
        OutfitManager.getOutfitRequest(getApplicationContext(), outfitId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());

                        outfitJsonObject = response;

                        // parse the JSON array and add data to the adapter
                        try {
                            outfitName = response.has("outfitName") ? response.getString("outfitName") : "";
                            // set edit to the most recent name
                            outfitNameEdit.setText(outfitName);
                        } catch (JSONException e) {
                            Log.e("JSON Error", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error getting outfit: " + error.toString());
                    }
                });
    }

    private void populateClothesList() {
        OutfitManager.getAllOutfitItemsRequest(getApplicationContext(), outfitId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject clothing = response.getJSONObject(i);

                                long clothingId = clothing.getLong("clothesId");
                                String name = clothing.has("itemName") ? clothing.getString("itemName") : null;
                                String color = clothing.has("color") ? clothing.getString("color") : null;
                                int typeId = clothing.has("clothingType") ? clothing.getInt("clothingType") : -1;
                                int specialTypeId = clothing.has("specialType") ? clothing.getInt("specialType") : -1;

                                // create and add item
                                EditOutfitClothingListItem item = new EditOutfitClothingListItem(getApplicationContext(), clothing,
                                        clothingId, outfitId, name, color, typeId, specialTypeId);
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
                        Log.e("Volley Error", "Error getting clothes: " + error.toString());
                    }
                });
    }

    private void updateStoredJsonAndSaveChanges() {
        OutfitManager.getOutfitRequest(getApplicationContext(), outfitId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", "Successfully got outfit " + outfitId + " to save: " + response.toString());

                        outfitJsonObject = response;

                        saveChanges();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error getting outfit " + outfitId + " to save: " + error.toString());
                    }
                });
    }

    private void createOutfit(String name) {
        OutfitManager.createOutfitRequest(getApplicationContext(), UserManager.getUserID(getApplicationContext()), name,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", "Successfully created outfit: " + response.toString());

                        outfitJsonObject = response;
                        outfitName = name;
                        outfitNameEdit.setText(outfitName);

                        // parse the JSON array and add data to the adapter
                        try {
                            outfitId = response.getLong("outfitId");
                        } catch (JSONException e) {
                            Log.e("JSON Error", "Error parsing new outfit: " + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error creating outfit: " + error.toString());
                    }
                });
    }

    private void saveChanges() {
        // can't save if there is no outfit
        if (outfitId == -1 || outfitName == null) {
            Log.e("EditOutfitActivity", "Error when saving outfit: outfitId == -1 or outfitName == null");
            return;
        }

        JSONObject updateObject = new JSONObject();

        try {
            // clone object
            updateObject = new JSONObject(outfitJsonObject.toString());

            // update name
            updateObject.put("outfitName", outfitNameEdit.getText().toString());

            // remove creation date
            updateObject.remove("creationDate");

        } catch (JSONException e) {
            Log.e("JSON Error", outfitNameEdit.getText().toString() + " : " + e.toString());
            return;
        }

        OutfitManager.updateOutfitRequest(getApplicationContext(), updateObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", "Successfully saved outfit: " + response.toString());

                        outfitJsonObject = response;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error saving outfit: " + error.toString());
                    }
                });
    }

    private void deleteOutfit() {
        OutfitManager.deleteOutfitRequest(getApplicationContext(), outfitId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley Response", "Successfully deleted outfit: " + response.toString());

                        // switch activities only if deleted successfully
                        Intent intent = new Intent(EditOutfitActivity.this, OutfitsActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error deleting outfit: " + error.toString());
                    }
                });
    }
}