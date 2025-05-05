package com.example.closetics.clothes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.closetics.MainActivity;
import com.example.closetics.R;

import java.util.ArrayList;

/**
 * Adapter class for displaying a list of clothing items in a RecyclerView.
 * This adapter binds data from a list of strings to the views in the RecyclerView items.
 * It provides two buttons (edit and delete) for each item, and sets click listeners on them.
 */
public class ClothesByTypeAdapter extends RecyclerView.Adapter<ClothesByTypeAdapter.MyViewHolder>{

    private ArrayList<ClothingItem> objects; // List of strings representing clothing items
    private OnItemClickListener clickListener; // Listener to handle item clicks

    /**
     * Constructor for the ClothesByTypeAdapter.
     *
     * @param objects The list of strings representing clothing items.
     * @param clickListener The listener to handle item click events.
     */
    public ClothesByTypeAdapter(ArrayList<ClothingItem> objects, OnItemClickListener clickListener) {
        this.objects = objects;
        this.clickListener = clickListener;
    }

    /**
     * Creates a new ViewHolder instance and inflates the layout for the RecyclerView item.
     *
     * @param parent The parent ViewGroup.
     * @param viewType The view type of the item.
     * @return A new MyViewHolder instance.
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_clothing_item, parent, false);
        return new MyViewHolder(view);
    }

    /**
     * Binds the data to the views for a given position in the RecyclerView.
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int realPosition = holder.getBindingAdapterPosition(); // Get the actual position of the item
        ClothingItem item = objects.get(realPosition);
        //Log.d("image",item.getImage());
        if (item.getImage() != null) {
            holder.imageView.setImageBitmap(resizeWithAspectRatio(item.getImage(), 154, 160));
        }
        holder.name.setText(item.getItemName()); // Set the text for the TextViews
        Integer type = Math.toIntExact(item.getType());
        Integer specialType = Math.toIntExact(item.getSpecialType());
        holder.type.setText(MainActivity.CLOTHING_TYPES.get(type));
        holder.specialType.setText(MainActivity.CLOTHING_SPECIAL_TYPES.get(specialType));
        holder.price.setText(item.getPrice());
        holder.material.setText(item.getMaterial());
        holder.brand.setText(item.getBrand());
        holder.favorite.setText(item.getFavorite());
        holder.size.setText(item.getSize());
        holder.color.setText(item.getColor());
        holder.dateBought.setText(item.getDateBought());

        // Set click listener for the edit button
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(realPosition, v, item);
            }
        });

        // Set click listener for the delete button
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(realPosition, v, item);
            }
        });
    }

    public static Bitmap resizeWithAspectRatio(byte[] imageData, int maxWidth, int maxHeight) {
        Bitmap original = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        int width = original.getWidth();
        int height = original.getHeight();

        float ratio = Math.min((float) maxWidth / width, (float) maxHeight / height);
        int newWidth = Math.round(width * ratio);
        int newHeight = Math.round(height * ratio);

        return Bitmap.createScaledBitmap(original, newWidth, newHeight, true);
    }




    /**
     * Returns the total number of items in the data set.
     *
     * @return The number of items in the data set.
     */
    @Override
    public int getItemCount() {
        return objects.size();
    }

    /**
     * ViewHolder class for holding references to the views in the RecyclerView item.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private Button editButton; // Button for editing the item
        private Button deleteButton; // Button for deleting the item
        private TextView name; // TextView for displaying the clothing item
        private TextView type;
        private TextView specialType;
        private TextView price;
        private TextView material;
        private TextView brand;
        private TextView favorite;
        private TextView size;
        private TextView color;
        private TextView dateBought;
        private ImageView imageView;


        /**
         * Constructor for the ViewHolder.
         *
         * @param itemView The view for the RecyclerView item.
         */
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            editButton = itemView.findViewById(R.id.view_profile); // Initialize the edit button
            deleteButton = itemView.findViewById(R.id.delete_button); // Initialize the delete button
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            specialType = itemView.findViewById(R.id.specialType);
            price = itemView.findViewById(R.id.price);
            material = itemView.findViewById(R.id.material);
            brand = itemView.findViewById(R.id.brand);
            favorite = itemView.findViewById(R.id.favorite);
            size = itemView.findViewById(R.id.size);
            color = itemView.findViewById(R.id.color);
            dateBought = itemView.findViewById(R.id.dateBought);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    /**
     * Interface for handling item click events.
     */
    public interface OnItemClickListener {
        /**
         * Called when an item is clicked, either the edit or delete button.
         *
         * @param position The position of the clicked item.
         * @param view The view that was clicked.
         */
        void onItemClick(int position, View view, ClothingItem item);
    }
}
