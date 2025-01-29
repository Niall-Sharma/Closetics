package ItemClasses.Enums;

import ItemClasses.ClothingType;

public class Sleepwear extends ClothingType {
    private final Types type;

    public Sleepwear(Sleepwear.Types type) {
        super(ClothingType.Types.SLEEPWEAR);
        this.type = type;
    }

    @Override
    public Types getType() {
        return type;
    }

    public enum Types {
        PAJAMAS,
        NIGHTGOWNS,
        SLEEP_SHIRTS,
        ROBES,
        LOUNGEWEAR,
        OTHER
    }
}
