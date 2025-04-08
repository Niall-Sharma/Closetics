package com.example.closetics.dashboard;

public class LeaderboardItem {

    String rank;
    String username;
    String categoryValue;
    long userId;

    public LeaderboardItem(String rank, String username, String categoryValue, long userId){
        this.rank = rank;
        this.username = username;
        this.categoryValue = categoryValue;
        this.userId = userId;
    }
    //Empty constructor
    public LeaderboardItem(String rank,String categoryValue, long userId){
        this.categoryValue = categoryValue;
        this.userId = userId;
        this.rank = rank;
    }


    public void setUsername(String username) {
        this.username = username;
    }
}
