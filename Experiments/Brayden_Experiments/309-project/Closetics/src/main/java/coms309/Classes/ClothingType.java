package coms309.Classes;

public class ClothingType {
    private final Types category;

    public ClothingType(Types category) {
        this.category = category;
    }

    public Types getCategory() {
        return category;
    }

    public Object getType() {
        return null;
    }


    public enum Types {
        ACCESSORIES,
        ACTIVEWEAR,
        BOTTOMS,
        DRESSES,
        FOOTWEAR,
        FORMALWEAR,
        OUTERWEAR,
        SEASONAL,
        SLEEPWEAR,
        TOPS,
        UNDERGARMENTS,
        WORKWEAR
    }
}
