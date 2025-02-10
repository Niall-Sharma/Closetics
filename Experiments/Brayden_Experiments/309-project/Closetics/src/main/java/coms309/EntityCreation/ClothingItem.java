package coms309.EntityCreation;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "clothing_item")
@AllArgsConstructor
@NoArgsConstructor
public class ClothingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, name = "item_id")
    private Integer itemId;

    @Column(nullable = false, updatable = false, name = "user_id")
    private UUID userId;

    private String brand;

    private String color;

    @Column(name = "date_bought")
    private Date dateBought;

    private Double price;

    @Column(name = "item_name")
    private String itemName;

    @Column(nullable = false, name = "clothing_category")
    private String clothingCategory;

    @Column(nullable = false, name = "clothing_type")
    private String clothingType;

    @Column(nullable = false, name = "is_favorite")
    private Boolean isFavorite;

    @Column(name = "image_path1")
    private String imagePath1;

    @Column(name = "image_path2")
    private String imagePath2;

    @Column(name = "image_path3")
    private String imagePath3;

}