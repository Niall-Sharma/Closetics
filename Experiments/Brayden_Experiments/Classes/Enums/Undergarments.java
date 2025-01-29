package Classes.Enums;

import Classes.ClothingType;

public class Undergarments extends ClothingType {
    private final Types type;

    public Undergarments(Undergarments.Types type) {
        super(ClothingType.Types.UNDERGARMENTS);
        this.type = type;
    }

    @Override
    public Types getType() {
        return type;
    }

    public enum Types {
        BRAS,
        UNDERWEAR,
        BOXERS,
        BRIEFS,
        PANTIES,
        OTHER
    }
}
