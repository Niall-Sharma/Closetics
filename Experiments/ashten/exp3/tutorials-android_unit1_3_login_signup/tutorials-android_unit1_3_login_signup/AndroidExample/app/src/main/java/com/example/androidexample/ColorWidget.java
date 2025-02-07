package com.example.androidexample;



public class ColorWidget {
    private String colorName;
    private String tintName;
    private int easelImage;

    public ColorWidget(String colorName,String tintName,int easelImage){
        this.colorName = colorName;
        this.tintName = tintName;
        this.easelImage = easelImage;
    }

    public String getColorName(){
        return colorName;
    }
    public String getTintName(){
        return tintName;
    }
    public int getImage(){
        return easelImage;
    }

}
