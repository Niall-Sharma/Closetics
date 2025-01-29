package Classes.Enums;

import Classes.ClothingType;

public class Tops extends ClothingType {
    private final Types type;

    public Tops(Tops.Types type) {
        super(ClothingType.Types.TOPS);
        this.type = type;
    }

    @Override
    public Types getType() {
        return type;
    }

    public enum Types {
        T_SHIRTS,
        TANK_TOPS,
        BLOUSES,
        SHIRTS,
        POLOS,
        SWEATSHIRTS,
        HOODIES,
        CROP_TOPS,
        OTHER
    }
}
