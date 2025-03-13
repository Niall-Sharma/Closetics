package com.example.closetics.clothes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.example.closetics.MainActivity;
import com.example.closetics.R;
import com.example.closetics.UserManager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

//This activity holds the viewpager container, this

public class ClothesActivity extends AppCompatActivity {

    private static HashMap<Long,Long> clothingTypeCounts = new HashMap<>();
    private Button addClothes;
    private Button editClothes;
    private Button viewClothes;
    private Button finalSubmission;
    private Button deleteClothes;
    private CardView card;

    private String[] inputArray = new String[NUM_FRAGMENTS];

    private Button clothesActivityBack;
    private Button mainActivityBack;

    /*
    Recycler View
     */
    private RecyclerView gridRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TypeGridRecyclerViewAdapter gridRecyclerViewAdapter;


    private static final int NUM_FRAGMENTS = 8;
    //Shared data for the fragments
    private ClothesDataViewModel clothesDataViewModel;

    private TabLayout tabLayout;
    public static final String URL = MainActivity.SERVER_URL + "/clothes";


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
        card = findViewById(R.id.card_view);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setVisibility(View.GONE);
        finalSubmission.setVisibility(View.GONE);

        gridRecyclerView = findViewById(R.id.type_grid);
        //Makes the layout a grid
        layoutManager = new GridLayoutManager(this, 2);
        //Set layout manager
        gridRecyclerView.setLayoutManager(layoutManager);

        //Wait until we get a reponse to execute more code

        gridRecyclerViewAdapter = new TypeGridRecyclerViewAdapter(clothingTypeCounts, new TypeGridRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(long position) {
                getClothingByType(getApplicationContext(), UserManager.getUserID(getApplicationContext()), URL, position);

            }
        });

        gridRecyclerView.setAdapter(gridRecyclerViewAdapter);

        gridRecyclerView.setHasFixedSize(true);





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

        Context context = this;
        finalSubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MutableLiveData<String>> fragments = clothesDataViewModel.getFragments();
                saveClothing(getApplicationContext(), fragments, URL, UserManager.getUserID(getApplicationContext()));
                //Call get clothing before constructing adapter so that we can update the counts
                ClothesActivity.getUserClothing(context, UserManager.getUserID(getApplicationContext()), URL);

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
        gridRecyclerView.setVisibility(View.GONE);
        card.setVisibility(View.GONE);

    }

    private void getClothingByType(Context context, long userId, String URL, long type){

        ClothesManager.getClothingByTypeRequest(context, userId, URL, type, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                 /*
                Note on the response we need to parse the JSON array
                 */
                Log.d("Volley Response", response.toString());
                ArrayList<String> responseStringArray = new ArrayList<>();
                long[] clothingIds = new long[response.length()];
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        responseStringArray.add(jsonObject.toString());
                       //Check
                        long clothingId = jsonObject.getLong("clothesId");
                        clothingIds[i] = clothingId;
                        Log.d("JSON Object", jsonObject.toString());
                    }
                    showFragment(responseStringArray, clothingIds);
                } catch (JSONException e) {
                    Log.d("JSON exception", e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley Error", error.toString());
            }
        });
    }


    public static void getUserClothing(Context context, long userId, String URL){
        ClothesManager.getClothingByUserRequest(context, userId, URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                /*
                Note on the response we need to parse the JSON array
                 */
                Log.d("Volley Response", response.toString());
                clothingTypeCounts.clear();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        long key = jsonObject.getLong("clothingType");
                        incrementKeyValue(clothingTypeCounts, key);
                        Log.d("JSON Object", jsonObject.toString());
                    }
                    Intent intent = new Intent(context, ClothesActivity.class);
                    context.startActivity(intent);
                } catch (JSONException e) {
                    Log.d("JSON exception", e.toString());
                }


            }}, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error user clothing", error.toString());

            }
        });
    }

    private void saveClothing(Context context, ArrayList<MutableLiveData<String>> fragments, String URL, Long userId){
        ClothesManager.saveClothingRequest(context, fragments, userId,URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("Volley Response", response.toString());


            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error Response", error.toString());
            }
        });

    }

    private static void incrementKeyValue(HashMap<Long, Long> map, Long key){
        //If the map has the key, increment its value (the count)
        if (key == 0){
            return;
        }
        if (map.containsKey((key))){
            map.put(key, map.get(key) +1);
            Log.d("check", map.get(key).toString());

        }
        //If not put the new key with count of 1
        else{
            map.put(key,Long.valueOf(1));
        }
    }

    private void showFragment(ArrayList<String> JSONObject, long[] clothingIds){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = ViewClothesFragment.newInstance(JSONObject, clothingIds);
        transaction.replace(R.id.view_clothes_container, fragment, "view_clothes_fragment");
        transaction.commit();
        //Log.d("Fragment debug", String.valueOf(fragment.isAdded()));
    }




    /*
    Inner class for screen sliding
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {

        public ScreenSlidePagerAdapter(ClothesActivity clothesActivity) {
            super(clothesActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return ClothesCreationBaseFragment.newInstance(position, clothesDataViewModel);
        }

        @Override
        public int getItemCount() {
            return NUM_FRAGMENTS;
        }
    }

}



















