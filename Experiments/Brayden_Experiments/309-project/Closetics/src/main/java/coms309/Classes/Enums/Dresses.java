package coms309.Classes.Enums;

import coms309.Classes.ClothingType;

public class Dresses extends ClothingType {
    private final Types type;

    public Dresses(Dresses.Types type) {
        super(ClothingType.Types.DRESSES);
        this.type = type;
    }

    @Override
    public Types getType() {
        return type;
    }

    public enum Types {
        MAXI_DRESSES,
        MINI_DRESSES,
        MIDI_DRESSES,
        A_LINE_DRESSES,
        BODYCON_DRESSES,
        WRAP_DRESSES,
        COCKTAIL_DRESSES,
        GOWNS,
        SUN_DRESSES,
        OTHER
    }
}
