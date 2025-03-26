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
                //udpateClothing(getApplicationContext(), clothingId, ClothesActivity.URL, UserManager.getUserID(getApplicationContext()));


            }
        });




    }

    private void updateClothing(Context context, long clothingId, Map<String, Object> updatedFields, String URL) {
        ClothesManager.updateClothingRequest(context, clothingId, updatedFields, URL, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Update Response", response.toString());


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley Update Error", error.toString());
                    }
                }
        );
    }

    /*
Inner class for screen sliding
 */
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
