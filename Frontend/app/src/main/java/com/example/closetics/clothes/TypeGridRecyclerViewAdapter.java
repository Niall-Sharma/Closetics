package com.example.closetics.clothes;

import static android.app.PendingIntent.getActivity;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.closetics.MainActivity;
import com.example.closetics.R;

import java.util.HashMap;

public class TypeGridRecyclerViewAdapter extends RecyclerView.Adapter<TypeGridRecyclerViewAdapter.MyViewHolder> {

    int [] imageResources = {
            R.drawable.accessories_icon,
            R.drawable.activewear_icon,
            R.drawable.bottoms_icon,
            R.drawable.dress_icon,
            R.drawable.footwear_icon,
            R.drawable.formalwear_icon,
            //Outerwear icon here:
            R.drawable.outerwear_icon,
            R.drawable.seasonal_icon,
            R.drawable.sleepwear_icon,
            R.drawable.tops_icon,
            R.drawable.undergarments_icon,
            R.drawable.workwear_icon
    };
    HashMap<Long,Long> counts;
    OnItemClickListener clickListener;


    public TypeGridRecyclerViewAdapter(HashMap<Long,Long> counts, OnItemClickListener clickListener) {
        //this.imageResource = imageResource;
        this.counts = counts;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_grid_type, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        long longRealPosition = holder.getBindingAdapterPosition();
        int realPosition = holder.getBindingAdapterPosition();

        //Set the proper imageView drawable
        holder.imageView.setImageResource(imageResources[realPosition]);

        //holder.imageView.setImageResource(imageResource[position]);
        //Note: 1 based indexing for the hashmap keys
        holder.typeText.setText(MainActivity.CLOTHING_TYPES.get(realPosition+1));
        String count = String.valueOf(counts.get(longRealPosition+1));

        if (count.equals("null")){
            count = "0";
        }
        holder.countText.setText(count);

        //Only if not zero
        if (!count.equals("0")) {
            //This sets the clicklistener on the entire item in the list
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Send over the position of the item in list + 1 for the hashmap
                    clickListener.onItemClick(realPosition + 1);
                }
            });
       }


    }

    @Override
    public int getItemCount() {
        return MainActivity.CLOTHING_TYPES.size();
    }

    /*
    Inner class for view_grid_type
     */

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView typeText;
        TextView countText;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            typeText = itemView.findViewById(R.id.type_text);
            countText = itemView.findViewById(R.id.count_text);
        }
    }
    /*
    Must implement click listener to instantiate the class
     */



    //Inner interface
    public interface OnItemClickListener{
        void onItemClick(long position);
    }

}
