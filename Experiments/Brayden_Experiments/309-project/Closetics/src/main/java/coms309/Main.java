package coms309;
import coms309.Classes.ClothingItem;
import coms309.Classes.Enums.*;
import coms309.Classes.User;

import java.io.IOException;
import java.util.Date;

import static java.util.UUID.randomUUID;

public class Main {
    public static void main(String[] args) throws IOException {
        User user1 = new User(randomUUID(),"braydenh1804", "strongpassword", "Brayden", "Hayworth");
        user1.createUser();
        //user1.updateUsername("braydenh804");
        //user1.updatePassword("BetterPass");
        //user1.updateFirstName("Brayden");
        //user1.updateLastName("Hayworth");
        //user1.deleteUser();
        Tops tshirt = new Tops(Tops.Types.T_SHIRTS);
        Tops hoodie = new Tops(Tops.Types.HOODIES);
        ClothingItem clothes1 = new ClothingItem (null, user1.getUser_id(), "Nike", "Blue", new Date(), 9.99, "fav hoddie", tshirt, tshirt, true, "./image1", "./image2", "./image3");
        clothes1.createClothingItem();
        clothes1.updatePrice(19.99);
        //clothes1.deleteClothingItem();
    }
}
