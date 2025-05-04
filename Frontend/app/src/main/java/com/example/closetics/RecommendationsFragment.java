package com.example.closetics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.recommendations.RecOutfitsListAdapter;
import com.example.closetics.recommendations.RecOutfitsListItem;
import com.example.closetics.recommendations.RecUsersListAdapter;
import com.example.closetics.recommendations.RecUsersListItem;
import com.example.closetics.recommendations.RecWebSocketService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * One of the Fragments of the MainActivity.
 * Contains the recommendations feed with outfits
 * and user search.
 */
public class RecommendationsFragment extends Fragment {

    private final String[] INT_TO_MONTH = {
            "January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"
    };

    private SearchView search;
    private ImageButton backButton;
    private RecyclerView outfitsRecycler, usersRecycler;

    private RecOutfitsListAdapter outfitsAdapter;
    private RecUsersListAdapter usersAdapter;

    private boolean isStateOutfits;
    private boolean isReceiving;
    private boolean isAddingRecommendationsInProgress;
    private HashSet<Long> likedOutfitIds;

    // For receiving new outfits from websocket
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");

            getActivity().runOnUiThread(() -> {
                addMoreRecommendations(message);
            });
        }
    };

    /**
     * Required empty public constructor
     */
    public RecommendationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommendations, container, false);

        // TODO: redirect to login when not logged in

        // attempt to start websocket
        startWebSocket();

        // initialize UI elements
        search = view.findViewById(R.id.rec_search);
        search.setQuery("", false); // clear search
        backButton = view.findViewById(R.id.rec_back_button);
        outfitsRecycler = view.findViewById(R.id.rec_outfits_recycler);
        usersRecycler = view.findViewById(R.id.rec_users_recycler);

        isStateOutfits = true;
        updateState();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(messageReceiver,
                new IntentFilter("RecWebSocketMessageReceived"));
        Log.d("RecommendationsFragment", "Receiver registered");
        isReceiving = true;

        isAddingRecommendationsInProgress = false;

        // initialize recycler views
        outfitsAdapter = new RecOutfitsListAdapter(new ArrayList<RecOutfitsListItem>());
        RecyclerView.LayoutManager outfitsLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        outfitsRecycler.setLayoutManager(outfitsLayoutManager);
        outfitsRecycler.setItemAnimator(new DefaultItemAnimator());
        outfitsRecycler.setAdapter(outfitsAdapter);

        usersAdapter = new RecUsersListAdapter(new ArrayList<RecUsersListItem>(), item -> {
            if (item.getId() == UserManager.getUserID(getActivity().getApplicationContext())) {
                // open your profile if clicked on your username
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("OPEN_FRAGMENT", 3); // open fragment Profile
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), PublicProfileActivity.class);
                intent.putExtra("USER_ID", item.getId());
                startActivity(intent);
            }
            //Toast.makeText(getContext(), "Clicked: " + item.getUsername(), Toast.LENGTH_SHORT).show();
        });
        RecyclerView.LayoutManager usersLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        usersRecycler.setLayoutManager(usersLayoutManager);
        usersRecycler.setItemAnimator(new DefaultItemAnimator());
        usersRecycler.setAdapter(usersAdapter);

        // onClick logic
        search.setOnSearchClickListener(v -> {
            if (isStateOutfits) {
                isStateOutfits = false;
                updateState();
            }
        });

        backButton.setOnClickListener(v -> {
            isStateOutfits = true;
            usersAdapter.clearItems(); // clear search results
            search.setQuery("", false); // clear search
            search.setIconified(true); // collapse searchbar
            updateState();
        });

        // load more outfits after scrolling far enough
        outfitsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                // if user sees one before last outfit
                if (!isAddingRecommendationsInProgress && ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition() >= outfitsAdapter.getItemCount() - 2) {
                    isAddingRecommendationsInProgress = true; // prevent sending requests until previous one finishes
                    requestMoreRecommendations();
                }
            }
        });

        // search users logic
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.replaceAll("[^a-zA-Z0-9]", ""); // remove all invalid chars
                if (newText.length() > 1) {
                    searchUsers(newText);
                } else {
                    usersAdapter.clearItems(); // clear search if less than 2 chars inputted
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                search.clearFocus();
                return true;
            }
        });

        requestMoreRecommendations();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isReceiving) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(messageReceiver,
                    new IntentFilter("RecWebSocketMessageReceived"));
            Log.d("RecommendationsFragment", "Started, Receiver registered");
            isReceiving = true;
        }
    }

    @Override
    public void onStop() {
        if (isReceiving) {
            super.onStop();
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(messageReceiver);
            Log.d("RecommendationsFragment", "Stopped, Receiver unregistered");
            isReceiving = false;
        }
    }

    /**
     * Starts the Recommendations WebSocket if the user is logged in.
     * Should be called every time RecommendationsFragment fragment is open in case it
     * was accidentally closed / not properly opened before.
     */
    private void startWebSocket() {
        Intent serviceIntent = new Intent(getActivity(), RecWebSocketService.class);
        serviceIntent.setAction("RecWebSocketConnect");
        getActivity().startService(serviceIntent);
        Log.d("RecommendationsFragment", "WebSocket started");
    }

    /**
     * Requests more recommended outfits through the WebSocket
     * to display in the feed.
     */
    private void requestMoreRecommendations() {
        Intent intent = new Intent("RecWebSocketSendMessage");
        intent.putExtra("message", "5"); // put a number of outfits here
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

        Log.d("RecommendationsFragment", "Requested more outfits: 5");
    }

    /**
     * Takes a JSON array with Outfits received from the WebSocket and
     * add them to the end of the current recommendations feed.
     *
     * @param message a JSON array of Outfits in the String format
     */
    private void addMoreRecommendations(String message) {
        // Mock outfits
//        RecOutfitsListItem mockOutfit = new RecOutfitsListItem(1, "My old shoes", "bob002", Arrays.asList(R.drawable.clothing_mock_img, R.drawable.clothing_mock_img, R.drawable.clothing_mock_img), "Very expensive", "February 3, 1976", false);
//        for (int i = 0; i < 10; i++) {
//            outfitsAdapter.addItem(mockOutfit);
//        }

        Log.d("RecommendationsFragment", "Received outfits: " + message);

        JSONArray outfitObjects;
        try {
            outfitObjects = new JSONArray(message);
        } catch (JSONException e) {
            Log.e("RecommendationsFragment Error", "JSON Exception from received outfits: " + e.toString());
            isAddingRecommendationsInProgress = false;
            return;
        }

        for (int i = 0; i < outfitObjects.length(); i++) {
            try {
                JSONObject outfit = outfitObjects.getJSONObject(i);

                long id = outfit.getLong("outfitId");
                String name = outfit.getString("outfitName");
                String username = outfit.getJSONObject("user").getString("username");
                boolean isLiked = false; // actual like info requested in adapter

                //String stats = "No stats for now";
                JSONObject stats = outfit.getJSONObject("outfitStats");
                String statsStr = "Wearing statistics:\n   Times worn: " + stats.getInt("timesWorn") +
                        "\n   Average high temperature: " + (Math.round(stats.getDouble("avgHighTemp") * 10.0) / 10.0) + " °F" +
                        "\n   Average low temperature: " + (Math.round(stats.getDouble("avgLowTemp") * 10.0) / 10.0) + " °F";

                int year = outfit.getJSONArray("creationDate").getInt(0);
                int month = outfit.getJSONArray("creationDate").getInt(1);
                int day = outfit.getJSONArray("creationDate").getInt(2);
                //int year = 2025, month = 4, day = 6;
                String date = INT_TO_MONTH[month - 1] + " " + day + ", " + year;

                // TODO: add actual images
                List<Integer> imageIds = Arrays.asList(R.drawable.clothing_mock_img, R.drawable.clothing_mock_img, R.drawable.clothing_mock_img);

                outfitsAdapter.addItem(new RecOutfitsListItem(getActivity(), id, name, username, imageIds, statsStr, date, isLiked));
            } catch (JSONException e) {
                Log.e("RecommendationsFragment Error", "JSON Exception. Could not add outfit " + i + ": " + e.toString());
            }
        }

        isAddingRecommendationsInProgress = false;
    }

    /**
     * Makes visible either Outfits feed or User search depending
     * on the valuse of isStateOutfits boolean variable:
     * - true  - Outfits feed;
     * - false - User search.
     */
    private void updateState() {
        if (isStateOutfits) {
            backButton.setVisibility(TextView.GONE);
            usersRecycler.setVisibility(TextView.GONE);
            outfitsRecycler.setVisibility(TextView.VISIBLE);
        } else {
            backButton.setVisibility(TextView.VISIBLE);
            usersRecycler.setVisibility(TextView.VISIBLE);
            outfitsRecycler.setVisibility(TextView.GONE);
        }
    }

    /**
     * Sends an HTTP request to get an array of users whose usernames
     * contain given String as a substring.
     * Then displays new search results.
     *
     * @param username substring of the username to search for
     */
    private void searchUsers(String username) {
        UserManager.searchUsersByUsernameRequest(getActivity().getApplicationContext(), username,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d("Volley Response", "Successful Search by username " + username + ": " + response.toString());

                            ArrayList<RecUsersListItem> usersListItems = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject user = response.getJSONObject(i);
                                usersListItems.add(new RecUsersListItem(user.getLong("userId"), user.getString("username")));
                            }

                            usersAdapter.setNewItems(usersListItems);
                        }
                        catch (JSONException e) {
                            Log.e("JSON Error", "Search users by username " + username + " Error: " + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Get user by username " + username + " Error: " + error.toString());
                    }
                });
    }
}