package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;   // define message textview variable
    private Button themeButton;

    private int nightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */
        messageText = findViewById(R.id.mainText);      // link to message textview in the Main activity XML
        messageText.setText("Hello World");

        themeButton = findViewById(R.id.themeButton);

        // init shared prefs
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        // set night mode state (default - system)
        nightMode = sharedPreferences.getInt("nightMode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(nightMode);

        // change theme on click
        themeButton.setOnClickListener((View v) -> {
            if (nightMode == AppCompatDelegate.MODE_NIGHT_NO) {
                nightMode = AppCompatDelegate.MODE_NIGHT_YES;
            }
            else {
                nightMode = AppCompatDelegate.MODE_NIGHT_NO;
            }
            editor.putInt("nightMode", nightMode);
            editor.apply();
            AppCompatDelegate.setDefaultNightMode(nightMode);
        });


    }
}