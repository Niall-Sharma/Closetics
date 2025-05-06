package com.example.closetics.outfits;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.MainActivity;
import com.example.closetics.PublicProfileActivity;
import com.example.closetics.R;
import com.example.closetics.UserManager;
import com.example.closetics.outfits.ProfileOutfitsListAdapter;
import com.example.closetics.outfits.ProfileOutfitsListItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileOutfitsListAdapter extends RecyclerView.Adapter<ProfileOutfitsListAdapter.ViewHolder> {
    private List<ProfileOutfitsListItem> items;
    private boolean isMyOutfits;

    public ProfileOutfitsListAdapter(List<ProfileOutfitsListItem> items) {
        this.items = items;
        this.isMyOutfits = false;
    }

    public ProfileOutfitsListAdapter(List<ProfileOutfitsListItem> items, boolean isMyOutfits) {
        this.items = items;
        this.isMyOutfits = isMyOutfits;
    }

    @NonNull
    @Override
    public ProfileOutfitsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_profile_outfits, parent, false);
        return new ProfileOutfitsListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileOutfitsListAdapter.ViewHolder holder, int position) {
        ProfileOutfitsListItem item = items.get(position);

        long outfitId = item.getId();
        String name = item.getName();
        List<Long> clothingIds = item.getClothingIds();

        holder.nameText.setText(name);

        List<OutfitClothesListItem> clothesItems = new ArrayList<>();
        for (long clothingId : item.getClothingIds()) {
            clothesItems.add(new OutfitClothesListItem(item.getContext(), clothingId));
        }
        if (clothesItems.isEmpty()) {
            // hide recycler
            holder.recycler.setVisibility(TextView.GONE);
        } else {
            // init recycler
            holder.recycler.setVisibility(TextView.VISIBLE);
            OutfitClothesListAdapter clothesAdapter = new OutfitClothesListAdapter(clothesItems);
            RecyclerView.LayoutManager clothesLayoutManager = new LinearLayoutManager(item.getContext(), LinearLayoutManager.HORIZONTAL, false);
            holder.recycler.setLayoutManager(clothesLayoutManager);
            holder.recycler.setItemAnimator(new DefaultItemAnimator());
            holder.recycler.setAdapter(clothesAdapter);
        }

        if (!isMyOutfits) {
            // hide buttons if not my profile is being displayed
            holder.editButton.setVisibility(TextView.GONE);
            holder.favoriteButton.setVisibility(TextView.GONE);
        } else {
            // init buttons
            holder.editButton.setVisibility(TextView.VISIBLE);
            holder.favoriteButton.setVisibility(TextView.VISIBLE);

            holder.editButton.setOnClickListener(v -> {
                Intent intent = new Intent(item.getActivity(), EditOutfitActivity.class);
                intent.putExtra("OUTFIT_ID", item.getId());
                item.getActivity().startActivity(intent);
            });

            if (item.isFavorite()) {
                holder.favoriteButton.setImageResource(R.drawable.star);
            } else {
                holder.favoriteButton.setImageResource(R.drawable.star_outline);
            }
            holder.favoriteButton.setOnClickListener(v -> {
                swapFavorite(holder, item);
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(ProfileOutfitsListItem item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    private void swapFavorite(@NonNull ProfileOutfitsListAdapter.ViewHolder holder, ProfileOutfitsListItem item) {
        OutfitManager.swapFavoriteOnOutfitRequest(item.getContext(), item.getId(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", "Successful favorite swapped " + item.getId());

                        item.setFavorite(!item.isFavorite());

                        if (item.isFavorite()) {
                            holder.favoriteButton.setImageResource(R.drawable.star);
                        } else {
                            holder.favoriteButton.setImageResource(R.drawable.star_outline);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText;
        private RecyclerView recycler;
        private ImageButton editButton, favoriteButton;

        public ViewHolder(final View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.profile_outfit_list_item_name_text);
            recycler = itemView.findViewById(R.id.profile_outfit_list_item_recycler);
            editButton = itemView.findViewById(R.id.profile_outfit_list_item_edit_button);
            favoriteButton = itemView.findViewById(R.id.profile_outfit_list_item_favorite_button);
        }
    }
}