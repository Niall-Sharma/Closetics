package com.example.closetics.outfits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.closetics.R;

import java.util.List;

public class EditOutfitClothingListAdapter extends ArrayAdapter<EditOutfitClothingListItem> {



    public EditOutfitClothingListAdapter(Context context, List<EditOutfitClothingListItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        EditOutfitClothingListItem item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_edit_outfit_clothing, parent, false);
        }

        // Lookup view for data population
        TextView nameText = convertView.findViewById(R.id.edit_outfit_clothing_list_item_name_text);
        TextView colorText = convertView.findViewById(R.id.edit_outfit_clothing_list_item_color_text);
        TextView typeText = convertView.findViewById(R.id.edit_outfit_clothing_list_item_type_text);
        TextView specialTypeText = convertView.findViewById(R.id.edit_outfit_clothing_list_item_special_type_text);
        Button deleteButton = convertView.findViewById(R.id.edit_outfit_clothing_list_item_delete_button);

        deleteButton.setOnClickListener(v -> {
            // delete this item
            OutfitManager.removeClothingRequest();
            remove(item);
        });

        // Populate the data into the template view using the data object
        nameText.setText(item.getName());

        String clothes = "Clothes: ";
        for (int i = 0; i < item.getClothes().length() - 1; i++) { // convert an array into comma-separated string
            clothes += item.getClothingName(i) + ", ";
        }
        clothes += clothesNames.get(clothesNames.size() - 1);
        clothesText.setText(clothes);

        // Return the completed view to render on screen
        return convertView;
    }
}