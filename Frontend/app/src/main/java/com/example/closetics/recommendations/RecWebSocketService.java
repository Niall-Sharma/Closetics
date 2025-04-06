package com.example.closetics.recommendations;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.closetics.MainActivity;
import com.example.closetics.UserManager;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class RecWebSocketService extends Service {
    private final String URL = MainActivity.SERVER_WS_URL + "/recommendation/"; // + {{userId}}

    private WebSocketClient webSocket;

    private boolean isConnected;

    // Listen to the messages from Activities
    // Forward the message to its designated Websocket connection
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");

            if (webSocket != null && isConnected) {
                webSocket.send(message);
                Log.d("RecWebSocket", "Message sent: " + message);
            } else {
                Log.d("RecWebSocket", "WebSocket is closed, message not sent: " + message);
            }
        }
    };

    public RecWebSocketService() {
        webSocket = null;
        isConnected = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("RecWebSocket", "Received intent");
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            if (action.equals("RecWebSocketConnect")) {
                //String URL = intent.getStringExtra("URL"); // eg, "ws://localhost:8080/chat/1/uname"
                connectWebSocket();
            } else if (action.equals("RecWebSocketDisconnect")) {
                disconnectWebSocket();
            }
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {    // Register BroadcastReceiver to listen for messages from Activities
        super.onCreate();
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(messageReceiver, new IntentFilter("RecWebSocketSendMessage"));
    }

    @Override
    public void onDestroy() {   // Close WebSocket connection to prevent memory leaks
        super.onDestroy();
        if (webSocket != null) {
            webSocket.close();
            isConnected = false;
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        Log.d("RecWebSocket", "WebSocket closed on Destroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Initialize WebSocket client and define callback for message reception
    private void connectWebSocket() {
        if (isConnected) {
            Log.d("RecWebSocket", "Connection attempt, but WebSocket is already connected");
            return;
        }

        try {
            URI serverURI = URI.create(URL + UserManager.getUserID(getApplicationContext()));
            WebSocketClient client = new WebSocketClient(serverURI) {
                @Override
                public void onOpen(ServerHandshake handShakeData) {
                    Log.d("RecWebSocket", "On Open. URI: " + serverURI.toString());
                    isConnected = true;
                }

                @Override
                public void onMessage(String message) {
                    Intent intent = new Intent("RecWebSocketMessageReceived");
                    intent.putExtra("message", message);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                    Log.d("RecWebSocket", "On Message. Message: " + message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("RecWebSocket", "On Close. Code: " + code + ", Reason: " + reason);
                    isConnected = false;
                }

                @Override
                public void onError(Exception ex) {
                    Log.d("RecWebSocket", "On Error: " + ex.toString());
                }
            };

            client.connectBlocking(); // connect to the websocket
            webSocket = client;
            isConnected = true;

        } catch (Exception e) {
            Log.e("RecWebSocket Error", "Connection Error: " + e.toString());
        }
    }

    private void disconnectWebSocket() {
        if (webSocket != null && isConnected) {
            webSocket.close();
            isConnected = false;
        }
    }
}
