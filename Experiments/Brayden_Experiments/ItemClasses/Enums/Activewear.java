package ItemClasses.Enums;
import ItemClasses.ClothingType;

public class Activewear extends ClothingType {
    private final Types type;

    public Activewear(Activewear.Types type) {
        super(ClothingType.Types.ACTIVEWEAR);
        this.type = type;
    }

    @Override
    public Types getType() {
        return type;
    }

    public enum Types {
        SPORTS_BRAS,
        YOGA_PANTS,
        TRACKSUITS,
        RUNNING_SHORTS,
        GYM_T_SHIRTS,
        COMPRESSION_WEAR,
        OTHER
    }
}
