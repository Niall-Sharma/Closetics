package com.example.closetics;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.clothes.ClothesActivity;
import com.example.closetics.clothes.ClothesCreationBaseFragment;
import com.example.closetics.clothes.ClothesManager;
import com.example.closetics.clothes.ClothingItem;
import com.example.closetics.clothes.CustomSlideAdapter;
import com.example.closetics.dashboard.ClothingStatItem;
import com.example.closetics.dashboard.ImagePagerAdapter;
import com.example.closetics.dashboard.LeaderboardActivity;
import com.example.closetics.dashboard.SetTodaysOutfitFragment;
import com.example.closetics.dashboard.StatisticsActivity;

import com.example.closetics.dashboard.StatisticsManager;
import com.example.closetics.dashboard.StatisticsRecyclerViewAdapter;
import com.example.closetics.outfits.OutfitManager;
import com.example.closetics.outfits.OutfitsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DashboardFragment extends Fragment {



    private Button leaderboard;
    private Button userStatistics;
    private Button setTomorrow;
    private long current;
    private long tomorrow;
    private ImageButton login;
    private TextView outfitName;
    private TextView outfitInsights;
    private TextView todaysDate;
    private ImageView outfitImage;
    private JSONObject outfitStats;
    private TextView wornCount;
    private TextView outfitTotalCount;
    private TextView averageLowTemperature;
    private ArrayList<byte[]> images;
    private ArrayList<ClothingStatItem> clothingStatItemArrayList;
    private CardView cardView;

    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;

    private TextView todaysTemperature;
    private ImageView todaysTemperatureImage;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private StatisticsRecyclerViewAdapter statsAdapter;


    /*
    Note add a viewpager2 for the outfit images
     */

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // redirect to profile if not logged in
        //((MainActivity)getActivity()).redirectToFragment(3);

        leaderboard = view.findViewById(R.id.leaderboardButton);
        userStatistics = view.findViewById(R.id.userStatsButton);
        setTomorrow = view.findViewById(R.id.setTomorrowButton);
        login = view.findViewById(R.id.loginPageButton);
        outfitImage = view.findViewById(R.id.imageView2);
        todaysDate = view.findViewById(R.id.todaysDate);
        outfitName = view.findViewById(R.id.outfitName);
        outfitInsights = view.findViewById(R.id.outfitStats);
        wornCount = view.findViewById(R.id.wornCount);
        cardView = view.findViewById(R.id.cardView);
        outfitTotalCount = view.findViewById(R.id.outfitTotalCount);
        averageLowTemperature = view.findViewById(R.id.averageLowTemperature);
        viewPager = view.findViewById(R.id.viewPager);
        todaysTemperature = view.findViewById(R.id.dashboard_temperature_text);
        todaysTemperatureImage = view.findViewById(R.id.dashboard_temperature_image);
        recyclerView = view.findViewById(R.id.recyclerView);



        current = OutfitManager.getCurrentDailyOutfit(getActivity());
        tomorrow = OutfitManager.getTomorrowDailyOutfit(getActivity());

        Log.d("current", String.valueOf(OutfitManager.getCurrentDailyOutfit(getActivity())));
        Log.d("tommorow", String.valueOf(OutfitManager.getTomorrowDailyOutfit(getActivity())));



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/dd/yyyy");
            LocalDate today = LocalDate.now();
            String formattedDate = today.format(formatter);
            todaysDate.setText(formattedDate);

        }
        //Check user tier for permissions
        if (UserManager.getUserTier(getActivity()) == 0){
            //No access to stats or leaderboard
            //15 outfits
            //15 clothes
            setFreeVisibility();


        }
        else if (UserManager.getUserTier(getActivity()) ==1){
            //Statistics and 30, 30
            setBasicVisibility();

        }
        else{
            //Premium
            //All features, leaderboard, no limits on outfits or clothes
            setPremiumVisibility();
        }

        //No set outfit for today
        if (current == -1){
            String s = "Set Today's Outfit";
            setTomorrow.setText(s);
            outfitName.setText("Please set an Outfit!");

        }
        //Current is set!
        if (current != -1){
            String s = "Set Tomorrow's Outfit";
            setTomorrow.setText(s);
            outfitImage.setVisibility(View.GONE);
            getOutfit();
            String s1 = String.valueOf(current);
            outfitName.setText(s1);
            //Set the viewpager with images
            //StatisticsRecyclerViewAdapter adapter = new StatisticsRecyclerViewAdapter();
            //recyclerView
        }





        setTomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OutfitsActivity.class);

                //If current is false, set current
                if (current == -1){
                    intent.putExtra("setTomorrow", false);
                }
                else{
                    intent.putExtra("setTomorrow", true);
                }
                startActivity(intent);
            }
        });

        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LeaderboardActivity.class);
                startActivity(intent);

            }
        });

        userStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StatisticsActivity.class);
                startActivity(intent);

            }
        });

        setTodaysTemperature();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // redirect to profile if not logged in
        if (UserManager.getUsername(getActivity().getApplicationContext()) == null) {
            ((MainActivity)getActivity()).redirectToFragment(3);
        }
    }

    private void getClothingItems(Context context, Long outfitId){
        OutfitManager.getAllOutfitItemsRequest(context, outfitId, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i =0; i < response.length(); i++){
                    try {
                        JSONObject clothingItem = response.getJSONObject(i);
                        Log.d("ClothingItem", clothingItem.toString());
                        Long clothesId = clothingItem.getLong("clothesId");
                        JSONObject statItem = clothingItem.getJSONObject("clothingStats");
                        String itemName = clothingItem.getString("itemName");
                        ClothingStatItem clothingStatItem = new ClothingStatItem(statItem, itemName, clothesId);
                        clothingStatItemArrayList = new ArrayList<>();
                        images = new ArrayList<>();
                        getImage(context, clothesId, response.length(), clothingStatItem);


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Image Error", error.toString());
            }
        });
    }
    private void getImage(Context context, Long clothesId, int size, ClothingStatItem clothingStatItem){
        ClothesManager.getImageByClothing(context, clothesId, MainActivity.SERVER_URL, new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {
                images.add(response);
                clothingStatItem.setImage(response);
                clothingStatItemArrayList.add(clothingStatItem);
                if (images.size() == size){
                    ImagePagerAdapter adapter = new ImagePagerAdapter(context, images);
                    viewPager.setAdapter(adapter);
                    layoutManager = new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(layoutManager);
                    statsAdapter = new StatisticsRecyclerViewAdapter(clothingStatItemArrayList, 2, context);
                    recyclerView.setAdapter(statsAdapter);


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Assume there is no image, add null
                images.add(null);
                clothingStatItem.setImage(null);
                clothingStatItemArrayList.add(clothingStatItem);
                if (images.size() == size){
                    ImagePagerAdapter adapter = new ImagePagerAdapter(context, images);
                    viewPager.setAdapter(adapter);
                    layoutManager = new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(layoutManager);
                    statsAdapter = new StatisticsRecyclerViewAdapter(clothingStatItemArrayList, 2, context);
                    recyclerView.setAdapter(statsAdapter);

                }

            }
        });
    }

    private void getOutfit(){
        OutfitManager.getOutfitRequest(getActivity(), current, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("get outfit", response.toString());
                try {
                    String name = response.getString("outfitName");
                    outfitName.setText(name);
                    name+=" Insights";
                    outfitInsights.setText(name);
                    //Grab the images from the clothes
                    //
                    outfitStats = response.getJSONObject("outfitStats");
                    String worn = outfitStats.getString("timesWorn");
                    wornCount.setText(worn);
                    String lowTemp = outfitStats.getString("avgLowTemp") + " °F";
                    String highTemp = outfitStats.getString("avgHighTemp") + " °F";
                    outfitTotalCount.setText(highTemp);
                    averageLowTemperature.setText(lowTemp);
                    Long outfitId = response.getLong("outfitId");
                    getClothingItems(getActivity(), outfitId);


                    //Grab the image

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
    private void setBasicVisibility(){
        cardView.setVisibility(View.VISIBLE);
        userStatistics.setVisibility(View.VISIBLE);
        leaderboard.setVisibility(View.GONE);
    }
    private void setFreeVisibility(){
        cardView.setVisibility(View.INVISIBLE);
        userStatistics.setVisibility(View.GONE);
        leaderboard.setVisibility(View.GONE);

    }
    private void setPremiumVisibility(){
        cardView.setVisibility(View.VISIBLE);
        userStatistics.setVisibility(View.VISIBLE);
        leaderboard.setVisibility(View.VISIBLE);
    }



    /*
    Will need to add error
     */
    public static void addWornToday(Context context, long outfitId){
        StatisticsManager.addWornOutfitTodayRequest(context,outfitId, MainActivity.SERVER_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Add worn response", response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error worn", error.toString());

            }
        });
    }




    /*
    This is called by the android manifest!
     */
    public static class DateChangeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_DATE_CHANGED.equals(intent.getAction())){
                //Swap tomorrow to current on next day
                long clothingId = OutfitManager.getTomorrowDailyOutfit(context);
                OutfitManager.saveCurrentDailyOutfit(context, clothingId);
                //Reset tomorrow to -1
                OutfitManager.saveTomorrowDailyOutfit(context, -1);
                addWornToday(context, clothingId);
            }
        }
    }

    private void setTodaysTemperature() {
        StatisticsManager.getTodaysWeatherRequest(getActivity().getApplicationContext(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Get today's weather response: ", response.toString());

                        try {
                            double lowTemp = response.getDouble("lowTemp");
                            double highTemp = response.getDouble("highTemp");
                            double weightedAvgTemp = (lowTemp + 2.0 * highTemp) / 3.0;

                            todaysTemperature.setText((Math.round(lowTemp * 10.0) / 10.0) + " - " + (Math.round(highTemp * 10.0) / 10.0) + " °F");

                            if (weightedAvgTemp < 60.0) { // cold
                                todaysTemperatureImage.setImageResource(R.drawable.thermometer_low);
                            } else if (weightedAvgTemp <= 80.0) { // mid
                                todaysTemperatureImage.setImageResource(R.drawable.thermometer);
                            } else { // hot
                                todaysTemperatureImage.setImageResource(R.drawable.thermometer_high);
                            }

                        } catch (JSONException e) {
                            Log.e("JSON Parse Error", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error getting today's weather: ", error.toString());

                    }
                });
    }





}