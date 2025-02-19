package closetics.Clothes;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity(name = "clothes_table")
public class Clothing {


    @Id
    int id;

    @Enumerated(EnumType.STRING)
    private TYPES type;

    @Enumerated(EnumType.STRING)
    private SPECIALTYPES specialtype;
    boolean isFavorite;

    public Clothing(int id, TYPES type, SPECIALTYPES specialtype, boolean isFavorite) {
        this.id = id;
        this.type = type;
        this.specialtype = specialtype;
        this.isFavorite = isFavorite;
    }

    public enum TYPES{
        SHOES,
        TOPS,
        BOTTOMS,
        ACCESSORIES
    }
    public enum SPECIALTYPES{
        TENNISSHOES,
        RUNNINGSHOES
    }

}
