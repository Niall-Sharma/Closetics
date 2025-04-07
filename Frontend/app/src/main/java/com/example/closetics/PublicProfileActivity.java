package com.example.closetics;

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

import com.example.closetics.follow.FollowActivity;

public class PublicProfileActivity extends AppCompatActivity {

    private TextView usernameText, nameText;
    private Button clothesButton, outfitsButton, followingButton, followersButton, followButton;
    private RecyclerView outfitRecycler;

    private long userId;

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


    }
}