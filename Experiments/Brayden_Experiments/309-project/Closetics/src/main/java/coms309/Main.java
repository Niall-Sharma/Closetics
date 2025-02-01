package coms309;
import coms309.Classes.ClothingItem;
import coms309.Classes.Enums.*;
import coms309.Classes.User;

import java.util.Date;
import java.util.UUID;

import static java.util.UUID.randomUUID;

public class Main {
    public static void main(String[] args) {
        User user1 = new User(randomUUID(), "braydenh804", "strongpassword", "Brayden",
                "Hayworth", new Date());
        Tops tshirt = new Tops(Tops.Types.T_SHIRTS);

        ClothingItem item = new ClothingItem( 1, user1.getUser_id(),
                "Nike", "Red", new Date(), 59.99f, tshirt, tshirt,
                true, "path1.jpg", "path2.jpg", "path3.jpg"
        );
        System.out.println(item.toJSON());
        System.out.println(user1.toJSON());
    }
}
