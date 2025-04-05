package com.example.closetics.dashboard;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.closetics.MainActivity;
import com.example.closetics.R;

public class StatisticsActivity extends AppCompatActivity {
    private ImageButton back;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context activity = this;
        setContentView(R.layout.activity_statistics);

        back = findViewById(R.id.backButton);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
            }
        });





    }
}
