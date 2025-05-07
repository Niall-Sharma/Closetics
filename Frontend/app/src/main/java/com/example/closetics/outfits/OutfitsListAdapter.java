package com.example.closetics.outfits;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.DashboardFragment;
import com.example.closetics.MainActivity;
import com.example.closetics.PublicProfileActivity;
import com.example.closetics.R;
import com.example.closetics.UserManager;
import com.example.closetics.recommendations.RecUsersListAdapter;
import com.example.closetics.recommendations.RecUsersListItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OutfitsListAdapter  extends ArrayAdapter<OutfitsListItem> {

    private Context context;
    private Activity activity;
    private boolean isSetDayOutfit, isTomorrow;

    public OutfitsListAdapter(Context context, List<OutfitsListItem> items, Activity activity) {
        super(context, 0, items);
        this.context = context;
        this.activity = activity;
        this.isSetDayOutfit = false;
    }

    public OutfitsListAdapter(Context context, List<OutfitsListItem> items, Activity activity, boolean isTomorrow) {
        super(context, 0, items);
        this.context = context;
        this.activity = activity;
        this.isSetDayOutfit = true;
        this.isTomorrow = isTomorrow;
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        OutfitsListItem item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_outfits, parent, false);
        }

        // Lookup views for data population
        TextView nameText = convertView.findViewById(R.id.outfit_list_item_name_text);
        TextView clothesText = convertView.findViewById(R.id.outfit_list_item_clothes_text);
        ImageButton editButton = convertView.findViewById(R.id.outfit_list_item_edit_button);
        ImageButton favoriteButton = convertView.findViewById(R.id.outfit_list_item_favorite_button);
        ImageButton chooseButton = convertView.findViewById(R.id.outfit_list_item_choose_button);
        RecyclerView clothesRecycler = convertView.findViewById(R.id.outfit_list_item_clothes_recycler);

        nameText.setText(item.getName());

        String clothesStr = "Clothes: ";
        List<OutfitClothesListItem> clothesItems = new ArrayList<>();
        for (int i = 0; i < item.getClothes().size(); i++) { // convert an list into comma-separated string
            clothesStr += item.getClothingName(i) + "; ";
            clothesItems.add(new OutfitClothesListItem(context, item.getClothingId(i)));
        }
//        clothes += item.getClothingName(item.getClothes().size() - 1);
        if (clothesItems.isEmpty()) {
            clothesStr += "None";
            clothesText.setText(clothesStr);

            // hide recycler
            clothesRecycler.setVisibility(TextView.GONE);
        } else {
            clothesText.setText(clothesStr);

            // init recycler
            clothesRecycler.setVisibility(TextView.VISIBLE);
            OutfitClothesListAdapter clothesAdapter = new OutfitClothesListAdapter(clothesItems);
            RecyclerView.LayoutManager clothesLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            clothesRecycler.setLayoutManager(clothesLayoutManager);
            clothesRecycler.setItemAnimator(new DefaultItemAnimator());
            clothesRecycler.setAdapter(clothesAdapter);
        }

        // buttons
        if (isSetDayOutfit) { // if choosing outfit for the day, show checkmark
            chooseButton.setVisibility(TextView.VISIBLE);
            favoriteButton.setVisibility(TextView.GONE);
            editButton.setVisibility(TextView.GONE);

            chooseButton.setOnClickListener(v -> {
                // send outfitId to main
                long selectedId = item.getId();
                if (isTomorrow) {
                    OutfitManager.saveTomorrowDailyOutfit(context, selectedId);
                }
                else {
                    OutfitManager.saveCurrentDailyOutfit(context, selectedId);
                    DashboardFragment.addWornToday(context, selectedId);
                }

                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
            });

        } else { // otherwise show normal buttons
            chooseButton.setVisibility(TextView.GONE);
            favoriteButton.setVisibility(TextView.VISIBLE);
            editButton.setVisibility(TextView.VISIBLE);

            editButton.setOnClickListener(v -> {
                Intent intent = new Intent(activity, EditOutfitActivity.class);
                intent.putExtra("OUTFIT_ID", item.getId());
                activity.startActivity(intent);
            });

            if (item.isFavorite()) {
                favoriteButton.setImageResource(R.drawable.star);
            } else {
                favoriteButton.setImageResource(R.drawable.star_outline);
            }
            favoriteButton.setOnClickListener(v -> {
                swapFavorite(favoriteButton, item);
            });

        }

        // Return the completed view to render on screen
        return convertView;
    }

    private void swapFavorite(ImageButton favoriteButton, OutfitsListItem item) {
        OutfitManager.swapFavoriteOnOutfitRequest(context, item.getId(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", "Successful favorite swapped " + item.getId());

                        item.setFavorite(!item.isFavorite());

                        if (item.isFavorite()) {
                            favoriteButton.setImageResource(R.drawable.star);
                        } else {
                            favoriteButton.setImageResource(R.drawable.star_outline);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error favorite swapping " + item.getId() + ", Error: " + error.toString());
                    }
                });
    }
}