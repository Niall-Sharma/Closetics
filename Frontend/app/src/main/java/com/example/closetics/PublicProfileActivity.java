package com.example.closetics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.follow.FollowActivity;
import com.example.closetics.follow.FollowManager;
import com.example.closetics.outfits.OutfitManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PublicProfileActivity extends AppCompatActivity {

    private TextView usernameText, nameText;
    private Button clothesButton, outfitsButton, followingButton, followersButton, followButton;
    private RecyclerView outfitRecycler;

    private long userId;
    private boolean meIsFollowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_profile);

        // get userId whose profile we are displaying
        Bundle extras = getIntent().getExtras();
        userId = -1;
        if (extras != null) {
            userId = extras.getLong("USER_ID", -1);
        }

        // initialize ui
        usernameText = findViewById(R.id.public_profile_username_text);
        nameText = findViewById(R.id.public_profile_name_text);
        clothesButton = findViewById(R.id.public_profile_clothes_button);
        outfitsButton = findViewById(R.id.public_profile_outfits_button);
        followingButton = findViewById(R.id.public_profile_following_button);
        followersButton = findViewById(R.id.public_profile_followers_button);
        followButton = findViewById(R.id.public_profile_follow_button);
        outfitRecycler = findViewById(R.id.public_profile_outfits_recycler);

        if (userId == -1) {
            Log.e("PublicProfileActivity", "User Id not passed");
        } else {
            populateUserData(userId);
        }
    }

    private void populateUserData(long userId) {
        // set listeners only if user id was passed
        followingButton.setOnClickListener(v -> {
            Intent intent = new Intent(PublicProfileActivity.this, FollowActivity.class);
            intent.putExtra("IS_FOLLOWING", true);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        followersButton.setOnClickListener(v -> {
            Intent intent = new Intent(PublicProfileActivity.this, FollowActivity.class);
            intent.putExtra("IS_FOLLOWING", false);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        // populate all needed data with requests
        populateFollow(userId);

        populateClothes(userId);

        populateOutfits(userId);
    }

    private void populateClothes(long userId) {
        FollowManager.getAllUserClothesRequest(getApplicationContext(), userId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", "Successful get clothes of user " + userId + ": " + response.toString());

                        // setup clothes button (no listeners!)
                        clothesButton.setText(response.length() + " clothes");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Get clothes of " + userId + " user Error: " + error.toString());
                    }
                });
    }

    private void populateOutfits(long userId) {
        OutfitManager.getAllOutfitsRequest(getApplicationContext(), userId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", "Successful get outfits of user " + userId + ": " + response.toString());

                        // setup outfits button (no listeners!)
                        outfitsButton.setText(response.length() + " outfits");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Get outfits of " + userId + " user Error: " + error.toString());
                    }
                });
    }

    private void populateFollow(long userId) {
        UserManager.getUserProfileRequest(getApplicationContext(), userId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Volley Response", "Successful get profile of user " + userId + ": " + response.toString());

                            long myUserId = UserManager.getUserID(getApplicationContext());
                            JSONArray followingArray = response.getJSONArray("following");
                            JSONArray followersArray = response.getJSONArray("followers");
                            // check if logged in user is following this user
                            meIsFollowing = false;
                            for (int i = 0; i < followersArray.length(); i++) {
                                if (followersArray.getJSONObject(i).getLong("id") == myUserId) {
                                    meIsFollowing = true;
                                    break;
                                }
                            }

                            if (response.has("username")) {
                                usernameText.setText(response.getString("username"));
                                // TODO: get actual name with another request
                                nameText.setText(response.getString("username"));
                            }

                            // setup following/followers buttons (no listeners!)
                            followingButton.setText(followingArray.length() + " following");
                            followersButton.setText(followersArray.length() + " followers");

                            // setup follow button
                            followButton.setText(meIsFollowing ? "Following" : "Follow");
                            followButton.setOnClickListener(v -> {
                                if (meIsFollowing) {
                                    unfollow(userId);
                                    followButton.setText("Follow");
                                } else {
                                    follow(userId);
                                    followButton.setText("Following");
                                }
                                meIsFollowing = !meIsFollowing;
                            });
                        }
                        catch (JSONException e) {
                            Log.e("JSON Error", "Get profile of user " + userId + " Error: " + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Get profile of " + userId + " user Error: " + error.toString());
                    }
                });
    }

    private void follow(long followingId) {
        UserManager.addFollowingRequest(getApplicationContext(), UserManager.getUserID(getApplicationContext()), followingId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", "Successful followed " + followingId);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error following " + followingId + ", Error: " + error.toString());
                    }
                });
    }

    private void unfollow(long followingId) {
        UserManager.removeFollowingRequest(getApplicationContext(), UserManager.getUserID(getApplicationContext()), followingId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", "Successful unfollowed " + followingId);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error unfollowing " + followingId + ", Error: " + error.toString());
                    }
                });
    }
}