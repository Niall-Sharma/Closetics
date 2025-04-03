package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.handshake.ServerHandshake;

public class ChatActivity1 extends AppCompatActivity {

    private Button sendBtn, backMainBtn;
    private EditText msgEtx;
    private TextView msgTv, usernameTv;
    private static final String TAG = "ChatActivity1";

//    private boolean isRecieverActive;
//    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat1);
        Log.d(TAG, "onCreate: ChatActivity1 initialized.");


        /* initialize UI elements */
        sendBtn = (Button) findViewById(R.id.sendBtn);
        backMainBtn = (Button) findViewById(R.id.backMainBtn);
        msgEtx = (EditText) findViewById(R.id.msgEdt);
        msgTv = (TextView) findViewById(R.id.tx1);
        usernameTv = findViewById(R.id.usernameTv);

        usernameTv.setText("Connected as: " + getIntent().getStringExtra("username"));

        msgEtx.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND/* || keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER*/) {
                    sendMsg();
                    return true;
                }
                return false;
            }
        });

//        msgEtx.setImeActionLabel("actionDone", KeyEvent.KEYCODE_ENTER);

        /* send button listener */
        sendBtn.setOnClickListener(v -> {
            sendMsg();
        });

        /* back button listener */
        backMainBtn.setOnClickListener(view -> {
            Intent serviceIntent = new Intent(this, WebSocketService.class);
            serviceIntent.setAction("DISCONNECT");
            serviceIntent.putExtra("key", "chat1");
            startService(serviceIntent);

            // go to chat activity
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("chat1Username", getIntent().getStringExtra("username"));
            startActivity(intent);
            finish();
        });
    }

    private void sendMsg() {
        String message = msgEtx.getText().toString().trim();
        // broadcast this message to the WebSocketService
        // tag it with the key - to specify which WebSocketClient (connection) to send
        // in this case: "chat1"
        if (message.isEmpty()) {
            Log.w(TAG, "sendBtn clicked but message was empty.");
            return;
        }
        Intent intent = new Intent("SendWebSocketMessage");
        intent.putExtra("key", "chat1");
        intent.putExtra("message", msgEtx.getText().toString());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        Log.d(TAG, "Broadcast sent for message: " + message);
        msgEtx.setText("");
        Log.d(TAG, "Message input cleared.");
    }

    // For receiving messages
    // only react to messages with tag "chat1"
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String key = intent.getStringExtra("key");
            if ("chat1".equals(key)){
                String message = intent.getStringExtra("message");
//                String coloredMessage = "<b>" + message.substring(0, message.indexOf(':')) + "</b>" + message.substring(message.indexOf(':'));
                runOnUiThread(() -> {
                    Log.d("ChatActivity1", "message sent from " + getIntent().getStringExtra("username"));
                    Log.d("ChatActivity1", "message sent: " + message);
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
//        isRecieverActive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
//        isRecieverActive = false;
        Log.d("ChatActivity1", "messageReceiver stopped");
    }
}