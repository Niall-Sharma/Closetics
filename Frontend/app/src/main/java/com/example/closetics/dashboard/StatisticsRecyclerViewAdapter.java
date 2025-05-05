package com.example.closetics.dashboard;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.closetics.R;

import org.json.JSONException;

import java.util.ArrayList;

public class StatisticsRecyclerViewAdapter extends RecyclerView.Adapter<StatisticsRecyclerViewAdapter.MyViewHolder>{
    /*
    Baseline clothes stats adapter
     */
    ArrayList<ClothingStatItem> objects;
    boolean which;

    public StatisticsRecyclerViewAdapter(ArrayList<ClothingStatItem> objects, boolean b){
        this.objects = objects;
        which = b;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_recycler_view_item, parent, false);
        StatisticsRecyclerViewAdapter.MyViewHolder myViewHolder = new StatisticsRecyclerViewAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int realPosition = holder.getBindingAdapterPosition();

        //Set the text view in the recycler view
        ClothingStatItem item = objects.get(realPosition);
        if (which){
            try {
                holder.timesWorn.setText(item.getTimesWorn());
                holder.highTemp.setText(item.getAvgHighTemp());
                holder.lowTemp.setText(item.getAvgLowTemp());
                holder.name.setText(item.getName());
            } catch (JSONException e) {
                Log.e("exception", e.toString());
            }

        }
        else{
            try {
                String s = "Number Of Outfits In: ";
                holder.timesWorn.setText(item.getTimesWorn());
                holder.outfitsIn.setText(item.getNumberOfOutfitsIn());
                holder.highTemp.setText(item.getAvgHighTemp());
                holder.lowTemp.setText(item.getAvgLowTemp());
                holder.prompt.setText(s);
                holder.name.setText(item.getName());
            } catch (JSONException e) {
                Log.e("exception", e.toString());
            }

        }


    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView timesWorn;
        private TextView lowTemp;
        private TextView highTemp;
        private TextView outfitsIn;
        private TextView name;
        private TextView prompt;
        private ImageView image;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            timesWorn = itemView.findViewById(R.id.specialType);
            lowTemp = itemView.findViewById(R.id.low_temp);
            highTemp = itemView.findViewById(R.id.high_temp);
            outfitsIn = itemView.findViewById(R.id.outfits_in);
            name = itemView.findViewById(R.id.name);
            prompt = itemView.findViewById(R.id.numberOutfitsPrompt);

        }
    }
}
