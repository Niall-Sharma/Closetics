package closetics.Clothes;

import jakarta.persistence.*;

@Entity(name = "clothes_table")
public class Clothing {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int clothesId;

    @Enumerated(EnumType.STRING)
    private TYPES type;

    @Enumerated(EnumType.STRING)
    private SPECIALTYPES specialtype;

    boolean isFavorite;
    String size;
    String lastWorn;
    int timesWorn;
    String color;
    String dateBought;
    String brand;
    String imagePath1;
    String imagePath2;
    String imagePath3;
    String itemName;
    String material;

    public Clothing(int itemId, SPECIALTYPES specialtype, TYPES type,  boolean isFavorite, String size, String lastWorn, int timesWorn, String color, String dateBought, String brand, String imagePath1, String imagePath2, String imagePath3, String itemName, String material) {
        this.clothesId = itemId;
        this.type = type;
        this.specialtype = specialtype;
        this.isFavorite = isFavorite;
        this.size = size;
        this.lastWorn = lastWorn;
        this.timesWorn = timesWorn;
        this.color = color;
        this.dateBought = dateBought;
        this.brand = brand;
        this.imagePath1 = imagePath1;
        this.imagePath2 = imagePath2;
        this.imagePath3 = imagePath3;
        this.itemName = itemName;
        this.material = material;
    }
    public Clothing(){

    }

    public int getClothesId() {
        return clothesId;
    }

    public void setClothesId(int clothesId) {
        this.clothesId = clothesId;
    }

    public TYPES getType() {
        return type;
    }

    public void setType(TYPES type) {
        this.type = type;
    }

    public SPECIALTYPES getSpecialtype() {
        return specialtype;
    }

    public void setSpecialtype(SPECIALTYPES specialtype) {
        this.specialtype = specialtype;
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

    public String getLastWorn() {
        return lastWorn;
    }

    public void setLastWorn(String lastWorn) {
        this.lastWorn = lastWorn;
    }

    public int getTimesWorn() {
        return timesWorn;
    }

    public void setTimesWorn(int timesWorn) {
        this.timesWorn = timesWorn;
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
