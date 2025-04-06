package com.example.closetics.recommendations;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.closetics.R;

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

        // TODO: change resId to proper image format
        holder.image.setImageResource(item.getImageId());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;

        public ViewHolder(final View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.rec_outfit_image_container);
        }
    }
}
