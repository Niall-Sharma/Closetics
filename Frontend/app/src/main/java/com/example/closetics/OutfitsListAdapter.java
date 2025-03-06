package com.example.closetics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class OutfitsListAdapter  extends ArrayAdapter<OutfitsListItem> {

    public OutfitsListAdapter(Context context, List<OutfitsListItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        OutfitsListItem item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_outfits, parent, false);
        }

        // Lookup view for data population
        TextView nameText = convertView.findViewById(R.id.outfit_list_item_name);
        TextView clothesText = convertView.findViewById(R.id.outfit_list_item_clothes);

        // Populate the data into the template view using the data object
        nameText.setText(item.getName());

        List<String> clothesNames = item.getClothesNames();
        String clothes = "Clothes: ";
        for (int i = 0; i < clothesNames.size() - 1; i++) { // convert an array into comma-separated string
            clothes += clothesNames.get(i) + ", ";
        }
        clothes += clothesNames.get(clothesNames.size() - 1);
        clothesText.setText(clothes);

        // Return the completed view to render on screen
        return convertView;
    }
}