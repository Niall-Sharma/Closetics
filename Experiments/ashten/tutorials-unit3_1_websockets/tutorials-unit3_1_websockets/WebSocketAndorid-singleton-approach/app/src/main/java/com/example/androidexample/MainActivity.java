package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity{

    private Button connectBtn, connectBtn2, backBtn, backBtn2;
    private EditText serverEtx, usernameEtx, serverEtx2, usernameEtx2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* initialize UI elements */
        connectBtn = (Button) findViewById(R.id.connectBtn);
        connectBtn2 = (Button) findViewById(R.id.connectBtn2);
        backBtn = (Button) findViewById(R.id.backBtn);
        backBtn2 = (Button) findViewById(R.id.backBtn2);
        serverEtx = (EditText) findViewById(R.id.serverEdt);
        usernameEtx = (EditText) findViewById(R.id.unameEdt);
        serverEtx2 = (EditText) findViewById(R.id.serverEdt2);
        usernameEtx2 = (EditText) findViewById(R.id.unameEdt2);

        /* connect button listener */
        connectBtn.setOnClickListener(view -> {
            String serverUrl = serverEtx.getText().toString() + usernameEtx.getText().toString();

            // Establish WebSocket connection and set listener
            WebSocketManager1.getInstance().connectWebSocket(serverUrl);

            // got to chat activity
            Intent intent = new Intent(this, ChatActivity1.class);
            startActivity(intent);
        });

        /* connect button listener */
        connectBtn2.setOnClickListener(view -> {
            String serverUrl = serverEtx2.getText().toString() + usernameEtx2.getText().toString();

            // Establish WebSocket connection and set listener
            WebSocketManager2.getInstance().connectWebSocket(serverUrl);

            // got to chat activity
            Intent intent = new Intent(this, ChatActivity2.class);
            startActivity(intent);
        });

        /* back button listener */
        backBtn.setOnClickListener(view -> {
            // got to chat activity
            Intent intent = new Intent(this, ChatActivity1.class);
            startActivity(intent);
        });

        /* back button listener */
        backBtn2.setOnClickListener(view -> {
            // got to chat activity
            Intent intent = new Intent(this, ChatActivity2.class);
            startActivity(intent);
        });
    }
}