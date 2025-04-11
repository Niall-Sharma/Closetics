package com.example.closetics.dashboard;

public class LeaderboardItem {


    private String rank;
    private String username;
    private String categoryValue;
    private long userId;

    public LeaderboardItem(String rank, String username, String categoryValue, long userId){
        this.rank = rank;
        this.username = username;
        this.categoryValue = categoryValue;
        //this.userId = userId;
    }
    //Empty constructor
    public LeaderboardItem(String rank,String categoryValue){
        this.categoryValue = categoryValue;
        this.rank = rank;
    }


    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public String getCategoryValue() {
        return categoryValue;
    }
    public String getRank() {
        return rank;
    }


}
