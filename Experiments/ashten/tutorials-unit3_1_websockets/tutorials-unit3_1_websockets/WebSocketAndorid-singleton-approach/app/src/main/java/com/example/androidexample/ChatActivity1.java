package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.handshake.ServerHandshake;

import java.util.Random;

public class ChatActivity1 extends AppCompatActivity implements WebSocketListener{

    private Button sendBtn, backMainBtn;
    private EditText msgEtx;
    private TextView msgTv;
    private String [] randomQuestionChoices = {"Hello", "Howdy", "OK", "whats up", "what's your name?", "how have you been?",
    "what did you do today", "what is your favorite color?"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat1);

        /* initialize UI elements */
        sendBtn = (Button) findViewById(R.id.sendBtn);
        backMainBtn = (Button) findViewById(R.id.backMainBtn);
        msgEtx = (EditText) findViewById(R.id.msgEdt);
        msgTv = (TextView) findViewById(R.id.tx1);


        /* connect this activity to the websocket instance */
        WebSocketManager1.getInstance().setWebSocketListener(ChatActivity1.this);

        /* send button listener */
        sendBtn.setOnClickListener(v -> {
            String message = msgEtx.getText().toString().trim();
            if (message.isEmpty()) {
                //Toast.makeText(ChatActivity1.this, "Message cannot be empty!", Toast.LENGTH_SHORT).show();
                Random random = new Random();
                int randomNumber = random.nextInt(randomQuestionChoices.length-1);
                randomResponse(randomQuestionChoices[randomNumber]);
            } else {
                try {
                    // send message
                    WebSocketManager1.getInstance().sendMessage(msgEtx.getText().toString());
                    msgEtx.setText(""); //clear text box
                } catch (Exception e) {
                    Log.d("ExceptionSendMessage:", e.getMessage().toString());
                }
            }
        });

        /* back button listener */
        backMainBtn.setOnClickListener(view -> {
            // got to chat activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }


    @Override
    public void onWebSocketMessage(String message) {
        /**
         * In Android, all UI-related operations must be performed on the main UI thread
         * to ensure smooth and responsive user interfaces. The 'runOnUiThread' method
         * is used to post a runnable to the UI thread's message queue, allowing UI updates
         * to occur safely from a background or non-UI thread.
         */
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "\n"+message);
        });
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {}

    @Override
    public void onWebSocketError(Exception ex) {}

    private void randomResponse(String message){

        WebSocketManager1.getInstance().sendMessage(message);



    }
}