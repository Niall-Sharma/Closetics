package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class BackgroundChanger extends AppCompatActivity {

    private ArrayList<ColorWidget> colorWidgetsList = new ArrayList<>();
    private int[] widgetImages = {R.drawable.easel_red, R.drawable.easel_olive, R.drawable.easel_orange,
            R.drawable.easel_purple, R.drawable.easel_dark_green, R.drawable.easel_blue};

    //Set up the activity/screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_changer);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        setUpEachColorWidget();

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, colorWidgetsList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    private void setUpEachColorWidget(){
        //Grab arrays from strings.xml
        String [] colorNames = getResources().getStringArray(R.array.colors_full_name);
        String [] tintNames = getResources().getStringArray(R.array.tint_values);

        //Create card widgets with all the proper data
        for (int i =0; i<colorNames.length;i++){
            ColorWidget cardWidget = new ColorWidget(colorNames[i], tintNames[i], widgetImages[i]);
            colorWidgetsList.add(cardWidget);
        }
    }


    /*

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

     */
}
