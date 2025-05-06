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

import com.android.volley.NetworkResponse;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * ClothesActivity is responsible for managing the clothing-related
 * functionalities within the application, including viewing, adding, and
 * editing clothing items.
 *
 * It displays a grid of clothing types, handles camera interactions,
 * fragment-based clothing creation via ViewPager2, and communicates with
 * the backend to fetch and store clothing data.
 *
 * Main features:
 * <ul>
 *     <li>Displays clothing items by type</li>
 *     <li>Opens a camera interface</li>
 *     <li>Creates new clothing entries using fragments</li>
 *     <li>Submits clothing entries to server</li>
 * </ul>
 */
public class ClothesActivity extends AppCompatActivity {

    /**
     * Stores the count of each clothing type keyed by their ID.
     */
    private static HashMap<Long, Long> clothingTypeCounts = new HashMap<>();

    private Button addClothes;
    private Button testCamera;
    private Button finalSubmission;
    private CardView card;

    private Button clothesActivityBack;
    private Button mainActivityBack;

    private RecyclerView gridRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TypeGridRecyclerViewAdapter gridRecyclerViewAdapter;

    public static final int NUM_FRAGMENTS = 10;
    private ClothesDataViewModel clothesDataViewModel;

    private TabLayout tabLayout;
    public static final String URL = MainActivity.SERVER_URL;

    private ViewPager2 viewPager;
    private CustomSlideAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes);

        addClothes = findViewById(R.id.add_clothes);
        clothesActivityBack = findViewById(R.id.activity_back);
        clothesActivityBack.setVisibility(View.GONE);
        testCamera = findViewById(R.id.view_clothes);
        finalSubmission = findViewById(R.id.final_submission);
        card = findViewById(R.id.card_view);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setVisibility(View.GONE);
        finalSubmission.setVisibility(View.GONE);

        gridRecyclerView = findViewById(R.id.type_grid);
        layoutManager = new GridLayoutManager(this, 2);
        gridRecyclerView.setLayoutManager(layoutManager);

        gridRecyclerViewAdapter = new TypeGridRecyclerViewAdapter(clothingTypeCounts, new TypeGridRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(long position) {
                getClothingByType(getApplicationContext(), UserManager.getUserID(getApplicationContext()), URL, position);
            }
        });
        gridRecyclerView.setAdapter(gridRecyclerViewAdapter);
        gridRecyclerView.setHasFixedSize(true);

        viewPager = findViewById(R.id.edit_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        ClothesActivity clothesActivity = this;

        addClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityItemsVisibility();

                clothesDataViewModel = new ViewModelProvider(clothesActivity).get(ClothesDataViewModel.class);
                clothesDataViewModel.setFragmentsSize(NUM_FRAGMENTS);
                viewPager.setAdapter((ScreenSlidePagerAdapter) pagerAdapter);

                new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
                    tab.setText("•");
                }).attach();
            }
        });

        Context context = this;
        finalSubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MutableLiveData<String>> fragments = clothesDataViewModel.getFragments();
                //If an image was set/taken
                saveClothing(getApplicationContext(), fragments, URL, UserManager.getUserID(getApplicationContext()));
                //Need to add the image to the new clothing item


            }
        });

        testCamera.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        clothesActivityBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClothesActivity.class);
            startActivity(intent);
        });
    }


    /**
     * Hides certain UI elements and shows the fragment creation views.
     */
    private void activityItemsVisibility() {
        addClothes.setVisibility(View.GONE);
        testCamera.setVisibility(View.GONE);
        tabLayout.setVisibility(View.VISIBLE);
        finalSubmission.setVisibility(View.VISIBLE);
        clothesActivityBack.setVisibility(View.VISIBLE);
        gridRecyclerView.setVisibility(View.GONE);
        card.setVisibility(View.GONE);
    }

    /**
     * Fetches clothing items by a specific type from the server and displays them in a fragment.
     *
     * @param context  the application context
     * @param userId   the ID of the user
     * @param URL      the server URL
     * @param type     the type of clothing to fetch
     */
    private void getClothingByType(Context context, long userId, String URL, long type) {
        ClothesManager.getClothingByTypeRequest(context, userId, URL + "/getClothing", type, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Volley Response", response.toString());
                ArrayList<String> responseStringArray = new ArrayList<>();
                ArrayList<ClothingItem> responseClothingItems = new ArrayList<>();
                Set<Long> clothingIds =  new HashSet<>();

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        responseStringArray.add(jsonObject.toString());
                        getImageByClothing(jsonObject.getLong("clothesId"), jsonObject, i, new ImageCallback() {
                            @Override
                            public void onImageLoaded(ClothingItem clothingItem, int i) throws JSONException {
                                long clothingId = jsonObject.getLong("clothesId");
                                clothingIds.add(clothingId);
                                responseClothingItems.add(clothingItem);

                                if (responseClothingItems.size() == response.length()){
                                    showFragment(responseStringArray, clothingIds, responseClothingItems);
                                }
                            }
                        });

                    }
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

    /**
     * Parses a JSONObject into a ClothingItem object.
     *
     * @param response the JSONObject representing a clothing item
     * @return a ClothingItem instance
     * @throws JSONException if parsing fails
     */
    private ClothingItem createClothingItem(JSONObject response, byte[] image) throws JSONException {
        Long type = response.getLong("clothingType");
        Long specialType = response.getLong("specialType");
        Long id = response.getLong("clothesId");
        String favorite = String.valueOf(response.getBoolean("favorite"));
        String brand = response.getString("brand");
        String color = response.getString("color");
        String dateBought = response.getString("dateBought");
        String itemName = response.getString("itemName");
        String material = response.getString("material");
        String size = response.getString("size");
        String price = response.getString("price");
        return new ClothingItem(id, type, specialType, image,favorite, size, color, dateBought, brand, itemName, material, price);
    }

    /**
     * Fetches all clothing items for a user and updates the clothing type counts.
     *
     * @param context the application context
     * @param userId  the ID of the user
     * @param URL     the server URL
     */
    public static void getUserClothing(Context context, long userId, String URL) {
        ClothesManager.getClothingByUserRequest(context, userId, URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
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
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error user clothing", error.toString());
            }
        });
    }

    /**
     * Saves clothing entries created via the fragments.
     *
     * @param context   the application context
     * @param fragments the clothing data from fragments
     * @param URL       the server URL
     * @param userId    the ID of the user
     */
    private void saveClothing(Context context, ArrayList<MutableLiveData<String>> fragments, String URL, Long userId) {
        ClothesManager.saveClothingRequest(context, fragments, userId, URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //the response is a json object! the json object we just created! we need the
                //clothing id of this item so that we can add the image!
                Log.d("Save Clothing response", response.toString());
                try {
                    Long id = response.getLong("clothesId");
                    if (fragments.get(0).getValue() != null) {
                        addImage(id);
                    }
                    getUserClothing(getApplicationContext(), UserManager.getUserID(getApplicationContext()), URL);

                } catch (JSONException e) {
                    Log.e("ID error", e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error Response", error.toString());
            }
        });
    }
    private void addImage(long clothingId){
        ClothesManager.addImage(this, clothingId, ClothesCreationBaseFragment.byteArray, MainActivity.SERVER_URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.d("Add image good", response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Add image bad", error.toString());

            }
        });

    }
    private void getImageByClothing(long clothingId, JSONObject object, int i, ImageCallback callback){
        ClothesManager.getImageByClothing(this, clothingId, MainActivity.SERVER_URL, new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {
                Log.d("Get Image Success", Arrays.toString(response));
                byte[] image = response;
                try {
                    callback.onImageLoaded(createClothingItem(object, image), i);
                } catch (JSONException e) {
                    Log.e("Error", e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Get Image error", error.toString());
                //On an error, assume that there is no image for this clothing item
                try {
                    callback.onImageLoaded(createClothingItem(object, null), i);
                } catch (JSONException e) {
                    Log.e("Error", e.toString());
                }
            }
        });

    }

    /**
     * Increments the value for a given key in the map or initializes it if not present.
     *
     * @param map the HashMap to update
     * @param key the key to increment
     */
    private static void incrementKeyValue(HashMap<Long, Long> map, Long key) {
        if (key == 0) {
            return;
        }
        if (map.containsKey(key)) {
            map.put(key, map.get(key) + 1);
            Log.d("check", map.get(key).toString());
        } else {
            map.put(key, 1L);
        }
    }

    /**
     * Displays the clothing items inside a fragment.
     *
     * @param JSONObject     the list of clothing JSON strings
     * @param clothingIds    the array of clothing item IDs
     * @param clothingItems  the list of ClothingItem objects
     */
    private void showFragment(ArrayList<String> JSONObject, Set<Long> clothingIds, ArrayList<ClothingItem> clothingItems) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = ViewClothesFragment.newInstance(JSONObject, clothingItems);
        transaction.replace(R.id.view_clothes_container, fragment, "view_clothes_fragment");
        transaction.commit();
    }

    public interface ImageCallback {
        void onImageLoaded(ClothingItem clothingItem, int i) throws JSONException;
    }

    /**
     * Inner class for managing fragment creation inside the ViewPager2 widget.
     */
    public static class ScreenSlidePagerAdapter extends FragmentStateAdapter implements CustomSlideAdapter{

        public final Map<Integer, Fragment> fragmentMap = new HashMap<>();


        /**
         * Constructs a new ScreenSlidePagerAdapter.
         *
         * @param clothesActivity the parent activity
         */
        public ScreenSlidePagerAdapter(ClothesActivity clothesActivity) {
            super(clothesActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = ClothesCreationBaseFragment.newInstance(position);
            fragmentMap.put(position, fragment);
            return fragment;
        }

        @Override
        public int getItemCount() {
            return NUM_FRAGMENTS;
        }

        @Override
        public Fragment getFragment(int position) {
            return fragmentMap.get(position);
        }
        @Override
        public void update(int position){
            notifyItemChanged(10);
        }
    }
}




















