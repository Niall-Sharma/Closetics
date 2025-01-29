package Classes;

import java.util.Date;
import java.util.UUID;

public class ClothingItem extends ConvertsToJSON{
    private Integer item_id;
    private UUID user_id;
    private String brand;
    private String color;
    private Date date_bought;
    private Float price;
    private ClothingType clothing_category;
    private ClothingType clothing_type;
    private Boolean is_favorite;
    private String image_path1;
    private String image_path2;
    private String image_path3;

    // Constructor
    public ClothingItem(Integer item_id, UUID user_id, String brand, String color, Date date_bought, Float price, ClothingType clothing_category, ClothingType clothing_type,
                        Boolean is_favorite, String image_path1, String image_path2, String image_path3) {
        this.item_id = item_id;
        this.user_id = user_id;
        this.brand = brand;
        this.color = color;
        this.date_bought = date_bought;
        this.price = price;
        this.clothing_category = clothing_category;
        this.clothing_type = clothing_type;
        this.is_favorite = is_favorite;
        this.image_path1 = image_path1;
        this.image_path2 = image_path2;
        this.image_path3 = image_path3;
    }

    public Integer getItem_id() {
        return item_id;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Date getDate_bought() {
        return date_bought;
    }

    public void setDate_bought(Date date_bought) {
        this.date_bought = date_bought;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public ClothingType getClothing_category() {
        return clothing_category;
    }

    public void setClothing_category(ClothingType clothing_category) {
        this.clothing_category = clothing_category;
    }

    public ClothingType getClothing_type() {
        return clothing_type;
    }

    public void setClothing_type(ClothingType clothing_type) {
        this.clothing_type = clothing_type;
    }

    public Boolean getIs_favorite() {
        return is_favorite;
    }

    public void setFavorite(Boolean is_favorite) {
        this.is_favorite = is_favorite;
    }

    public String getImage_path1() {
        return image_path1;
    }

    public void setImage_path1(String image_path1) {
        this.image_path1 = image_path1;
    }

    public String getImage_path2() {
        return image_path2;
    }

    public void setImage_path2(String image_path2) {
        this.image_path2 = image_path2;
    }

    public String getImage_path3() {
        return image_path3;
    }

    public void setImage_path3(String image_path3) {
        this.image_path3 = image_path3;
    }

    @Override
    public String toJSON() {
                return "{" +
                "\"item_id\": \"" + escapeJSON(item_id.toString()) + "\"," +
                "\"user_id\": \"" + escapeJSON(user_id.toString()) + "\"," +
                "\"brand\": \"" + escapeJSON(brand) + "\"," +
                "\"color\": \"" + escapeJSON(color) + "\"," +
                "\"date_bought\": \"" + (date_bought != null ? dateFormat.format(date_bought) : null) + "\"," +
                "\"price\": " + (price != null ? price : "null") + "," +
                "\"clothing_category\": \"" + escapeJSON(clothing_category.getCategory().toString()) + "\"," +
                "\"clothing_type\": \"" + escapeJSON(clothing_type.getType().toString()) + "\"," +
                "\"is_favorite\": " + is_favorite + "," +
                "\"image_path1\": \"" + escapeJSON(image_path1) + "\"," +
                "\"image_path2\": \"" + escapeJSON(image_path2) + "\"," +
                "\"image_path3\": \"" + escapeJSON(image_path3) + "\"" +
                "}";
    }

}