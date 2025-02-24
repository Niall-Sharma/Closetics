package closetics.Clothes;

import jakarta.persistence.*;

@Entity(name = "clothes_table")
public class Clothing {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int clothesId;

    @Enumerated(EnumType.STRING)
    private TYPES type;

    @Enumerated(EnumType.STRING)
    private SPECIALTYPES specialtype;

    boolean isFavorite;
    String size;
    String lastWorn;
    int timesWorn;
    String color;
    String dateBought;
    String brand;
    String imagePath1;
    String imagePath2;
    String imagePath3;
    String itemName;
    String material;

    public Clothing(int itemId, SPECIALTYPES specialtype, TYPES type,  boolean isFavorite, String size, String lastWorn, int timesWorn, String color, String dateBought, String brand, String imagePath1, String imagePath2, String imagePath3, String itemName, String material) {
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
