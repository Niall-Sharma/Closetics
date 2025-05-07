package com.example.closetics.outfits;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.R;
import com.example.closetics.clothes.ClothesManager;
import com.example.closetics.recommendations.RecImagesListAdapter;
import com.example.closetics.recommendations.RecImagesListItem;

import java.util.List;

public class OutfitClothesListAdapter extends RecyclerView.Adapter<OutfitClothesListAdapter.ViewHolder> {
    private List<OutfitClothesListItem> items;

    public OutfitClothesListAdapter(List<OutfitClothesListItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public OutfitClothesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_outfit_clothes_images, parent, false);
        return new OutfitClothesListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OutfitClothesListAdapter.ViewHolder holder, int position) {
        OutfitClothesListItem item = items.get(position);

        setClothingImage(holder, item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void setClothingImage(@NonNull OutfitClothesListAdapter.ViewHolder holder, OutfitClothesListItem item) {
        ClothesManager.getClothingImage(item.getContext(), item.getClothingId(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        Log.d("Volley Response", "Successfully get image of clothing " + item.getClothingId());

                        // Display the image in the ImageView
                        if (response != null) {
                            holder.image.setImageBitmap(response);
                        } else {
                            // display default image
                            holder.image.setImageResource(R.drawable.clothing_mock_img); // TODO: set actual default img
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error getting image of clothing: " + item.getClothingId() + ": " + error.toString());

                        // display default image
                        holder.image.setImageResource(R.drawable.clothing_mock_img); // TODO: set actual default img
                    }
                });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;

        public ViewHolder(final View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.outfit_clothes_image_container);
        }
    }
}