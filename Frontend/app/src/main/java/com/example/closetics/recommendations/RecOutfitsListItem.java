package com.example.closetics.recommendations;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import java.util.List;

public class RecOutfitsListItem {
    private long id;
    private String name;
    private String username;
    private List<Integer> imageIds;
    private String stats;
    private String date;
    private boolean isLiked;
    private FragmentActivity activity;

    public RecOutfitsListItem(FragmentActivity activity, long id, String name, String username, List<Integer> imageIds, String stats, String date, boolean isLiked) {
        this.activity = activity;
        this.id = id;
        this.name = name;
        this.username = username;
        this.imageIds = imageIds;
        this.stats = stats;
        this.date = date;
        this.isLiked = isLiked;
    }

    public Context getActivity() {
        return activity;
    }

    public Context getContext() {
        return activity.getApplicationContext();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Integer> getImageIds() {
        return imageIds;
    }

    public void setImageIds(List<Integer> imageIds) {
        this.imageIds = imageIds;
    }

    public String getStats() {
        return stats;
    }

    public void setStats(String stats) {
        this.stats = stats;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
