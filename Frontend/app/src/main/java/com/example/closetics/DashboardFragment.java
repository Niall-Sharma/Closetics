package com.example.closetics;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.dashboard.LeaderboardActivity;
import com.example.closetics.dashboard.SetTodaysOutfitFragment;
import com.example.closetics.dashboard.StatisticsActivity;

import com.example.closetics.dashboard.StatisticsManager;
import com.example.closetics.outfits.OutfitManager;
import com.example.closetics.outfits.OutfitsActivity;

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
    private int holdCount;


    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        leaderboard = view.findViewById(R.id.leaderboardButton);
        userStatistics = view.findViewById(R.id.userStatsButton);
        setTomorrow = view.findViewById(R.id.setTomorrowButton);
        login = view.findViewById(R.id.loginPageButton);
        outfitImage = view.findViewById(R.id.imageView2);
        todaysDate = view.findViewById(R.id.todaysDate);
        outfitName = view.findViewById(R.id.outfitName);

        current = OutfitManager.getCurrentDailyOutfit(getActivity());
        tomorrow = OutfitManager.getTomorrowDailyOutfit(getActivity());

        Log.d("current", String.valueOf(OutfitManager.getCurrentDailyOutfit(getActivity())));
        Log.d("tommorow", String.valueOf(OutfitManager.getTomorrowDailyOutfit(getActivity())));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            LocalDate today = LocalDate.now();
            String formattedDate = today.format(formatter);
            todaysDate.setText(formattedDate);

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
            getOutfit();
            getOutfitStats(current);
            String s1 = String.valueOf(current);
            outfitName.setText(s1);
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


        return view;
    }

    private void getOutfit(){
        OutfitManager.getOutfitRequest(getActivity(), current, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("get outfit", response.toString());
                try {
                    String name = response.getString("outfitName");
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




    /*
    Note: this needs to be updated to work for TODAY'S outfit not just some random outfit
    Possibly add something to the outfit controller or outfit stat controller in the backend

     */
    private void getOutfitStats(long outfitId){
        StatisticsManager.getOutfitsStatsRequest(getActivity(), outfitId, MainActivity.SERVER_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("check", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("check", error.toString());
            }
        });
    }

    private void showFragment(){
        //NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
        //navController.navigate(R.id.set_todays_outfit_action);

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

            }
        }
    }








}