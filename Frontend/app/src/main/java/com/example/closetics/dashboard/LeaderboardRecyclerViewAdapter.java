package com.example.closetics.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.closetics.R;

import java.util.ArrayList;

public class LeaderboardRecyclerViewAdapter extends RecyclerView.Adapter<LeaderboardRecyclerViewAdapter.MyViewHolder> {


    private ArrayList<LeaderboardItem> adapterItems;
    OnItemClickListener clickListener;

    public LeaderboardRecyclerViewAdapter (ArrayList<LeaderboardItem> adapterItems, OnItemClickListener clickListener){
        this.adapterItems = adapterItems;
        this.clickListener = clickListener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_recyclerview_item, parent, false);
        LeaderboardRecyclerViewAdapter.MyViewHolder myViewHolder = new LeaderboardRecyclerViewAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int realPosition = holder.getBindingAdapterPosition();
        try {
            LeaderboardItem item = adapterItems.get(realPosition);
            //Rank
            String correctRank = item.getRank();//.substring(1);
            holder.rank.setText(correctRank);
            //Log.d("username", item.getUsername());
            holder.username.setText(item.getUsername());
            holder.usernameButton.setText(item.getUsername());
            holder.categoryValue.setText(item.getCategoryValue());


            holder.usernameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(realPosition, v, item);
                }
            });

        } catch (NullPointerException e) {
           throw new NullPointerException();
        }

    }

    @Override
    public int getItemCount() {
        /*
        Currently set to 10 in the backend
         */
        return adapterItems.size();
    }
    public void updateAdapter(){

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private Button usernameButton;
        private TextView username;
        private TextView rank;
        private TextView categoryValue;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameButton = itemView.findViewById(R.id.username_button);
            username = itemView.findViewById(R.id.username_value);
            rank = itemView.findViewById(R.id.rank_value);
            categoryValue = itemView.findViewById(R.id.category_value);

        }
    }
    //Inner interface
    public interface OnItemClickListener{
        //Send the view to differentiate between which button view is being pressed
        void onItemClick(int position, View view, LeaderboardItem item);
    }


}


