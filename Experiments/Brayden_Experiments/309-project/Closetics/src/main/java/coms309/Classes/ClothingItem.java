package coms309.Classes;
import java.util.Date;
import java.util.UUID;
import lombok.*;
import okhttp3.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@AllArgsConstructor
@RequiredArgsConstructor
public class ClothingItem extends ConvertsToJSON{

    @Getter
    private Integer itemId = 0;

    @Getter
    @NonNull
    private UUID userId;

    @Getter
    private String brand;

    @Getter
    private String color;

    @Getter
    private Date dateBought;

    @Getter
    private Double price;

    @Getter
    private String itemName;

    @Getter
    @NonNull
    private ClothingType clothingCategory;

    @Getter
    @NonNull
    private ClothingType clothingType;

    @Getter
    @NonNull
    private Boolean isFavorite = false;

    @Getter
    private String imagePath1;

    @Getter
    private String imagePath2;

    @Getter
    private String imagePath3;

    public void updateBrand(String newBrand) {
        String oldBrand = this.brand;
        this.brand = newBrand;
        if (!updateClothingItem()) {this.brand = oldBrand;}
    }

    public void updateColor(String newColor) {
        String oldColor = this.color;
        this.color = newColor;
        if (!updateClothingItem()) {this.color = oldColor;}
    }

    public void updateDateBought(Date newDateBought) {
        Date oldDateBought = this.dateBought;
        this.dateBought = newDateBought;
        if (!updateClothingItem()) {this.dateBought = oldDateBought;}
    }

    public void updatePrice(Double newPrice) {
        Double oldPrice = this.price;
        this.price = newPrice;
        if (!updateClothingItem()) {this.price = oldPrice;}
    }

    public void updateItemName(String newItemName) {
        String oldItemName = this.itemName;
        this.itemName = newItemName;
        if (!updateClothingItem()) {this.itemName = oldItemName;}
    }

    public void updateClothingCategory(ClothingType newCategory) {
        ClothingType oldCategory = this.clothingCategory;
        this.clothingCategory = newCategory;
        if (!updateClothingItem()) {this.clothingCategory = oldCategory;}
    }

    public void updateClothingType(ClothingType newType) {
        ClothingType oldType = this.clothingType;
        this.clothingType = newType;
        if (!updateClothingItem()) {this.clothingType = oldType;}
    }

    public void updateIsFavorite(Boolean newIsFavorite) {
        Boolean oldIsFavorite = this.isFavorite;
        this.isFavorite = newIsFavorite;
        if (!updateClothingItem()) {this.isFavorite = oldIsFavorite;}
    }

    public void updateImagePath1(String newPath) {
        String oldPath = this.imagePath1;
        this.imagePath1 = newPath;
        if (!updateClothingItem()) {this.imagePath1 = oldPath;}
    }

    public void updateImagePath2(String newPath) {
        String oldPath = this.imagePath2;
        this.imagePath2 = newPath;
        if (!updateClothingItem()) {this.imagePath2 = oldPath;}
    }

    public void updateImagePath3(String newPath) {
        String oldPath = this.imagePath3;
        this.imagePath3 = newPath;
        if (!updateClothingItem()) {this.imagePath3 = oldPath;}
    }

    @Override
    public String toJSON() {
                return "{" +
                "\"itemId\": \"" + escapeJSON(itemId.toString()) + "\"," +
                "\"userId\": \"" + escapeJSON(userId.toString()) + "\"," +
                "\"brand\": \"" + escapeJSON(brand) + "\"," +
                "\"color\": \"" + escapeJSON(color) + "\"," +
                "\"dateBought\": \"" + (dateBought != null ? dateFormat.format(dateBought) : null) + "\"," +
                "\"price\": \"" + (price != null ? price : "null") + "\"," +
                "\"itemName\": \"" + escapeJSON(itemName) + "\"," +
                "\"clothingCategory\": \"" + escapeJSON(clothingCategory.getCategory().toString()) + "\"," +
                "\"clothingType\": \"" + escapeJSON(clothingType.getType().toString()) + "\"," +
                "\"isFavorite\": \"" + isFavorite + "\"," +
                "\"imagePath1\": \"" + escapeJSON(imagePath1) + "\"," +
                "\"imagePath2\": \"" + escapeJSON(imagePath2) + "\"," +
                "\"imagePath3\": \"" + escapeJSON(imagePath3) + "\"" +
                "}";
    }

    public void createClothingItem() {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(toJSON(), mediaType);
            Request request = new Request.Builder().url("http://localhost:8080/addClothingItem").post(body)
                    .addHeader("Content-Type", "application/json").build();
            Response response = client.newCall(request).execute();

            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                itemId = jsonNode.get("itemId").asInt();
                System.out.println("Response: " + jsonResponse);
            } else {
                System.out.println("Error: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteClothingItem() {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            String url = "http://localhost:8080/deleteClothingItem/" + this.itemId;
            Request request = new Request.Builder().url(url).method("DELETE", body).build();
            Response response = client.newCall(request).execute();

            if (response.isSuccessful() && response.body() != null) {
                System.out.println("Response: " + response.body().string());
            } else {
                System.out.println("Error: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean updateClothingItem() {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(toJSON(), mediaType);
            Request request = new Request.Builder().url("http://localhost:8080/updateClothingItem").put(body)
                    .addHeader("Content-Type", "application/json").build();
            Response response = client.newCall(request).execute();

            if (response.isSuccessful() && response.body() != null) {
                System.out.println("Response: " + response.body().string());
                return true;
            } else {
                System.out.println("Error: " + response.code());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}