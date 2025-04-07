package com.example.closetics.follow;

import android.content.Context;

public class FollowUsersListItem {
    private long id;
    private String username;
    private boolean isFollowing;
    private Context context;

    public FollowUsersListItem(Context context, long id, String username, boolean isFollowing) {
        this.context = context;
        this.id = id;
        this.username = username;
        this.isFollowing = isFollowing;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
