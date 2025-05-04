package com.example.closetics.clothes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.closetics.R;

import java.util.ArrayList;

/**
 * Adapter class for displaying a list of clothing items in a RecyclerView.
 * This adapter binds data from a list of strings to the views in the RecyclerView items.
 * It provides two buttons (edit and delete) for each item, and sets click listeners on them.
 */
public class ClothesByTypeAdapter extends RecyclerView.Adapter<ClothesByTypeAdapter.MyViewHolder>{

    private ArrayList<String> objects; // List of strings representing clothing items
    private OnItemClickListener clickListener; // Listener to handle item clicks

    /**
     * Constructor for the ClothesByTypeAdapter.
     *
     * @param objects The list of strings representing clothing items.
     * @param clickListener The listener to handle item click events.
     */
    public ClothesByTypeAdapter(ArrayList<String> objects, OnItemClickListener clickListener) {
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
        String jsonObject = objects.get(realPosition); // Get the clothing item from the list
        holder.object.setText(jsonObject); // Set the text for the TextView
        Log.d("JSON object binding", jsonObject);

        // Set click listener for the edit button
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(realPosition, v, jsonObject);
            }
        });

        // Set click listener for the delete button
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(realPosition, v, jsonObject);
            }
        });
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
        private TextView object; // TextView for displaying the clothing item

        /**
         * Constructor for the ViewHolder.
         *
         * @param itemView The view for the RecyclerView item.
         */
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            editButton = itemView.findViewById(R.id.view_profile); // Initialize the edit button
            deleteButton = itemView.findViewById(R.id.delete_button); // Initialize the delete button
            object = itemView.findViewById(R.id.rank_value); // Initialize the TextView
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
         * @param jsonObject The string data associated with the clicked item.
         */
        void onItemClick(int position, View view, String jsonObject);
    }
}
