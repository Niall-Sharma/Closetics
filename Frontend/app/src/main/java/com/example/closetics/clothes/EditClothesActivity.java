package com.example.closetics.clothes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.MainActivity;
import com.example.closetics.R;
import com.example.closetics.UserManager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class EditClothesActivity extends AppCompatActivity {
    private Button finalSubmission;
    private TabLayout tabLayout;
    private ClothesDataViewModel clothesDataViewModel;
    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;
    private long clothingId;
    private ClothingItem clothingItem;









    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_clothes);

        finalSubmission = findViewById(R.id.submit_button);
        tabLayout = findViewById(R.id.tabLayout);

        viewPager = findViewById(R.id.edit_pager);

        //This needs an innner class
        pagerAdapter = new EditClothesActivity.ScreenSlidePagerAdapter(this);

        clothesDataViewModel = new ViewModelProvider(this).get(ClothesDataViewModel.class);

        //Initialize the fragments list
        clothesDataViewModel.setFragmentsSize(ClothesActivity.NUM_FRAGMENTS);

        //Set the viewpager
        viewPager.setAdapter(pagerAdapter);
        //Set tab mediator
        new TabLayoutMediator(tabLayout, viewPager, ((tab, position) ->{
            tab.setText(String.valueOf(position+1));
        } )).attach();

        //Set the clothingId and clothingItem instance variables
        Intent intent = getIntent();

        clothingId = intent.getLongExtra("clothingId", 0);
        clothingItem = (ClothingItem) intent.getSerializableExtra("clothingItem");

        finalSubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<MutableLiveData<String>> fragments = clothesDataViewModel.getFragments();
                getAndUpdateClothing(getApplicationContext(), ClothesActivity.URL, fragments);


            }
        });




    }

    private void getAndUpdateClothing(Context context, String URL, ArrayList<MutableLiveData<String>> fragments) {
        ClothesManager.getClothingRequest(context, clothingId, URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Volley Response", response.toString());
                JSONObject updateObject;
                try {
                    updateObject = putUpdatedFields(response, fragments);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                updateClothing(context, updateObject, URL);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", error.toString());
            }
        });
    }




    private void updateClothing(Context context, JSONObject object, String URL){
        ClothesManager.updateClothingRequest(context, object, URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Volley Response", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", error.toString());
            }
        });


    }


    private JSONObject putUpdatedFields(JSONObject response, ArrayList<MutableLiveData<String>> fragments) throws JSONException {
        String favorite = fragments.get(0).getValue();
        String size = fragments.get(1).getValue();
        String color = fragments.get(2).getValue();
        String dateBought = fragments.get(3).getValue();
        String brand = fragments.get(6).getValue();
        String itemName = fragments.get(5).getValue();
        String material = fragments.get(7).getValue();
        String price = fragments.get(4).getValue();

        if (favorite!=null) {
            if (favorite.toLowerCase().trim().equals("true")) {
                ClothesManager.nullCheck("favorite", true, response);
            } else if (favorite.toLowerCase().trim().equals("false")){
                ClothesManager.nullCheck("favorite", false, response);
            }
        }

        ClothesManager.nullCheck("size", size, response);
        ClothesManager.nullCheck("color", color, response);
        ClothesManager.nullCheck("dateBought", dateBought, response);
        ClothesManager.nullCheck("brand", brand, response);
        ClothesManager.nullCheck("itemName", itemName, response);
        ClothesManager.nullCheck("material", material, response);
        ClothesManager.nullCheck("price", price, response);

        return response;

    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {

        public ScreenSlidePagerAdapter(EditClothesActivity editClothesActivity) {
            super(editClothesActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {

            return EditClothesFragment.newInstance(position, clothesDataViewModel, clothingItem);
        }

        @Override
        public int getItemCount() {

            return ClothesActivity.NUM_FRAGMENTS;
        }
    }




}
