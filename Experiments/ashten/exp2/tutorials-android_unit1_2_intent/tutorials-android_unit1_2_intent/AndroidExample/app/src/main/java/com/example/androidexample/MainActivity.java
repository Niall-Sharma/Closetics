package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;     // define message textview variable
    private Button counterButton;     // define counter button variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */
        messageText = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
        counterButton = findViewById(R.id.main_counter_btn);// link to counter button in the Main activity XML
        counterButton.setText("To the Calculator Activity");
       // messageText.setText("Calculator Application");

        //A way to grab data from one activity to another (come back to this)
        //extract data passed into this activity from another activity
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            messageText.setText("Calculator Application");
        } else {
            String number = extras.getString("NUM");  // this will come from CounterActivity
            messageText.setText("The number was " + number);
        }


        //click listener on counter button pressed, "listening" for user input
        //Uses an anonymous class: View.OnClickListener with the classes method being onClick

        counterButton.setOnClickListener(new View.OnClickListener() {
            //The method that gets triggered when the button is pressed
            @Override
            public void onClick(View v) {
                /* when counter button is pressed, use intent to switch to Counter Activity */
                Intent intent = new Intent(MainActivity.this, CounterActivity.class);
                startActivity(intent);
            }
        });
    }
}