package Classes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClothingItem {
    private String brand;
    private String color;
    private Date date_bought;
    private Float price;
    private ClothingType clothing_category;
    private ClothingType clothing_type;
    private Boolean favorite;
    private String image_path1;
    private String image_path2;
    private String image_path3;

    // Constructor
    public ClothingItem(String brand, String color, Date date_bought, Float price, ClothingType clothing_category, ClothingType clothing_type,
                        Boolean favorite, String image_path1, String image_path2, String image_path3) {
        this.brand = brand;
        this.color = color;
        this.date_bought = date_bought;
        this.price = price;
        this.clothing_category = clothing_category;
        this.clothing_type = clothing_type;
        this.favorite = favorite;
        this.image_path1 = image_path1;
        this.image_path2 = image_path2;
        this.image_path3 = image_path3;
    }

    // JSON conversion method
    public String toJson() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Format the date
        return "{" +
                "\"brand\": \"" + escapeJson(brand) + "\"," +
                "\"color\": \"" + escapeJson(color) + "\"," +
                "\"date_bought\": \"" + (date_bought != null ? dateFormat.format(date_bought) : null) + "\"," +
                "\"price\": " + (price != null ? price : "null") + "," +
                "\"clothing_category\": \"" + escapeJson(clothing_category.getCategory().toString()) + "\"," +
                "\"clothing_type\": \"" + escapeJson(clothing_type.getType().toString()) + "\"," +
                "\"favorite\": " + favorite + "," +
                "\"image_path1\": \"" + escapeJson(image_path1) + "\"," +
                "\"image_path2\": \"" + escapeJson(image_path2) + "\"," +
                "\"image_path3\": \"" + escapeJson(image_path3) + "\"" +
                "}";
    }

    // Escaping special characters in strings for JSON
    private String escapeJson(String value) {
        if (value == null) {
            return null;
        }
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}