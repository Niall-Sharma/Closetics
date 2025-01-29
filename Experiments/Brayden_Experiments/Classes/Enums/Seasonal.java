package Classes.Enums;

import Classes.ClothingType;

public class Seasonal extends ClothingType {
    private final Types type;

    public Seasonal(Seasonal.Types type) {
        super(ClothingType.Types.SEASONAL);
        this.type = type;
    }

    @Override
    public Types getType() {
        return type;
    }

    public enum Types {
        SWIMWEAR,
        WINTERWEAR,
        RAINWEAR,
        OTHER
    }
}
