package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    private Button connectBtn1, connectBtn2, backBtn1, backBtn2;
    private EditText serverEtx1, usernameEtx1, serverEtx2, usernameEtx2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* initialize UI elements */
        connectBtn1 = (Button) findViewById(R.id.connectBtn);
        connectBtn2 = (Button) findViewById(R.id.connectBtn2);
        backBtn1 = (Button) findViewById(R.id.backBtn);
        backBtn2 = (Button) findViewById(R.id.backBtn2);
        serverEtx1 = (EditText) findViewById(R.id.serverEdt);
        usernameEtx1 = (EditText) findViewById(R.id.unameEdt);
        serverEtx2 = (EditText) findViewById(R.id.serverEdt2);
        usernameEtx2 = (EditText) findViewById(R.id.unameEdt2);

        // Connect on Enter press
        usernameEtx1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    connectBtn1.performClick();
                    return true;
                }
                return false;
            }
        });

        /* connect1 button listener */
        connectBtn1.setOnClickListener(view -> {
            String username = usernameEtx1.getText().toString();
            if (username.isEmpty()) {
                Toast.makeText(MainActivity.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!validateUsername(username)) {
                Toast.makeText(MainActivity.this, "Invalid username", Toast.LENGTH_SHORT).show();
                return;
            }
            String serverUrl = serverEtx1.getText().toString() + username;

            // start Websocket service with key "chat1"
            Intent serviceIntent = new Intent(this, WebSocketService.class);
            serviceIntent.setAction("CONNECT");
            serviceIntent.putExtra("key", "chat1");
            serviceIntent.putExtra("url", serverUrl);
            startService(serviceIntent);

            // got to chat activity #1
            Intent intent = new Intent(this, ChatActivity1.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        /* connect2 button listener */
        connectBtn2.setOnClickListener(view -> {
            String serverUrl = serverEtx2.getText().toString() + usernameEtx2.getText().toString();

            // start Websocket service with key "chat2"
            Intent serviceIntent = new Intent(this, WebSocketService.class);
            serviceIntent.setAction("CONNECT");
            serviceIntent.putExtra("key", "chat2");
            serviceIntent.putExtra("url", serverUrl);
            startService(serviceIntent);

            // got to chat activity #2
            Intent intent = new Intent(this, ChatActivity2.class);
            startActivity(intent);
        });

        /* back button listener */
        backBtn1.setOnClickListener(view -> {
            String username = getIntent().getStringExtra("chat1Username");
            if (username == null) {
                Toast.makeText(MainActivity.this, "No previous session found", Toast.LENGTH_SHORT).show();
                return;
            }
            String serverUrl = serverEtx1.getText().toString() + username;

            // start Websocket service with key "chat1"
            Intent serviceIntent = new Intent(this, WebSocketService.class);
            serviceIntent.setAction("CONNECT");
            serviceIntent.putExtra("key", "chat1");
            serviceIntent.putExtra("url", serverUrl);
            startService(serviceIntent);

            // got to chat activity
            Intent intent = new Intent(this, ChatActivity1.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        /* back2 button listener */
        backBtn2.setOnClickListener(view -> {
            // got to chat activity
            Intent intent = new Intent(this, ChatActivity2.class);
            startActivity(intent);
        });
    }

    public static boolean validateUsername(String username){
        String pattern = "[0-9A-Za-z]{3,16}";
        return username.matches(pattern);
    }

}