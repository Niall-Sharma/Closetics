package closetics.Clothes;

import jakarta.persistence.*;

@Entity(name = "clothes_table")
public class Clothing {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Enumerated(EnumType.STRING)
    private TYPES type;

    @Enumerated(EnumType.STRING)
    private SPECIALTYPES specialtype;
    boolean isFavorite;
    String size;
    String lastWorn;
    int timesWorn;

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
