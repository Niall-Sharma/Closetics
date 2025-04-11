package com.example.closetics.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.closetics.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class StatisticsRecyclerViewAdapter extends RecyclerView.Adapter<StatisticsRecyclerViewAdapter.MyViewHolder>{
    /*
    Baseline clothes stats adapter
     */
    ArrayList<JSONObject> objects;

    public StatisticsRecyclerViewAdapter(ArrayList<JSONObject> objects){
        this.objects = objects;
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
        holder.object.setText(objects.get(realPosition).toString());

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView object;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //object = itemView.findViewById(R.id.stat_object);

        }
    }
}
