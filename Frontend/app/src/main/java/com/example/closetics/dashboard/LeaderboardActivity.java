package com.example.closetics.dashboard;

import android.os.Bundle;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.closetics.R;
import com.example.closetics.clothes.ClothesByTypeAdapter;
import com.example.closetics.clothes.ClothingItem;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {

    private Spinner leaderboardFilter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ClothesByTypeAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);










    }
}
