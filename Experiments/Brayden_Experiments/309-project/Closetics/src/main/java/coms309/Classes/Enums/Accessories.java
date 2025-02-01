package coms309.Classes.Enums;

import coms309.Classes.ClothingType;

public class Accessories extends ClothingType {
    private final Types type; // Subcategory

    public Accessories(Accessories.Types type) {
        super(ClothingType.Types.ACCESSORIES);
        this.type = type;
    }

    @Override
    public Types getType() {
        return type;
    }

    public enum Types {
        SCARVES,
        HATS,
        BELTS,
        GLOVES,
        HANDBAGS,
        BACKPACKS,
        GLASSES,
        JEWELRY,
        WATCHES,
        OTHER
    }
}
