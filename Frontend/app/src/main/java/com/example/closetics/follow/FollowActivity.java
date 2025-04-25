package com.example.closetics.follow;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.closetics.R;
import com.google.android.material.tabs.TabLayout;

/**
 * Activity that displays 2 tabs with followers and following lists
 * of a user with passed id.
 *
 * Requires "USER_ID" extra parameter of type long
 * Requires "IS_FOLLOWING" extra parameter of type boolean
 *  - true  - initially display following list
 *  - false - initially display followers list
 */
public class FollowActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private FollowTabAdapter tabAdapter;

    private boolean isFollowing;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        // initialize ui
        tabLayout = findViewById(R.id.follow_tab_layout);
        viewPager2 = findViewById(R.id.follow_viewPager2);
        tabAdapter = new FollowTabAdapter(this);
        viewPager2.setAdapter(tabAdapter);

        Bundle extras = getIntent().getExtras();
        isFollowing = true;
        userId = -1;
        if (extras != null) {
            isFollowing = extras.getBoolean("IS_FOLLOWING", true);
            userId = extras.getLong("USER_ID", -1);
        }

        // set listeners
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        // switch to Followers tab by default if needed
        if (!isFollowing) {
            tabLayout.getTabAt(1).select();
        }
    }
}