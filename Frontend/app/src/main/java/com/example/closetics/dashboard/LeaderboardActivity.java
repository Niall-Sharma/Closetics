package com.example.closetics.dashboard;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.closetics.R;
import com.example.closetics.UserManager;
import com.example.closetics.clothes.ClothesActivity;
import com.example.closetics.clothes.ClothesByTypeAdapter;
import com.example.closetics.clothes.ClothingItem;
import com.example.closetics.clothes.EditClothesActivity;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {

    /*
    Websocket client
     */
    private WebSocketClient webSocketClient;


    private Spinner leaderboardFilter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ClothesByTypeAdapter adapter;
    private String chosenFilter;
    private TextView categoryValue;
    private final String[] spinnerItems = new String[]
            {"Most Expensive Clothing", "Most Expensive Outfits", "Largest Closet" };

    private final String WEB_SOCKET_URL = "ws://localhost:8080/leaderboard/";


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
        Creating the websocket client/handshake
         */
        setWebSocketClient();
        try {
            webSocketClient.connect();
        } catch (Exception e) {
            Log.e("WebSocket", "Connection failed: " + e.getMessage());
        }


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
                chosenFilter = (String) parent.getItemAtPosition(position);
                categoryValue.setText(chosenFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                chosenFilter= (String)parent.getItemAtPosition(0);
                categoryValue.setText(chosenFilter);

            }
        });
        



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
    private void setWebSocketClient(){
        try {

            URI uri = new URI(WEB_SOCKET_URL + UserManager.getUsername(this));

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

                    if (message.equals("Username already exists")){
                        //print an error message

                    }
                    else{
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
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("Websocket", "Closed with reason: " + reason);
                    //Handle UI updates or reconnection attempts if needed


                }

                @Override
                public void onError(Exception ex) {
                    Log.e("Websocket", "Error: " + ex.getMessage());
                    //Show error dialog or retry connection

                }
            };
        } catch (URISyntaxException e) {
            //throw new RuntimeException(e);
            Log.e("error", e.toString());
        }
    }


}
