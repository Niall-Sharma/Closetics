package com.example.closetics.recommendations;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.closetics.R;
import com.example.closetics.clothes.ClothesManager;

import java.util.List;

public class RecImagesListAdapter extends RecyclerView.Adapter<RecImagesListAdapter.ViewHolder> {
    private List<RecImagesListItem> items;

    public RecImagesListAdapter(List<RecImagesListItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecImagesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_rec_images, parent, false);
        return new RecImagesListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecImagesListAdapter.ViewHolder holder, int position) {
        RecImagesListItem item = items.get(position);

        //holder.image.setImageResource(item.getImageId());
        setClothingImage(holder, item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void setClothingImage(@NonNull RecImagesListAdapter.ViewHolder holder, RecImagesListItem item) {
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

            image = itemView.findViewById(R.id.rec_outfit_image_container);
        }
    }
}
