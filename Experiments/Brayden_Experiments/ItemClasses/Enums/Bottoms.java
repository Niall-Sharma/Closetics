package ItemClasses.Enums;

import ItemClasses.ClothingType;

public class Bottoms extends ClothingType {
    private final Types type;

    public Bottoms(Bottoms.Types type) {
        super(ClothingType.Types.BOTTOMS);
        this.type = type;
    }

    @Override
    public Types getType() {
        return type;
    }

    public enum Types {
        JEANS,
        TROUSERS,
        SHORTS,
        SKIRTS,
        LEGGINGS,
        SWEATPANTS,
        CAPRIS,
        OTHER
    }
}
