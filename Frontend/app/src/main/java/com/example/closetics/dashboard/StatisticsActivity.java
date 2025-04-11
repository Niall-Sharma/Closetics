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
import androidx.cardview.widget.CardView;
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
import com.example.closetics.outfits.OutfitsActivity;

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

    private CardView card1;
    private CardView card2;

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

        card1 = findViewById(R.id.cardView);
        card2 = findViewById(R.id.cardView2);



        setMostExpensiveOutfit();
        setMostWornOutfit();
        setMostExpensiveClothing();
        setMostWornClothingItem();
        setTotalOutfitsCount();
        setTotalClosetValueAndTotalClothing();




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
                setCardsInvisible();
                showFragment(CLOTHES_STATS_TAG, fragment);




            }
        });
        outfitStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichButton.setText("Outfit Stats");
                Fragment fragment = new OutfitStatsFragment(allOutfitStatsObjects);
                setCardsInvisible();
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
        OutfitManager.getAllOutfitsRequest(this, UserManager.getUserID(this), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
               totalOutfitCount.setText(String.valueOf(response.length()));
                ArrayList<JSONObject> statsObjects = new ArrayList<>();

                for (int i =0; i < response.length(); i++){
                   try {
                       JSONObject object = response.getJSONObject(i);
                       JSONObject stats = object.getJSONObject("clothingStats");
                       statsObjects.add(stats);
                   } catch (JSONException e) {
                       Log.e("exception", e.toString());
                   }

               }
                setAllOutfitStatsObjects(statsObjects);
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
    private void setTotalClosetValueAndTotalClothing(){
        ClothesManager.getClothingByUserRequest(this, UserManager.getUserID(this), MainActivity.SERVER_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                int totalClosetValue =0;

                ArrayList<JSONObject> statsObjects = new ArrayList<>();
                for (int i =0; i<response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Log.d("object", object.toString());
                        JSONObject statObject = object.getJSONObject("clothingStats");
                        //Add the json object to the arrayList
                        statsObjects.add(statObject);
                        Log.d("statObject", statObject.toString());

                        /*
                        Check if price is not null
                         */
                        if (!object.getString("price") .equals("null")){
                            int price = object.getInt("price");
                            totalClosetValue +=price;
                        }

                    } catch (JSONException e) {
                        Log.e("exception", e.toString());
                    }
                }
                setAllClothingStatsObjects(statsObjects);
                totalClothingItems.setText(String.valueOf(response.length()));
                totalClosetCount.setText(String.valueOf(totalClosetValue));


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
    private void setCardsInvisible(){
        card1.setVisibility(View.GONE);
        card2.setVisibility(View.GONE);
    }
    private void setCardsVisible(){
        card1.setVisibility(View.VISIBLE);
        card2.setVisibility(View.VISIBLE);
    }
    private void setMostExpensiveOutfit(){
        StatisticsManager.mostExpensiveOutfitRequest(this, UserManager.getUserID(this), MainActivity.SERVER_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Most expensive", response.toString());
                try {
                    getOutfit(response.getLong("outfitId"), response.getString("totalPrice"));

                } catch (JSONException e) {
                    Log.e("exception", e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Most expensive clothing request", error.toString());

            }
        });
    }

    private void getOutfit(long outfitId, String price){
        OutfitManager.getOutfitRequest(this, outfitId, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("outfit", response.toString());
                    String s = "Name: " + response.getString("outfitName") +"\nPrice: " + price;
                    mostExpensiveOutfit.setText(s);
                } catch (JSONException e) {
                    Log.e("exception", e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
    private void setMostWornOutfit(){
        StatisticsManager.mostWornOutfitRequest(this, UserManager.getUserID(this), MainActivity.SERVER_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("check", response.toString());
                try {
                    String name = response.getString("outfitName");
                    JSONObject object = response.getJSONObject("outfitStats");
                    String timesWorn = object.getString("timesWorn");
                    String s = "Name: " + name + "\nTimes Worn: " + timesWorn;
                    mostWornOutfit.setText(s);
                } catch (JSONException e) {
                    Log.e("exception", e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void setMostExpensiveClothing(){
        StatisticsManager.mostExpensiveClothingRequest(this, UserManager.getUserID(this), MainActivity.SERVER_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("expensive clothing", response.toString());
                try {
                    String name = response.getString("itemName");
                    String price = response.getString("price");
                    if (price.equals("null")){
                        price = "none";
                    }
                    String s = "Name: " + name + "\nPrice: " + price;
                    mostExpensiveClothing.setText(s);
                } catch (JSONException e) {
                    Log.e("exception", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        });
    }
    private void setMostWornClothingItem(){
        StatisticsManager.mostWornClothingRequest(this, UserManager.getUserID(this), MainActivity.SERVER_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("most worn clothing", response.toString());
                try {
                    String name = response.getString("itemName");
                    JSONObject object = response.getJSONObject("clothingStats");
                    String timesWorn = object.getString("timesWorn");
                    String s = "Name: " + name + "\nTimes Worn: " + timesWorn;
                    mostWornClothingItem.setText(s);
                } catch (JSONException e) {
                    Log.e("exception", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }



}
