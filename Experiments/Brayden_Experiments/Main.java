import Classes.ClothingItem;
import Classes.Enums.*;
import Classes.User;

import java.util.Date;
import static java.util.UUID.randomUUID;

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