import ItemClasses.ClothingType;
import ItemClasses.Enums.Accessories;
import ItemClasses.Enums.Activewear;
import ItemClasses.Enums.Tops;

import java.util.Date;
import java.util.UUID;
import static java.util.UUID.randomUUID;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Tops tshirt = new Tops(Tops.Types.T_SHIRTS);

        ClothingItem item = new ClothingItem(
                "Nike", "Red", new Date(), 59.99f, tshirt, tshirt,
                true, "path1.jpg", "path2.jpg", "path3.jpg"
        );
        System.out.println(item.toJson());

        User user1 = new User(randomUUID(), "braydenh804", "strongpassword", "Brayden",
                "Hayworth", new Date());
        System.out.println(user1.toJson());
    }
}