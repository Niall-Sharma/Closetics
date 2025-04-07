package com.example.closetics.dashboard;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.MainActivity;
import com.example.closetics.R;
import com.example.closetics.UserManager;
import com.example.closetics.clothes.ClothesManager;
import com.example.closetics.clothes.ViewClothesFragment;
import com.example.closetics.outfits.OutfitManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {
    private ImageButton back;
    private Button outfitStats;
    private Button clothesStats;
    private Button overallStats;
    private TextView whichButton;

    private TextView totalOutfitCount;
    private TextView totalClosetCount;
    private TextView totalClothingItems;
    private TextView mostWornOutfit;
    private TextView mostWornClothingItem;
    private TextView mostExpensiveOutfit;
    private TextView mostExpensiveClothing;

    private ArrayList<JSONObject> allOutfitStatsObjects;
    private ArrayList<JSONObject> allClothingStatsObjects;

    private final String CLOTHES_STATS_TAG = "clothes_stats_fragment";
    private final String OUTFITS_STATS_TAG = "outfits_stats_fragment";





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context activity = this;
        setContentView(R.layout.activity_statistics);

        back = findViewById(R.id.backButton);
        whichButton = findViewById(R.id.categoryText);
        overallStats = findViewById(R.id.overallButton);
        clothesStats = findViewById(R.id.clothesButton);
        outfitStats = findViewById(R.id.outfitButton);
        whichButton.setText("Overall Stats");

        /*
        Overall stats textViews
         */
        totalOutfitCount = findViewById(R.id.totalOutfitsCount);
        totalClosetCount = findViewById(R.id.totalClosetCount);
        totalClothingItems = findViewById(R.id.clothingCount);
        mostWornOutfit = findViewById(R.id.wornOutfit);
        mostWornClothingItem = findViewById(R.id.wornClothing);
        mostExpensiveOutfit = findViewById(R.id.expensiveOutfit);
        mostExpensiveClothing = findViewById(R.id.expensiveClothing);
        /*
        Set the views
         */
        setTotalOutfitsCount();
        setTotalClosetValue();




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
            }
        });
        clothesStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichButton.setText("Clothes Stats");
                Fragment fragment = new ClothesStatsFragment(allClothingStatsObjects);
                showFragment(CLOTHES_STATS_TAG, fragment);



            }
        });
        outfitStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichButton.setText("Outfit Stats");
                Fragment fragment = new OutfitStatsFragment(allOutfitStatsObjects);
                showFragment(OUTFITS_STATS_TAG, fragment);
            }
        });
        overallStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichButton.setText("Overall Stats");

            }
        });


    }

    private void showFragment(String tag, Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.stats_categories_container, fragment, tag);
        transaction.commit();
    }

    /*
    Note: Come back to these!
     */
    private void setTotalOutfitsCount(){
        OutfitManager.getAllOutfitsRequest(this, UserManager.getUserID(this), MainActivity.SERVER_URL + "/getAllUserOutfits/", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int greatestPrice = 0;
                int wornOutfitCount = 0;
                ArrayList<JSONObject> statsObjects = new ArrayList<>();

                for (int i =0; i<response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Log.d("outfitObject", object.toString());
                        JSONObject statObject = object.getJSONObject("outfitStats");
                       Log.d("outfitStats", statObject.toString());
                       int wornOutfit = statObject.getInt("timesWorn");
                       statsObjects.add(statObject);

                       if (wornOutfit>wornOutfitCount){
                           wornOutfitCount = wornOutfit;
                       }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                totalOutfitCount.setText(String.valueOf(response.length()));
                mostExpensiveOutfit.setText(String.valueOf(greatestPrice));
                mostWornOutfit.setText(String.valueOf(wornOutfitCount));
                setAllOutfitStatsObjects(statsObjects);



                Log.d("Outfit count", String.valueOf(response.length()));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Outfit count error", error.toString());
            }
        });

    }
    /*
    Figure out a better way to find total closet value without having to fully scan all clothes
     */
    private void setTotalClosetValue(){
        ClothesManager.getClothingByUserRequest(this, UserManager.getUserID(this), MainActivity.SERVER_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int totalPrice =0;
                int greatestPrice =0;
                int greatestWornItem =0;

                ArrayList<JSONObject> statsObjects = new ArrayList<>();
                for (int i =0; i<response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Log.d("object", object.toString());
                        JSONObject statObject = object.getJSONObject("clothingStats");
                        //Add the json object to the arrayList
                        statsObjects.add(statObject);
                        Log.d("statObject", statObject.toString());
                        int timesWorn = statObject.getInt("timesWorn");

                        if (timesWorn>greatestWornItem){
                            greatestWornItem = timesWorn;
                        }
                        /*
                        Check if price is not null
                         */
                        if (!object.getString("price") .equals("null")){
                            int price = object.getInt("price");
                            totalPrice +=price;
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                Log.d("response length", String.valueOf(response.length()));

                //Total clothing items
                totalClothingItems.setText(String.valueOf(response.length()));

                //Total closet count
                totalClosetCount.setText(String.valueOf(totalPrice));

                //Most expensive clothing item

                mostExpensiveClothing.setText(String.valueOf(greatestPrice));

                //Set the clothing stats arrayList

                setAllClothingStatsObjects(statsObjects);
                //Set the worn clothing count
                mostWornClothingItem.setText(String.valueOf(greatestWornItem));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Clothing total price error", error.toString());
            }
        });
    }


    private void setAllClothingStatsObjects(ArrayList<JSONObject> objects){
        allClothingStatsObjects = objects;
    }
    private void setAllOutfitStatsObjects(ArrayList<JSONObject> objects){
        allOutfitStatsObjects = objects;
    }



}
