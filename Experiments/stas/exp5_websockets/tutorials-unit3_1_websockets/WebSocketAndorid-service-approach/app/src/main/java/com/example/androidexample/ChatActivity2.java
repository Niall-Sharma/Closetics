package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity2 extends AppCompatActivity {

    private Button sendBtn;
    private EditText msgEtx;
    private TextView msgTv;
    private Button backMainBtn;
    private static final String TAG = "ChatActivity2"; // For logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        /* initialize UI elements */
        sendBtn = (Button) findViewById(R.id.sendBtn2);
        msgEtx = (EditText) findViewById(R.id.msgEdt2);
        msgTv = (TextView) findViewById(R.id.tx2);
        backMainBtn = (Button) findViewById(R.id.backMainBtn);
        Log.d(TAG, "onCreate: ChatActivity2 initialized.");

        /* send button listener */
        sendBtn.setOnClickListener(v -> {
            // broadcast this message to the WebSocketService
            // tag it with the key - to specify which WebSocketClient (connection) to send
            // in this case: "chat2"
            String message = msgEtx.getText().toString().trim();
            if (message.isEmpty()) {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "sendBtn clicked but message was empty.");
                return;
            }
            Intent intent = new Intent("SendWebSocketMessage");
            intent.putExtra("key", "chat2");
            intent.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            msgEtx.setText("");
        });

        /* back button listener */
        backMainBtn.setOnClickListener(view -> {
            // got to chat activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

    }

    // For receiving messages
    // only react to messages with tag "chat2"
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String key = intent.getStringExtra("key");
            if ("chat2".equals(key)){
                String message = intent.getStringExtra("message");
                runOnUiThread(() -> {
                    String s = msgTv.getText().toString();
                    msgTv.setText(s + "\n" + message);
                });
            }
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver,
                new IntentFilter("WebSocketMessageReceived"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }
}