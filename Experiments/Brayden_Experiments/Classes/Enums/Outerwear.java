package Classes.Enums;

import Classes.ClothingType;

public class Outerwear extends ClothingType {
    private final Types type;

    public Outerwear(Outerwear.Types type) {
        super(ClothingType.Types.OUTERWEAR);
        this.type = type;
    }

    @Override
    public Types getType() {
        return type;
    }

    public enum Types {
        JACKETS,
        COATS,
        BLAZERS,
        CARDIGANS,
        VESTS,
        PONCHOS,
        WINDBREAKERS,
        PARKAS,
        OTHER
    }
}