package com.example.closetics.clothes;

import android.content.Context;
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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.ForgotPasswordFragment;
import com.example.closetics.MainActivity;
import com.example.closetics.R;
import com.example.closetics.UserManager;
import com.example.closetics.clothes.ClothesCreationBaseFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONObject;

import java.util.ArrayList;

//This activity holds the viewpager container, this

public class ClothesActivity extends AppCompatActivity {

    private Button addClothes;
    private Button editClothes;
    private Button viewClothes;
    private Button finalSubmission;
    private String[] inputArray = new String[NUM_FRAGMENTS];
    private static final int NUM_FRAGMENTS = 8;
    //Shared data for the fragments
    private ClothesDataViewModel clothesDataViewModel;

    private TabLayout tabLayout;
    private static final String URL = MainActivity.SERVER_URL + "/clothes";


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
        finalSubmission = findViewById(R.id.final_submission);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setVisibility(View.GONE);
        finalSubmission.setVisibility(View.GONE);




        viewPager = findViewById(R.id.pager);
        //This needs an innner class
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        ClothesActivity clothesActivity = this;


        //Set these invisible for now

        addClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityItemsVisibility();
                //Initialize view model with a new instance every time add clothes is clicked
                clothesDataViewModel = new ViewModelProvider(clothesActivity).get(ClothesDataViewModel.class);

                //Initialize the fragments list
                clothesDataViewModel.setFragmentsSize(NUM_FRAGMENTS);

                //Set the viewpager
                viewPager.setAdapter(pagerAdapter);
                //Set tab mediator
                new TabLayoutMediator(tabLayout, viewPager, ((tab, position) ->{
                    tab.setText(String.valueOf(position+1));
                } )).attach();

            }
        });

        viewClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });


        viewClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClothing(getApplicationContext(), UserManager.getUserID(getApplicationContext()), URL);
            }
        });
        finalSubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MutableLiveData<String>> fragments = clothesDataViewModel.getFragments();
                saveClothing(getApplicationContext(), fragments, URL, UserManager.getUserID(getApplicationContext()));

            }
        });

    }

    //Add more items if more added to activity
    private void activityItemsVisibility(){
        addClothes.setVisibility(View.GONE);
        editClothes.setVisibility(View.GONE);
        viewClothes.setVisibility(View.GONE);
        tabLayout.setVisibility(View.VISIBLE);
        finalSubmission.setVisibility(View.VISIBLE);
    }
    private void getClothing(Context context, Long userId, String URL){
        ClothesManager.getClothingByUserRequest(context, userId, URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Volley Response", response.toString());
                showFragment(response.toString());




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", error.toString());

            }
        });
    }

    private void saveClothing(Context context, ArrayList<MutableLiveData<String>> fragments, String URL, Long userId){
        ClothesManager.saveClothingRequest(context, fragments, userId,URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("Volley Response", response.toString());

                //Just send back to main for now!
                Intent intent = new Intent(context, ClothesActivity.class);
                startActivity(intent);

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error Response", error.toString());
            }
        });



    }
    private void showFragment(String get){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = ViewClothesFragment.newInstance(get);
        transaction.replace(R.id.pager, fragment, "view_clothes_fragment");
        transaction.commit();
        //Log.d("Fragment debug", String.valueOf(fragment.isAdded()));
    }


    //Inner class
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {

        public ScreenSlidePagerAdapter(ClothesActivity clothesActivity) {
            super(clothesActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Log.d("Check", String.valueOf(position));
            return ClothesCreationBaseFragment.newInstance(position, clothesDataViewModel);
        }

        @Override
        public int getItemCount() {
            return NUM_FRAGMENTS;
        }
    }

}



















