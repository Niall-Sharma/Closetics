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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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


/**
 * Activity that handles displaying the statistics related to the user's outfits and clothing items.
 * It displays both overall stats and category-specific stats (Clothing, Outfits).
 */
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
    private TextView warmClothing;
    private TextView coldClothing;
    private TextView warmOutfit;
    private TextView coldOutfit;

    private CardView card1;
    private CardView card2;

    private ArrayList<ClothingStatItem> allOutfitStatsObjects;
    private ArrayList<ClothingStatItem> allClothingStatsObjects;

    private final String CLOTHES_STATS_TAG = "Clothes Stats";
    private final String OUTFITS_STATS_TAG = "Outfit Stats";





    /**
     * Initializes the activity and sets up the UI components and event listeners.
     * It fetches and displays various statistics such as the number of outfits, total closet value,
     * most worn items, and weather-specific clothing stats.
     *
     * @param savedInstanceState The saved instance state from a previous instance of the activity.
     */
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
        warmClothing = findViewById(R.id.warm_clothing);
        coldClothing = findViewById(R.id.cold_clothing);
        warmOutfit = findViewById(R.id.warm_outfit);
        coldOutfit = findViewById(R.id.cold_outfit);

        card1 = findViewById(R.id.cardView);
        card2 = findViewById(R.id.cardView2);



        setMostExpensiveOutfit();
        setMostWornOutfit();
        setMostExpensiveClothing();
        setMostWornClothingItem();
        setTotalOutfitsCount();
        setTotalClosetValueAndTotalClothing(this);

        setColdClothing();
        setWarmClothing();
        setColdOutfit();
        setWarmOutfit();






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
                whichButton.setText(CLOTHES_STATS_TAG);
                Fragment fragment = new ClothesStatsFragment(allClothingStatsObjects);
                setCardsInvisible();
                showFragment(CLOTHES_STATS_TAG, fragment);



            }
        });
        outfitStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichButton.setText(OUTFITS_STATS_TAG);
                Fragment fragment = new OutfitStatsFragment(allOutfitStatsObjects);
                setCardsInvisible();
                showFragment(OUTFITS_STATS_TAG, fragment);
            }
        });
        overallStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = (String)whichButton.getText();
                whichButton.setText("Overall Stats");
                deleteFragment(tag);
                setCardsVisible();
            }
        });


    }

    /**
     * Displays a fragment inside the statistics container based on the provided tag and fragment.
     *
     * @param tag The tag of the fragment.
     * @param fragment The fragment to display.
     */
    private void showFragment(String tag, Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.stats_categories_container, fragment, tag);
        transaction.commit();
    }
    /**
     * Removes a fragment based on the given tag.
     *
     * @param tag The tag of the fragment to remove.
     */
    private void deleteFragment(String tag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);

        if (fragment != null){
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
    }


    /**
     * Fetches and sets the total number of outfits from the server.
     */
    private void setTotalOutfitsCount(){
        OutfitManager.getAllOutfitsRequest(this, UserManager.getUserID(this), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
               totalOutfitCount.setText(String.valueOf(response.length()));
                allOutfitStatsObjects = new ArrayList<>();

                for (int i =0; i < response.length(); i++){
                   try {
                       JSONObject object = response.getJSONObject(i);
                       Log.d("outfit check", object.toString());
                       JSONObject stats = object.getJSONObject("outfitStats");
                       String name = object.getString("outfitName");
                       long outfitId = object.getLong("outfitId");
                       ClothingStatItem c = new ClothingStatItem(stats, name, outfitId);
                       if (UserManager.getUserTier(getApplicationContext()) == 2){
                           setOutfitCostPerWear(outfitId, c);
                       }
                       else{
                           allOutfitStatsObjects.add(c);
                       }
                       //statsObjects.add(c);
                   } catch (JSONException e) {
                       Log.e("exception", e.toString());
                   }

               }
                //allOutfitStatsObjects = statsObjects;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Outfit count error", error.toString());
            }
        });
    }
    /**
     * Fetches and sets the total closet value and total number of clothing items from the server.
     */
    private void setTotalClosetValueAndTotalClothing(Context context){
        ClothesManager.getClothingByUserRequest(context, UserManager.getUserID(this), MainActivity.SERVER_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                int totalClosetValue =0;

                allClothingStatsObjects = new ArrayList<>();
                for (int i =0; i<response.length(); i++){
                    JSONObject object = response.optJSONObject(i);
                    try {
                        if (object == null){
                            continue;
                        }

                        JSONObject statObject = object.optJSONObject("clothingStats");
                        if (statObject == null){
                            continue;
                        }

                        Log.d("object", object.toString());

                        /*
                        Grab the numberOfOutfitsIn field
                         */
                        String name = object.getString("itemName");
                        long clothesId = object.getLong("clothesId");



                        ClothingStatItem statItem = new ClothingStatItem(statObject, name, clothesId);
                        getImage(context,clothesId, statItem);

                        //Add the json object to the arrayList
                        Log.d("statObject", object.toString());

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

    private void getImage(Context context, long clothesId, ClothingStatItem statItem){
        ClothesManager.getImageByClothing(context, clothesId, MainActivity.SERVER_URL, new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {
                statItem.setImage(response);
                setNumberOfOutfitsIn(clothesId, statItem);


                //allClothingStatsObjects.add(statItem);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                statItem.setImage(null);
                setNumberOfOutfitsIn(clothesId, statItem);
                //allClothingStatsObjects.add(statItem);
                Log.d("Error", error.toString());

            }
        });
    }


    /**
     * Sets the activities two cards invisible
     */
    private void setCardsInvisible(){
        card1.setVisibility(View.GONE);
        card2.setVisibility(View.GONE);
    }

    /**
     * Sets the activities two cards as visible
     */
    private void setCardsVisible(){
        card1.setVisibility(View.VISIBLE);
        card2.setVisibility(View.VISIBLE);
    }

    /**
     * Fetches and sets the user's most expensive outfit
     */
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

    /**
     * Fetches a users particular outfit via the outfit ID, used for setting most expensive outfit.
     * @param outfitId long outfit ID corresponding to a unique outfit
     * @param price String price
     */
    private void getOutfit(long outfitId, String price){
        OutfitManager.getOutfitRequest(this, outfitId, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("outfit", response.toString());
                    String formattedPrice = String.format("%.5s", price);  // Ensure max 5 characters
                    String s = "Name: " + response.getString("outfitName") +"\nPrice: " + formattedPrice;
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

    /**
     * Fetches and sets a user's most worn outfit.
     */
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

    /**
     * Fetches and sets a users most expensive piece of clothing.
     */
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

    /**
     * Fetches and sets a users most worn outfit.
     */
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

    /**
     * If the backend sends a null response, the provided textView with "none" and log
     * the error.
     * @param error the volley error
     * @param text a particular TextView
     */
   private void weatherRequestNull(VolleyError error, TextView text){
       if (error.networkResponse == null){
           String s = "none";
           text.setText(s);
       }
       Log.e("error", error.toString());
   }

    /**
     * Fetches and sets the number of clothing items suitable for cold weather.
     */
    private void setColdClothing() {
        StatisticsManager.coldestAverageClothingRequest(this, UserManager.getUserID(this), MainActivity.SERVER_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject statItem = response.getJSONObject("clothingStats");
                    String itemName = response.getString("itemName");
                    if (itemName.equals("null")){
                        itemName = "None";
                    }
                    Long highTemp = statItem.getLong("avgLowTemp");
                    String s = itemName + "\n" + String.valueOf(highTemp);
                    coldClothing.setText(s);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                weatherRequestNull(error, coldClothing);

            }
        });

    }

    /**
     * Fetches and sets the number of clothing items suitable for warm weather.
     */
    private void setWarmClothing(){
        StatisticsManager.warmestAverageClothingRequest(this, UserManager.getUserID(this), MainActivity.SERVER_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("CW check", response.toString());
                try {
                    JSONObject statItem = response.getJSONObject("clothingStats");
                    String itemName = response.getString("itemName");
                    if (itemName.equals("null")){
                        itemName = "None";
                    }
                    Long highTemp = statItem.getLong("avgHighTemp");
                    String s = itemName + "\n" + String.valueOf(highTemp);
                    warmClothing.setText(s);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                weatherRequestNull(error, warmClothing);

            }
        });
    }


    /**
     * Fetches and sets a users outfit's largest average high temperature
     */
    private void setWarmOutfit(){
        StatisticsManager.warmestAverageOutfitRequest(this, UserManager.getUserID(this), MainActivity.SERVER_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("OW check", response.toString());
                try {
                    JSONObject object = response.getJSONObject("outfitStats");
                    String high = object.getString("avgHighTemp") + " °F";
                    warmOutfit.setText(high);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                weatherRequestNull(error, warmOutfit);


            }
        });
    }

    /**
     * Fetches and sets a user's outfit's lowest average low temperature.
     */
    private void setColdOutfit(){
        StatisticsManager.coldestAverageOutfitRequest(this, UserManager.getUserID(this), MainActivity.SERVER_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("OC", response.toString());
                try {
                    JSONObject object = response.getJSONObject("outfitStats");
                    String high = object.getString("avgLowTemp") + " °F";
                    coldOutfit.setText(high);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                weatherRequestNull(error, coldOutfit);

            }
        });
    }
    private void setClothingCostPerWear(Long clothingId, ClothingStatItem clothingStatItem){
        StatisticsManager.getCostPerWearClothing(this, clothingId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                clothingStatItem.setWornCount(response);
                allClothingStatsObjects.add(clothingStatItem);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                allClothingStatsObjects.add(clothingStatItem);
            }
        });
    }

    private void setOutfitCostPerWear(Long outfitId, ClothingStatItem clothingStatItem){
        StatisticsManager.getCostPerWearOutfit(this, outfitId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                clothingStatItem.setWornCount(response);
                allOutfitStatsObjects.add(clothingStatItem);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                allOutfitStatsObjects.add(clothingStatItem);

            }
        });

    }

    /**
     * Fetches and sets the number of outfits a particular clothing item is apart of.
     * @param clothingId a clothing ID
     * @param statItem a stat item
     */
    private void setNumberOfOutfitsIn(long clothingId, ClothingStatItem statItem){
        StatisticsManager.calcNumberOfOutfitsInRequest(this, clothingId, MainActivity.SERVER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                statItem.setNumberOfOutfitsIn(response);

                if (UserManager.getUserTier(getApplicationContext()) == 2){
                    //Premium tier additional clothing stat!
                    setClothingCostPerWear(clothingId, statItem);
                }
                else{
                    allClothingStatsObjects.add(statItem);
                }



            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("outfitsIn", error.toString());
            }
        });
    }


}
