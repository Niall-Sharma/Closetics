package closetics.Clothes;

import closetics.Clothes.ClothingTypes.ClothingType;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import closetics.Clothes.ClothingTypes.SpecialType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.Date;

@Entity(name = "clothes_table")
public class Clothing {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long clothesId;

    private boolean isFavorite;
    private String size;
    private String color;
    private String dateBought;
    private String brand;
    private String imagePath1;
    private String imagePath2;
    private String imagePath3;
    private String itemName;
    private String material;
    private String price;
    private long userId;
    private long type;
    private long specialType;
  

    public Clothing(long itemId,  boolean isFavorite, String size, String color, String dateBought, String brand, String imagePath1, String imagePath2, String imagePath3, String itemName, String material, String price, long specialType, long type, long userId ) {
        this.clothesId = itemId;
        this.isFavorite = isFavorite;
        this.size = size;
        this.color = color;
        this.dateBought = dateBought;
        this.brand = brand;
        this.imagePath1 = imagePath1;
        this.imagePath2 = imagePath2;
        this.imagePath3 = imagePath3;
        this.itemName = itemName;
        this.material = material;
        this.specialType = specialType;
        this.type = type;
        this.userId = userId;
        this.price = price;
    }
    public Clothing(){

    }
    
    public long getClothingType(){
      return type;
    }

    public void setClothingType(long clothingType){
      type = clothingType;
    }
    
    public long getSpecialType(){
      return specialType;
    }

    public void setSpecialType(long specialType){
      this.specialType = specialType; 
    }

    public long getUser(){
      return userId;
    }
    
    public void setUser(long user){
      this.userId = user;
    }

    public long getClothesId() {
        return clothesId;
    }
    public void setClothesId(int clothesId) {
        this.clothesId = clothesId;
    }
    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

     public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDateBought() {
        return dateBought;
    }

    public void setDateBought(String dateBought) {
        this.dateBought = dateBought;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImagePath1() {
        return imagePath1;
    }

    public void setImagePath1(String imagePath1) {
        this.imagePath1 = imagePath1;
    }

    public String getImagePath2() {
        return imagePath2;
    }

    public void setImagePath2(String imagePath2) {
        this.imagePath2 = imagePath2;
    }

    public String getImagePath3() {
        return imagePath3;
    }

    public void setImagePath3(String imagePath3) {
        this.imagePath3 = imagePath3;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
