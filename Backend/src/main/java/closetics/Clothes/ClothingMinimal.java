package closetics.Clothes;

public class ClothingMinimal {
    private long clothingId;
    private long userId; // Accept userId directly instead of needing whole object
    private String brand;
    private String color;
    private String dateBought;
    private Boolean favorite;
    private String imagePath;
    private String itemName;
    private String material;
    private String price;
    private String size;
    private long specialType;
    private long clothingType;

    public long getClothingId() {
        return clothingId;
    }

    public String getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }

    public String getDateBought() {
        return dateBought;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getItemName() {
        return itemName;
    }

    public String getMaterial() {
        return material;
    }

    public String getPrice() {
        return price;
    }

    public String getSize() {
        return size;
    }

    public long getSpecialType() {
        return specialType;
    }

    public long getClothingType() {
        return clothingType;
    }

    public Long getUserId() {
        return userId;
    }
}
