package com.example.closetics;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.closetics.dashboard.LeaderboardActivity;
import com.example.closetics.dashboard.StatisticsActivity;

import com.example.closetics.R;

public class DashboardFragment extends Fragment {

    private Button leaderboard;
    private Button userStatistics;
    private Button setTomorrow;
    private ImageButton login;
    private TextView outfitName;
    private TextView outfitInsights;
    private TextView todaysDate;
    private ImageView outfitImage;


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
}