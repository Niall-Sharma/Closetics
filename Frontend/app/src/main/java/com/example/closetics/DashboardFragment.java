package com.example.closetics;


import android.content.Intent;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.dashboard.LeaderboardActivity;
import com.example.closetics.dashboard.SetTodaysOutfitFragment;
import com.example.closetics.dashboard.StatisticsActivity;

import com.example.closetics.dashboard.StatisticsManager;
import com.example.closetics.outfits.OutfitManager;

import org.json.JSONObject;

public class DashboardFragment extends Fragment {



    private Button leaderboard;
    private Button userStatistics;
    private Button setTomorrow;
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

        if (OutfitManager.getCurrentDailyOutfit(getActivity()) == null){
            /*
            This means that there is no outfit set for today!!
            So show a different fragment!
             */
            //showFragment();
        }




        //getOutfitStats();






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
        setTomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Add more", "clicked");
            }
        });


        return view;
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




    //Requests on this page:
    //getOutfit worn today
    //getOutfit stats, and getOutfit for grabbing the image

    //get overall statistics when pressing user statistics




}