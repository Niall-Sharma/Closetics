package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;     // define message textview variable
    private Button counterButton, passwordButton;     // define counter, password button variable

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */
        messageText = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
        counterButton = findViewById(R.id.main_counter_btn);// link to counter button in the Main activity XML
        passwordButton = findViewById(R.id.password_btn);

        /* extract data passed into this activity from another activity */
        Bundle extras = getIntent().getExtras();
        if(extras == null || !extras.containsKey("NUM")) {
            messageText.setText("Intent Example");
        } else {
            counter = extras.getInt("NUM");  // this will come from LoginActivity
            messageText.setText("The number was " + counter);
        }

        /* click listener on counter button pressed */
        counterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when counter button is pressed, use intent to switch to Counter Activity */
                Intent intent = new Intent(MainActivity.this, CounterActivity.class);
                intent.putExtra("NUM", counter);
                startActivity(intent);
            }
        });

        passwordButton.setOnClickListener((View v) -> {
            Intent intent = new Intent(MainActivity.this, PasswordActivity.class);
            intent.putExtra("NUM", counter);
            startActivity(intent);
        });
    }
}