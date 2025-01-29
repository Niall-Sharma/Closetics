package Classes.Enums;

import Classes.ClothingType;

public class Formalwear extends ClothingType {
    private final Types type;

    public Formalwear(Formalwear.Types type) {
        super(ClothingType.Types.FORMALWEAR);
        this.type = type;
    }

    @Override
    public Types getType() {
        return type;
    }

    public enum Types {
        SUITS,
        TUXEDOS,
        EVENING_GOWNS,
        DRESS_SHIRTS,
        TIES,
        WAISTCOATS,
        FORMAL_DRESSES,
        OTHER
    }
}
