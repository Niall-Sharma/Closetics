package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultActivity extends AppCompatActivity {

    private ImageView image;
    private TextView resultsText;
    private Button backButton;

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        image = findViewById(R.id.results_image);
        resultsText = findViewById(R.id.results_text);
        backButton = findViewById(R.id.back_button);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if (extras.containsKey("NUM"))
                counter = extras.getInt("NUM");

            String res = "";

            if (extras.containsKey("password"))
                res += " - Your password: " + extras.getString("password") + "\n";
            if (extras.containsKey("chip"))
                res += " - " + (extras.getBoolean("chip") ? "chip" : "no chip") + "\n";
            if (extras.containsKey("switch"))
                res += " - " + (extras.getBoolean("switch") ? "You did switch" : "You did not switch") + "\n";
            if (extras.containsKey("radio")) {
                int radioId = extras.getInt("radio", -1);
                if (radioId == R.id.good_radio_button)
                    res += " - You are from good, thanks\n";
                else if (radioId == R.id.pardon_radio_button)
                    res += " - Pardoned\n";
                else
                    res += " - You are not from\n";
            }

            resultsText.setText(res);
        }

        backButton.setOnClickListener((View v) -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            intent.putExtra("NUM", counter);
            startActivity(intent);
        });
    }
}