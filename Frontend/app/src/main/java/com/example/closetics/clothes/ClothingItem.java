package com.example.closetics.clothes;


import java.io.Serializable;

public class ClothingItem implements Serializable {
    public static String[] createClothesQuestions= { "Would you like to favorite this item?","Size of this item?","What color is it?","What date was it bought?",
            "What is its price?", "What would you like to call this piece of clothing?",
            "What is the brand?", "What material is it?"
    };
    private String favorite;
    private String size;
    private String color;
    private String dateBought;
    private String brand;
    //private String imagePath1;
    //private String imagePath2;
    //private String imagePath3;
    private String itemName;
    private String material;
    private String price;
    //private long userId;
    //private long type;
    //private long specialType;


    public ClothingItem (String favorite, String size, String color, String dateBought, String brand,
                         String itemName, String material, String price){
        this.favorite = favorite;
        this.size = size;
        this.color = color;
        this.dateBought = dateBought;
        this.brand = brand;
        this.itemName = itemName;
        this.material = material;
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public String getMaterial() {
        return material;
    }

    public String getItemName() {
        return itemName;
    }

    public String getFavorite() {
        return favorite;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public String getDateBought() {
        return dateBought;
    }

    public String getBrand() {
        return brand;
    }
}
