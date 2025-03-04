package closetics.Clothes;

import closetics.Clothes.ClothingTypes.ClothingType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import closetics.Clothes.ClothingTypes.SpecialType;
import jakarta.persistence.*;

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
    
    @ManyToOne
    @JoinColumn(name = "type_Id")
    @JsonIgnore
    private ClothingType type;

    @ManyToOne
    @JoinColumn(name = "specialType_Id")
    @JsonIgnore
    private SpecialType specialType;
  
    private long userId;

    public Clothing(long userId, ClothingType type, SpecialType specialType, int itemId,  boolean isFavorite, String size, String color, String dateBought, String brand, String imagePath1, String imagePath2, String imagePath3, String itemName, String material) {
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
    }
    public Clothing(){

    }
    
    public ClothingType getClothingType(){
      return type;
    }

    public void setClothingType(ClothingType clothingType){
      type = clothingType;
    }
    
    public SpecialType getSpecialType(){
      return specialType;
    }

    public void setSpecialType(SpecialType specialType){
      this.specialType = specialType; 
    }

    public long getUser(){
      return userId;
    }
    
    public void setUser(long user){
      this.userId = user;
    }

    public int getClothesId() {
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
}
