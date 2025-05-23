package coms309.Classes.Enums;

import coms309.Classes.ClothingType;

public class Footwear extends ClothingType {
    private final Types type;

    public Footwear(Footwear.Types type) {
        super(ClothingType.Types.FOOTWEAR);
        this.type = type;
    }

    @Override
    public Types getType() {
        return type;
    }

    public enum Types {
        SNEAKERS,
        BOOTS,
        SANDALS,
        FLIP_FLOPS,
        HEELS,
        FLATS,
        LOAFERS,
        OXFORDS,
        SLIPPERS,
        OTHER
    }
}
