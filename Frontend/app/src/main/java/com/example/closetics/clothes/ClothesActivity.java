package com.example.closetics.clothes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.closetics.R;
import com.example.closetics.clothes.ClothesCreationBaseFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

//This activity holds the viewpager container, this

public class ClothesActivity extends AppCompatActivity {

    private Button addClothes;
    private Button editClothes;
    private Button viewClothes;
    public static ArrayList<String> inputArray;
    private static final int NUM_FRAGMENTS = 8;

    private TabLayout tabLayout;


    //For the view pager
    //Handles the animation and swiping back and forth between fragments
    private ViewPager2 viewPager;
    //Provides the fragments to the viewpager widget
    private FragmentStateAdapter pagerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes);

        addClothes = findViewById(R.id.add_clothes);
        editClothes = findViewById(R.id.edit_clothes);
        viewClothes = findViewById(R.id.view_clothes);

        tabLayout = findViewById(R.id.tabLayout);




        viewPager = findViewById(R.id.pager);
        //This needs an innner class
        pagerAdapter = new ScreenSlidePagerAdapter(this);




        //Set these invisible for now


        addClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set the viewpager
                viewPager.setAdapter(pagerAdapter);
                //Set tab mediator
                new TabLayoutMediator(tabLayout, viewPager, ((tab, position) ->{
                    tab.setText(String.valueOf(position+1));
                } )).attach();
            }

        });

    }

    //Add more items if more added to activity
    private void activityItemsInvisible(){
        addClothes.setVisibility(View.GONE);
        editClothes.setVisibility(View.GONE);
        viewClothes.setVisibility(View.GONE);
    }


    //Inner class
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {

        public ScreenSlidePagerAdapter(ClothesActivity clothesActivity) {
            super(clothesActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return new ClothesCreationBaseFragment();
        }

        @Override
        public int getItemCount() {
            return NUM_FRAGMENTS;
        }
    }
}



















