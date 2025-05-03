package com.example.closetics.recommendations;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.MainActivity;
import com.example.closetics.PublicProfileActivity;
import com.example.closetics.R;
import com.example.closetics.UserManager;
import com.example.closetics.outfits.OutfitManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecOutfitsListAdapter extends RecyclerView.Adapter<RecOutfitsListAdapter.ViewHolder> {
    private List<RecOutfitsListItem> items;

    public RecOutfitsListAdapter(List<RecOutfitsListItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_rec_outfits, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecOutfitsListItem item = items.get(position);

        long id = item.getId();
        String name = item.getName();
        String username = item.getUsername();
        List<Integer> imageIds = item.getImageIds();
        String stats = item.getStats();
        String date = item.getDate();

        holder.nameText.setText(name);
        holder.usernameText.setText(username);
        holder.statsText.setText(stats);
        holder.dateText.setText("Posted on " + date);

        // init like button functionality
        initLike(holder, item);

        // set images
        // TODO: set proper images format here
        // holder.image.setImageResource(imageIds.get(0));
        ArrayList<RecImagesListItem> imagesListItems = new ArrayList<>();
        for (int imageId : item.getImageIds()) {
            imagesListItems.add(new RecImagesListItem(imageId));
        }
        RecImagesListAdapter imagesListAdapter = new RecImagesListAdapter(imagesListItems);
        holder.viewPager2.setAdapter(imagesListAdapter);

        holder.usernameText.setOnClickListener(v -> {
            if (UserManager.getUsername(item.getContext()).equals(item.getUsername())) {
                // open your profile if clicked on your username
                Intent intent = new Intent(item.getContext(), MainActivity.class);
                intent.putExtra("OPEN_FRAGMENT", 3); // open fragment Profile
                item.getActivity().startActivity(intent);
            } else {
                Intent intent = new Intent(item.getContext(), PublicProfileActivity.class);
                intent.putExtra("USER_ID", item.getId());
                item.getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(RecOutfitsListItem item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    private void initLike(@NonNull ViewHolder holder, RecOutfitsListItem item) {
        OutfitManager.isLikedOutfitRequest(item.getContext(), item.getId(), UserManager.getUserID(item.getContext()),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley Response", "Successful get like info of " + item.getId());

                        // set initial like state
                        item.setLiked("true".equals(response));

                        // set like icon
                        if (item.isLiked()) {
                            holder.likeButton.setImageResource(R.drawable.heart);
                        } else {
                            holder.likeButton.setImageResource(R.drawable.heart_outline);
                        }

                        // set like listener only if got correct like info
                        holder.likeButton.setOnClickListener(v -> {
                            item.setLiked(!item.isLiked());
                            if (item.isLiked()) {
                                holder.likeButton.setImageResource(R.drawable.heart);
                                like(item.getContext(), item.getId());
                            } else {
                                holder.likeButton.setImageResource(R.drawable.heart_outline);
                                unlike(item.getContext(), item.getId());
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error getting like info of " + item.getId() + ", Error: " + error.toString());
                    }
                });
    }

    private void like(Context context, long outfitId) {
        OutfitManager.addLikeRequest(context, outfitId, UserManager.getUserID(context),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", "Successful liked " + outfitId);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error liking " + outfitId + ", Error: " + error.toString());
                    }
                });
    }

    private void unlike(Context context, long outfitId) {
        OutfitManager.addLikeRequest(context, outfitId, UserManager.getUserID(context),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", "Successful unliked " + outfitId);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error unliking " + outfitId + ", Error: " + error.toString());
                    }
                });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText, usernameText, statsText, dateText;
        private ViewPager2 viewPager2;
        private ImageButton likeButton;

        public ViewHolder(final View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.rec_outfit_list_item_outfit_name_text);
            usernameText = itemView.findViewById(R.id.rec_outfit_list_item_username_text);
            statsText = itemView.findViewById(R.id.rec_outfit_list_item_stats_text);
            dateText = itemView.findViewById(R.id.rec_outfit_list_item_date_text);
            viewPager2 = itemView.findViewById(R.id.rec_outfit_list_item_viewPager2);
            likeButton = itemView.findViewById(R.id.rec_outfit_list_item_like_button);
        }
    }
}
