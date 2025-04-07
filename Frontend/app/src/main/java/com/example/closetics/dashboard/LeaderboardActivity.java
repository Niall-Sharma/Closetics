package com.example.closetics.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;

import java.net.URI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.closetics.MainActivity;
import com.example.closetics.R;
import com.example.closetics.UserManager;
import com.example.closetics.clothes.ClothesActivity;
import com.example.closetics.clothes.ClothesByTypeAdapter;
import com.example.closetics.clothes.ClothingItem;
import com.example.closetics.clothes.EditClothesActivity;

import java.net.URISyntaxException;
import java.nio.channels.AlreadyConnectedException;
import java.util.ArrayList;

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


    private int spinnerPosition; //Default to first spinner item
    private TextView categoryValue;
    private final String[] spinnerItems = new String[]
            {"Most Expensive Clothing", "Most Expensive Outfits", "Largest Closet" };

    private final String WEB_SOCKET_URL = MainActivity.SERVER_WS_URL +"/leaderboard/"; // + {username}/{type}



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

        /*
        Note this will have one more item
         */
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        leaderboardFilter.setAdapter(adapter);

        leaderboardFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            //Store chosen question
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerPosition = position + 1;
                chosenFilter = (String) parent.getItemAtPosition(position);
                categoryValue.setText(chosenFilter);
                if (isConnected){
                    webSocketClient.close();
                }
                connectWebSocketClient();
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
                if (webSocketClient!=null) {
                    webSocketClient.send(message);
                }

                //Delay of every 10 seconds
                handler.postDelayed(this, 10000);

            }
        };

        handler.post(continuousMessaging);





        //ArrayList<String> objects = getArguments().getStringArrayList("JSONObject");
        //ArrayList<ClothingItem> clothingItems = (ArrayList<ClothingItem>)getArguments().getSerializable("ClothingItems");
        //long [] clothingIds = getArguments().getLongArray("clothingIds");

        /*
        adapter = new ClothesByTypeAdapter(objects, new ClothesByTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, String jsonObject) {
                //Delete button on click logic very basic for roundtrip
                if (view.getId() == R.id.delete_button){
                    long clothingId = clothingIds[position];
                    Log.d("clothingId", String.valueOf(clothingId));
                    deleteClothing(getActivity(), clothingId, ClothesActivity.URL);
                    deleteItem(objects, position);


                }
                //Edit button on click logic
                else{
                    long clothingId = clothingIds[position];
                    ClothingItem clothingItem = clothingItems.get(position);

                    //Switch to editActivity
                    Intent intent = new Intent(getActivity(), EditClothesActivity.class);
                    intent.putExtra("clothingId", clothingId);
                    //Serializable
                    intent.putExtra("clothingItem", clothingItem);

                    startActivity(intent);


                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

         */


    }

    private void setWebSocketClient(URI uri){

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.d("Websocket", "Connected to Websocket server!");
                //Loading UI?
                //Connected UI?


            }

            @Override
            public void onMessage(String message) {
                Log.d("Websocket", "Message received: " + message);
                isConnected = true;

                if (message.equals("Username already exists")){
                    throw new AlreadyConnectedException();
                    //print an error message
                    //Do more?
                }

                //Grab the JSONArray
                try {
                    JSONArray leaderboardArray = new JSONArray(message);
                    //Parse the array and grab the fields needed for the recycler view
                    for (int i = 0; i < leaderboardArray.length(); i++){
                        Log.d("Object", leaderboardArray.get(i).toString());
                    }

                } catch (JSONException e) {
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


}
