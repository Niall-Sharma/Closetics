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
import com.example.closetics.MainActivity;
import com.example.closetics.R;
import com.example.closetics.UserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditOutfitActivity extends AppCompatActivity {
    private final String URL_GET_OUTFIT = MainActivity.SERVER_URL + "/getOutfit/"; // + {{outfitId}}
    private final String URL_GET_ALL_OUTFIT_ITEMS = MainActivity.SERVER_URL + "/getAllOutfitItems/"; // + {{outfitId}}
    private final String URL_DELETE_OUTFIT = MainActivity.SERVER_URL + "/deleteOutfit/"; // + {{outfitId}}
    private final String URL_UPDATE_OUTFIT = MainActivity.SERVER_URL + "/updateOutfit";
    private final String URL_CREATE_OUTFIT = MainActivity.SERVER_URL + "/createOutfit";

    private EditText outfitNameEdit;
    private Button addClothingButton;
    private Button deleteButton;
    private Button doneButton;
    private ListView clothesList;
    private EditOutfitClothingListAdapter adapter;

    private JSONObject jsonObject;
    private long outfitId;
    private String outfitName;

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
        outfitId = extras.getLong("OUTFIT_ID", -1);
//        outfitNewName = extras.getString("OUTFIT_NAME", null);
        outfitName = null;
        jsonObject = null;

        addClothingButton.setOnClickListener(v -> {
            saveChanges();

            Intent intent = new Intent(EditOutfitActivity.this, SelectClothesActivity.class);
            intent.putExtra("OUTFIT_ID", outfitId);
            startActivity(intent);
        });

        deleteButton.setOnClickListener(v -> {
            deleteOutfit();
        });

        doneButton.setOnClickListener(v -> {
            saveChanges();

            Intent intent = new Intent(EditOutfitActivity.this, OutfitsActivity.class);
            startActivity(intent);
        });

        if (outfitId == -1) {
            createOutfit("New Outfit");
        } else {
            populateOutfitInfo();
        }
    }

    private void populateOutfitInfo() {
        OutfitManager.getOutfitRequest(getApplicationContext(), outfitId, URL_GET_OUTFIT,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());

                        jsonObject = response;

                        // parse the JSON array and add data to the adapter
                        try {
                            outfitName = response.has("outfitName") ? response.getString("outfitName") : null;
                            if (outfitName == null || outfitName.isBlank()) {
                                outfitName = "No name";
                            }
                            // set edit to the most recent name
                            outfitNameEdit.setText(outfitName);

                            populateClothesList();
                        } catch (JSONException e) {
                            Log.e("JSON Error", e.toString());
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

    private void populateClothesList() {
        OutfitManager.getAllOutfitItemsRequest(getApplicationContext(), outfitId, URL_GET_ALL_OUTFIT_ITEMS,
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

    private void createOutfit(String name) {
        OutfitManager.createOutfitRequest(getApplicationContext(), UserManager.getUserID(getApplicationContext()), name, URL_CREATE_OUTFIT,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());

                        jsonObject = response;
                        outfitName = name;
                        outfitNameEdit.setText(outfitName);

                        // parse the JSON array and add data to the adapter
                        try {
                            outfitId = response.getLong("outfitId");
                        } catch (JSONException e) {
                            Log.e("JSON Error", e.toString());
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

    private void saveChanges() {
        try {
            // update name
            jsonObject.put("outfitName", outfitNameEdit.getText());
        } catch (JSONException e) {
            Log.e("JSON Error", e.toString());
        }

        OutfitManager.updateOutfitRequest(getApplicationContext(), jsonObject, URL_UPDATE_OUTFIT,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());

                        jsonObject = response;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                });
    }

    private void deleteOutfit() {
        OutfitManager.deleteOutfitRequest(getApplicationContext(), outfitId, URL_DELETE_OUTFIT,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());

                        // switch activities only if deleted successfully
                        Intent intent = new Intent(EditOutfitActivity.this, OutfitsActivity.class);
                        startActivity(intent);
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