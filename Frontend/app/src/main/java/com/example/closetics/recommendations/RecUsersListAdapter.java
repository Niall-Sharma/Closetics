package com.example.closetics.recommendations;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.closetics.R;

import java.util.List;

public class RecUsersListAdapter extends RecyclerView.Adapter<RecUsersListAdapter.ViewHolder>{
    private final List<RecUsersListItem> items;
    private final RecUsersListAdapter.OnItemClickListener listener;

    public RecUsersListAdapter(List<RecUsersListItem> items, RecUsersListAdapter.OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_rec_users, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecUsersListItem item = items.get(position);

        String username = item.getUsername();

        holder.usernameText.setText(username);

        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(RecUsersListItem item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void setNewItems(List<RecUsersListItem> newItems) {
        int oldSize = items.size();
        items.clear();
        items.addAll(newItems);
        notifyItemRangeChanged(0, Math.max(oldSize, newItems.size()));
    }

    public void clearItems() {
        int oldSize = items.size();
        items.clear();
        notifyItemRangeRemoved(0, oldSize);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView usernameText;

        public ViewHolder(final View itemView) {
            super(itemView);

            usernameText = itemView.findViewById(R.id.rec_user_list_item_username_text);
        }

        public void bind(final RecUsersListItem item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RecUsersListItem item);
    }
}
