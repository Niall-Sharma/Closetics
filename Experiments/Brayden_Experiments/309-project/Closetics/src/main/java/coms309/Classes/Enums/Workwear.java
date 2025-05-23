package coms309.Classes.Enums;

import coms309.Classes.ClothingType;

public class Workwear extends ClothingType {
    private final Types type;

    public Workwear(Workwear.Types type) {
        super(ClothingType.Types.WORKWEAR);
        this.type = type;
    }

    @Override
    public Types getType() {return type;}

    public enum Types {
        UNIFORMS,
        OVERALLS,
        SCRUBS,
        APRONS,
        SAFETY_GEAR,
        OTHER
    }
}
