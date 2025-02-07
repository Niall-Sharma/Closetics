package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

//Note: Add hard coded strings outside of XML file


public class MainActivity extends AppCompatActivity {

    private TextView messageText;   // define message textview variable
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // Links to the activity XML in the layout folder

        /* initialize UI elements */
        //Initializing the text view object from the layout XML
        messageText = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
        messageText.setText("Closetics");
        //First Button
        button1 = findViewById(R.id.firstButton);
        button1.setText("Press Here!");
        //Second Button
        button2 = findViewById(R.id.secondButton);
        button2.setText("Press Here!");

        //Third Button
        button3 = findViewById(R.id.thirdButton);
        button3.setText("Press Here!");





    }
}