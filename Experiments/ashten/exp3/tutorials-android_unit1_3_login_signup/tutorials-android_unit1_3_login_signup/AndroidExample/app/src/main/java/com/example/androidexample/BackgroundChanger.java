package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Arrays;
import java.util.List;

public class BackgroundChanger extends AppCompatActivity {

    private LinearLayout topContainer;
    private RecyclerView recyclerView;
    private PopulateRecycler populateRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_changer);

        topContainer = findViewById(R.id.top_container);
        recyclerView = findViewById(R.id.recyclerView);

        // List of colors to display in RecyclerView
        List<String> colors = Arrays.asList("#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FFA500", "#800080");

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        populateRecycler = new PopulateRecycler(colors, new PopulateRecycler.OnColorClickListener() {
            @Override
            public void onColorClick(String color) {
                // Change the background color of top_container
                topContainer.setBackgroundColor(Color.parseColor(color));

                // Return color to MainActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("SELECTED_COLOR", color);
                setResult(RESULT_OK, resultIntent);
            }
        });
        recyclerView.setAdapter(populateRecycler);
    }
}
