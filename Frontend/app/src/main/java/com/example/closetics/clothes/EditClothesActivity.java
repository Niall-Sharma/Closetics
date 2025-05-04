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
import java.util.HashMap;
import java.util.Map;

/**
 * Activity that allows the user to edit the details of a clothing item.
 * This activity utilizes a ViewPager2 to navigate between different fragments, where each fragment
 * corresponds to a different field of the clothing item to be edited. It also handles the submission
 * of the updated data to the backend.
 */


public class EditClothesActivity extends AppCompatActivity {
    private Button finalSubmission;
    private TabLayout tabLayout;
    private ClothesDataViewModel clothesDataViewModel;
    private ViewPager2 viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;
    private long clothingId;
    private ClothingItem clothingItem;

    /**
     * Called when the activity is created. Initializes the layout, fragments, and setup necessary
     * for the activity. Sets up a ViewPager2 with fragments, a TabLayout for tab navigation,
     * and sets up a click listener for the submission button.
     *
     * @param savedInstanceState Bundle containing any saved state from previous instances of this activity.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_clothes);

        finalSubmission = findViewById(R.id.submit_button);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.edit_pager);

        // Initialize the pager adapter and set it for the ViewPager
        pagerAdapter = new EditClothesActivity.ScreenSlidePagerAdapter(this);
        clothesDataViewModel = new ViewModelProvider(this).get(ClothesDataViewModel.class);

        // Initialize the fragments size
        clothesDataViewModel.setFragmentsSize(ClothesActivity.NUM_FRAGMENTS);

        // Set the ViewPager adapter and TabLayout mediator
        viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager, ((tab, position) -> {
            tab.setText(String.valueOf(position + 1));
        })).attach();

        // Set the clothingId and clothingItem instance variables from the Intent
        Intent intent = getIntent();
        clothingId = intent.getLongExtra("clothingId", 0);
        clothingItem = (ClothingItem) intent.getSerializableExtra("clothingItem");

        // Set onClickListener for the final submission button
        finalSubmission.setOnClickListener(v -> {
            ArrayList<MutableLiveData<String>> fragments = clothesDataViewModel.getFragments();
            getAndUpdateClothing(getApplicationContext(), ClothesActivity.URL, fragments);
        });
    }

    /**
     * Sends a request to retrieve the current details of the clothing item, then updates the
     * item with the new data from the fragments and submits the update.
     *
     * @param context The context from which the request is made.
     * @param URL The URL for the clothing item data.
     * @param fragments The list of fragments containing the updated data.
     */
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

    /**
     * Sends a request to update the clothing item on the server with the modified data.
     *
     * @param context The context from which the request is made.
     * @param object The updated clothing item data.
     * @param URL The URL for the update request.
     */
    private void updateClothing(Context context, JSONObject object, String URL) {
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

    /**
     * Updates the clothing item fields in the response object with values from the fragments.
     * Null values are handled by the ClothesManager's nullCheck method.
     *
     * @param response The current clothing item data.
     * @param fragments The list of fragments with updated data.
     * @return The updated response object with new clothing details.
     * @throws JSONException If there is an error processing the JSON data.
     */
    private JSONObject putUpdatedFields(JSONObject response, ArrayList<MutableLiveData<String>> fragments) throws JSONException {
        String favorite = fragments.get(0).getValue();
        String size = fragments.get(1).getValue();
        String color = fragments.get(2).getValue();
        String dateBought = fragments.get(3).getValue();
        String brand = fragments.get(6).getValue();
        String itemName = fragments.get(5).getValue();
        String material = fragments.get(7).getValue();
        String price = fragments.get(4).getValue();

        // Update fields with non-null values
        if (favorite != null) {
            if (favorite.toLowerCase().trim().equals("true")) {
                ClothesManager.nullCheck("favorite", true, response);
            } else if (favorite.toLowerCase().trim().equals("false")) {
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

    /**
     * Adapter for the ViewPager2 that manages the fragments for editing the clothing item.
     */
    public class ScreenSlidePagerAdapter extends FragmentStateAdapter implements CustomSlideAdapter{

        private final Map<Integer, Fragment> fragmentMap = new HashMap<>();

        /**
         * Constructor for the pager adapter.
         *
         * @param editClothesActivity The parent activity for the ViewPager2.
         */
        public ScreenSlidePagerAdapter(EditClothesActivity editClothesActivity) {
            super(editClothesActivity);
        }

        /**
         * Creates a new fragment for the given position.
         *
         * @param position The position of the fragment in the ViewPager2.
         * @return A new instance of the EditClothesFragment for the given position.
         */
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = EditClothesFragment.newInstance(position, clothesDataViewModel, clothingItem, pagerAdapter);
            fragmentMap.put(position, fragment);
            return fragment;
        }

        /**
         * Returns the total number of fragments in the adapter.
         *
         * @return The number of fragments.
         */
        @Override
        public int getItemCount() {
            return ClothesActivity.NUM_FRAGMENTS;
        }
        @Override
        public Fragment getFragment(int position) {
            return fragmentMap.get(position);
        }
    }
}







