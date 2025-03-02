package closetics.Clothes;

import closetics.Users.User;
import jakarta.persistence.*;

import java.util.Date;

@Entity(name = "clothes_table")
public class Clothing {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long clothesId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private TYPES type;

    @Enumerated(EnumType.STRING)
    private SPECIALTYPES specialtype;

    private boolean isFavorite;
    private String size;
    private String lastWorn;
    private int timesWorn;
    private String color;
    private Date dateBought;
    private String brand;
    private String imagePath1;
    private String imagePath2;
    private String imagePath3;
    private String itemName;
    private String material;

    public Clothing(int itemId, SPECIALTYPES specialtype, TYPES type,  boolean isFavorite, String size, String lastWorn, int timesWorn, String color, Date dateBought, String brand, String imagePath1, String imagePath2, String imagePath3, String itemName, String material) {
        this.clothesId = itemId;
        this.type = type;
        this.specialtype = specialtype;
        this.isFavorite = isFavorite;
        this.size = size;
        this.lastWorn = lastWorn;
        this.timesWorn = timesWorn;
        this.color = color;
        this.dateBought = dateBought;
        this.brand = brand;
        this.imagePath1 = imagePath1;
        this.imagePath2 = imagePath2;
        this.imagePath3 = imagePath3;
        this.itemName = itemName;
        this.material = material;
    }
    public Clothing(){

    }


}
