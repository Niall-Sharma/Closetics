package com.example.closetics.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.MainActivity;
import com.example.closetics.PublicProfileActivity;
import com.example.closetics.R;
import com.example.closetics.UserManager;
import com.example.closetics.clothes.ClothesActivity;
import com.example.closetics.clothes.ClothesByTypeAdapter;
import com.example.closetics.clothes.ClothingItem;
import com.example.closetics.clothes.EditClothesActivity;
import com.example.closetics.outfits.OutfitManager;

import java.net.URISyntaxException;
import java.nio.channels.AlreadyConnectedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class LeaderboardActivity extends AppCompatActivity {

    /*
     Continually update the leaderboard with Runnable and Handler
     */
    private Runnable continuousMessaging;
    private Handler handler;

    /*
    Websocket client
     */
    private WebSocketClient webSocketClient;


    private Spinner leaderboardFilter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private LeaderboardRecyclerViewAdapter adapter;
    private String chosenFilter;
    private URI uri;
    private boolean isConnected  = false;
    private Button back;


    private int spinnerPosition; //Default to first spinner item
    private TextView categoryValue;
    private final String[] spinnerItems = new String[]
            {"Most Expensive Clothing Items", "Largest Closet", "Most Bougie Outfits" };

    private final String WEB_SOCKET_URL = MainActivity.SERVER_WS_URL +"/leaderboard/"; // + {username}/{type}

    private ArrayList<LeaderboardItem> adapterItems;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        /*
        Set the categories spinner
         */

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        leaderboardFilter = findViewById(R.id.spinner);
        categoryValue = findViewById(R.id.category);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        /*
        Note this will have one more item
         */
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        leaderboardFilter.setAdapter(adapter);

        leaderboardFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //Store chosen question
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinnerPosition = position + 1;
                chosenFilter = (String) parent.getItemAtPosition(position);
                if (isConnected) {
                    webSocketClient.close();
                }
                connectWebSocketClient();
                categoryValue.setText(chosenFilter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        handler = new Handler();
        continuousMessaging = new Runnable() {
            @Override
            public void run() {
                //Send a message to websocket
                String message = "PLEASE_SEND_ME_THE_LEADERBOARDDDDDD";

                if (webSocketClient != null && webSocketClient.isOpen()) {
                    webSocketClient.send(message);
                }

                //Delay of every 10 seconds
                handler.postDelayed(this, 10000);

            }
        };

        handler.post(continuousMessaging);


    }



    private void setWebSocketClient(URI uri){

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.d("LeaderboardWebsocket", "Connected to Websocket server!");
                isConnected = true;
                //Loading UI?
                //Connected UI?


            }

            @Override
            public void onMessage(String message) {
                Log.d("LeaderboardWebsocket", "Message received: " + message);
                isConnected = true;

                if (message.equals("Username already exists")){
                    throw new AlreadyConnectedException();
                    //print an error message
                    //Do more?
                }

                //Grab the JSONArray
                try {
                    JSONArray leaderboardArray = new JSONArray(message);
                    ArrayList<LeaderboardItem> objects = new ArrayList<>(Collections.nCopies(leaderboardArray.length(), null));

                    LeaderboardItem leaderboardItem;
                    final AtomicInteger remainingRequests = new AtomicInteger(leaderboardArray.length()); // Counter to track async requests
                    //Parse the array and grab the fields needed for the recycler view
                    for (int i = 0; i < leaderboardArray.length(); i++){
                        //Do different things based on the category!
                        if (spinnerPosition == 1) {
                            JSONObject object = leaderboardArray.getJSONObject(i);
                            Log.d("Object", leaderboardArray.get(i).toString());
                            JSONObject user = object.getJSONObject("user");
                            String username = user.getString("username");
                            String price = object.getString("price");
                            long userId = user.getLong("userId");

                            if (!price.equals("null")) price = "$" + price;

                            leaderboardItem = new LeaderboardItem(String.valueOf(i) +1, username, price, userId);
                            objects.set(i, leaderboardItem);

                        }
                        else if (spinnerPosition == 2){
                            JSONArray object = leaderboardArray.getJSONArray(i);
                            //final AtomicInteger remainingRequests2 = new AtomicInteger(object.length());
                            Log.d("Array", leaderboardArray.toString());
                            long userId = object.getLong(0);
                            String clothingItems = object.getString(1);
                            leaderboardItem = new LeaderboardItem(String.valueOf(i), clothingItems);

                            //Get the username from the user id

                            getUsername(userId, leaderboardItem,  new UsernameCallback() {
                                @Override
                                public void onSuccess(String username, String i, String categoryValue) {
                                    LeaderboardItem l = new LeaderboardItem(i + 1, categoryValue);
                                    l.setUsername(username);
                                    Log.d("Username", "Username: " + l.getUsername());
                                    objects.set(Integer.valueOf(i), l);
                                }

                                @Override
                                public void onError(String error) {
                                    Log.e("Username Error", "Error: " + error);
                                }
                                @Override
                                public void onFinish(){
                                    if (remainingRequests.decrementAndGet() == 0){
                                        updateAdapter(objects);
                                    }
                                }
                            });


                        }
                        //3rd category
                        else{
                            //Object design is outfit id and total price
                            Log.d("tag", String.valueOf(leaderboardArray.length()));
                            JSONObject object = leaderboardArray.getJSONObject(i);
                            Log.d("Object", object.toString());
                            String price = object.getString("totalPrice");
                            //Use the getOutfit endpoint to get the userId
                            long outfitId = object.getLong("outfitId");
                            if (price.equals("null")) {
                                remainingRequests.decrementAndGet();
                                objects.remove(objects.size()-1);
                                continue;
                            }
                            price  = "$" + price;


                            leaderboardItem = new LeaderboardItem(String.valueOf(i), price);
                            getUserAttributesFromOutfitId(outfitId, leaderboardItem, new UsernameCallback() {
                                @Override
                                public void onSuccess(String username, String i, String categoryValue) {

                                    LeaderboardItem l = new LeaderboardItem(i+1, categoryValue);
                                    l.setUsername(username);
                                    Log.d("Username", "Username: " + l.getUsername());
                                    objects.set(Integer.valueOf(i), l);

                                }

                                @Override
                                public void onError(String error) {
                                    Log.e("Username Error", "Error: " + error);

                                }
                                @Override
                                public void onFinish(){
                                    if (remainingRequests.decrementAndGet() == 0){
                                        updateAdapter(objects);
                                    }
                                }
                            });

                        }

                    }
                    //This is called outside of the loop for first category
                    if (spinnerPosition == 1){
                        updateAdapter(objects);
                    }

                }


                catch (JSONException e) {
                    Log.e("JSONArray Error", e.toString());
                }

                }


            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.d("Websocket", "Closed with reason: " + reason);
                //Handle UI updates or reconnection attempts if needed
                isConnected = false;


            }

            @Override
            public void onError(Exception ex) {
                Log.e("Websocket", "Error: " + ex.getMessage());
                //Show error dialog or retry connection

            }

        };

    }
    private void setUri() {
        try {
            uri = new URI(WEB_SOCKET_URL + UserManager.getUsername(this) + "/" + spinnerPosition);
        }catch (URISyntaxException e) {
            //throw new RuntimeException(e);
            Log.e("error", e.toString());
        }
    }

    private void connectWebSocketClient(){
        setUri();
        setWebSocketClient(uri);
        try {
            webSocketClient.connect();
        } catch (Exception e) {
            Log.e("WebSocket", "Connection failed: " + e.getMessage());
        }

    }



    /*
    When the activity stops, stop sending messages and disconnect websocket
     */

    @Override
    protected void onStop(){
        super.onStop();
        if (handler!=null){
            handler.removeCallbacks(continuousMessaging);
        }
        if (webSocketClient!=null){
            webSocketClient.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ensure WebSocket is closed when activity is destroyed
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.close();
            Log.d("WebSocket", "WebSocket closed in onDestroy.");
        }
    }
    private void getUserAttributesFromOutfitId(long outfitId, LeaderboardItem item, UsernameCallback callback){
        OutfitManager.getOutfitRequest(this, outfitId, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject user = response.getJSONObject("user");
                    String username = user.getString("username");
                    //Maybe user profile id instead!
                    long userId = user.getLong("userId");
                    Log.d("user", user.toString());

                    callback.onSuccess(username, item.getRank(), item.getCategoryValue());
                    callback.onFinish();

                } catch (JSONException e) {
                    callback.onError(e.toString());

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.toString());

            }
        });
    }
    private void getUsername(long userId, LeaderboardItem item, UsernameCallback callback){
        UserManager.getUserByIdRequest(this, String.valueOf(userId), MainActivity.SERVER_URL + "/users/", new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    String username = response.getString("username");
                    callback.onSuccess(username, item.getRank(), item.getCategoryValue());
                    callback.onFinish();

                } catch (JSONException e) {
                    callback.onError(e.toString());

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.toString());
            }
        });


    }
    private void updateAdapter(ArrayList<LeaderboardItem> objects){
        //Replace adapter items
        adapterItems = objects;
        Log.d("check", adapterItems.get(0).getUsername());
        try{
            runOnUiThread(() -> {
                setAdapter();
            });
        } catch (NullPointerException e) {
            Log.d("null adapter", "yes");
        }
    }

    private void setAdapter(){

        this.adapter = new LeaderboardRecyclerViewAdapter(adapterItems, new LeaderboardRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, LeaderboardItem item) {
                Log.d("LeaderboardActivity", "Clicked on user: " + item.getUsername());

            }
        });
        recyclerView.setAdapter(this.adapter);
        recyclerView.setHasFixedSize(false);
    }

    /*
    Note: Work on this design, should this be an interface with it's
    own file?
     */

    public interface UsernameCallback {
        void onSuccess(String username, String i, String value);
        void onError(String error);
        void onFinish();
    }

}


