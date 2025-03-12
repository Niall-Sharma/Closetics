package com.example.closetics.clothes;

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

    int [] imageResource;
    HashMap<Long,Long> counts;
    OnItemClickListener clickListener;


    public TypeGridRecyclerViewAdapter(HashMap<Long,Long> counts) {
        //this.imageResource = imageResource;
        this.counts = counts;

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
        //Sets the changing views from the innerclass

        //holder.imageView.setImageResource(imageResource[position]);
        //Note: 1 based indexing for the hashmap keys
        String type = MainActivity.CLOTHING_TYPES.get(position+1);
        holder.typeText.setText(MainActivity.CLOTHING_TYPES.get(position+1));
        String count = String.valueOf(counts.get(position+1));
        Log.d("need", count);
        if (count.equals("null")){
            count = "0";
        }
        Log.d("need", count);
        holder.countText.setText(count);


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

    public interface OnItemClickListener{
        void onItemClick();
    }

}
