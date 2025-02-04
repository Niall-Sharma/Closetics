package coms309.clothing;


/**
 * Provides the Definition/Structure for the people row
 *
 * @author Vivek Bengre
 */

public class Clothing {

    private String name;

    private String size;

    private String color;

    private String type;
    private String price;
    private boolean isFavorite;
    public Clothing(){

    }

    public Clothing(String name, String size, String color, String type, String price, boolean isFavorite){
        this.name = name;
        this.size = size;
        this.color = color;
        this.type = type;
        this.price = price;
        this.isFavorite = isFavorite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public String toString() {
        return name + " "
                + color + " "
                + size + " "
                + type;
    }
}
