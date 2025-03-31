package closetics.Clothes;

import closetics.Users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import closetics.Clothes.Statistics.ClothingStats;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity(name = "clothing_table")
public class Clothing {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long clothesId;

    private boolean favorite;
    private String size;
    private String color;
    private String dateBought;
    private String brand;
    private String imagePath;
    private String itemName;
    private String material;
    private String price;
    private long type;
    private long specialType;
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"email", "password", "userTier",
            "sQA1", "sQID1", "sQA2", "sQID2", "sQA3", "sQID3"})
    private User user;
  
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "stat_id")
    @JsonIgnore
    private ClothingStats clothingStats;

    public Clothing(long itemId, boolean favorite, String size, String color, String dateBought, String brand, String imagePath, String itemName, String material, String price, long specialType, long type, User user ) {
        this.clothesId = itemId;
        this.favorite = favorite;
        this.size = size;
        this.color = color;
        this.dateBought = dateBought;
        this.brand = brand;
        this.imagePath = imagePath;
        this.itemName = itemName;
        this.material = material;
        this.specialType = specialType;
        this.type = type;
        this.clothingStats = new ClothingStats();
        this.user = user;
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

    public ClothingStats getStat(){
      return clothingStats;
    }
    public void setStat(ClothingStats clothingStats){
      this.clothingStats = clothingStats;
    }

    public long getSpecialType(){
      return specialType;
    }
    public void setSpecialType(long specialType){
      this.specialType = specialType;
    }

    public User getUser(){
      return user;
    }
    public void setUser(User user){
      this.user = user;
    }

    public long getClothesId() {
        return clothesId;
    }
    public void setClothesId(int clothesId) {
        this.clothesId = clothesId;
    }

    public boolean getFavorite() {
        return favorite;
    }
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
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

    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }
}
