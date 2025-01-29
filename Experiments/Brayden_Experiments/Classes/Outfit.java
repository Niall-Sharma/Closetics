package Classes;

import java.util.Date;

public class Outfit extends ConvertsToJSON{
    private Integer outfit_id;
    private String outfit_name;
    private Date creation_date;
    private Boolean is_favorite;
    private ClothingItem[] outfit_items;
    private Integer[] outfit_item_ids;

    public Outfit(Integer outfit_id, String outfit_name, Date creation_date, Boolean is_favorite,
                  ClothingItem[] outfit_items, Integer[] outfit_item_ids) {
        this.outfit_id = outfit_id;
        this.outfit_name = outfit_name;
        this.creation_date = creation_date;
        this.is_favorite = is_favorite;
        this.outfit_items = outfit_items;
        this.outfit_item_ids = outfit_item_ids;
    }

    public Integer getOutfit_id() {
        return outfit_id;
    }

    public String getOutfit_name() {
        return outfit_name;
    }

    public void setOutfit_name(String outfit_name) {
        this.outfit_name = outfit_name;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public Boolean getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(Boolean is_favorite) {
        this.is_favorite = is_favorite;
    }

    public ClothingItem[] getOutfit_items() {
        return outfit_items;
    }

    @Override
    public String toJSON() {
        return "{" +
                "\"outfit_id\": \"" + escapeJSON(outfit_id.toString()) + "\"," +
                "\"outfit_name\": \"" + escapeJSON(outfit_name) + "\"," +
                "\"creation_date\": \"" + (creation_date != null ? dateFormat.format(creation_date) : null) + "\"," +
                "\"user_created_date\": \"" + escapeJSON(is_favorite.toString()) + "\"" +
                "}";
    }

    public String[] eachItemToJSON() {
        int i = 0;
        String[] JSONArray = new String[getOutfit_items().length];

        for (ClothingItem item : getOutfit_items()) {
            JSONArray[i] = "{" +
                    "\"item_id\": \"" + escapeJSON(item.getItem_id().toString()) + "\"," +
                    "\"user_id\": \"" + escapeJSON(item.getUser_id().toString()) + "\"," +
                    "\"brand\": \"" + escapeJSON(item.getBrand()) + "\"," +
                    "\"color\": \"" + escapeJSON(item.getColor()) + "\"," +
                    "\"date_bought\": \"" + (item.getDate_bought() != null ? dateFormat.format(item.getDate_bought()) : null) + "\"," +
                    "\"price\": " + (item.getPrice() != null ? item.getPrice() : "null") + "," +
                    "\"clothing_category\": \"" + escapeJSON(item.getClothing_category().getCategory().toString()) + "\"," +
                    "\"clothing_type\": \"" + escapeJSON(item.getClothing_type().getType().toString()) + "\"," +
                    "\"is_favorite\": " + item.getIs_favorite() + "," +
                    "\"image_path1\": \"" + escapeJSON(item.getImage_path1()) + "\"," +
                    "\"image_path2\": \"" + escapeJSON(item.getImage_path2()) + "\"," +
                    "\"image_path3\": \"" + escapeJSON(item.getImage_path3()) + "\"" +
                    "}";
        }
        return JSONArray;
    }
}
