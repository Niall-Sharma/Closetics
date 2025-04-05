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

public class RecOutfitsListAdapter extends RecyclerView.Adapter<RecOutfitsListAdapter.RecOutfitsViewHolder> {
    private List<RecOutfitsListItem> items;

    public RecOutfitsListAdapter(List<RecOutfitsListItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecOutfitsListAdapter.RecOutfitsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_rec_outfits, parent, false);
        return new RecOutfitsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecOutfitsListAdapter.RecOutfitsViewHolder holder, int position) {
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
        // holder.image.setImageResource(imageIds.get(0)); // TODO: set images here
        holder.image.setImageResource(R.drawable.clothing_mock_img);
        if (item.isLiked()) {
            holder.likeButton.setImageResource(R.drawable.heart);
        } else {
            holder.likeButton.setImageResource(R.drawable.heart_outline);
        }

        holder.usernameText.setOnClickListener(v -> {
            // TODO: open public profile by username
        });

        holder.likeButton.setOnClickListener(v -> {
            item.setLiked(!item.isLiked());
            if (item.isLiked()) {
                holder.likeButton.setImageResource(R.drawable.heart);
            } else {
                holder.likeButton.setImageResource(R.drawable.heart_outline);
            }
            // TODO: make like http req
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

    public class RecOutfitsViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText, usernameText, statsText, dateText;
        private ImageView image; // TODO: Replace with ViewPager2
        private ImageButton likeButton;

        public RecOutfitsViewHolder(final View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.rec_outfit_list_item_outfit_name_text);
            usernameText = itemView.findViewById(R.id.rec_outfit_list_item_username_text);
            statsText = itemView.findViewById(R.id.rec_outfit_list_item_stats_text);
            dateText = itemView.findViewById(R.id.rec_outfit_list_item_date_text);
            image = itemView.findViewById(R.id.rec_outfit_list_item_image);
            likeButton = itemView.findViewById(R.id.rec_outfit_list_item_like_button);
        }
    }
}
