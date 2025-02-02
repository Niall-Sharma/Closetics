package coms309;
import coms309.Classes.ClothingItem;
import coms309.Classes.Enums.*;
import coms309.Classes.User;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import static java.util.UUID.randomUUID;

public class Main {
    public static void main(String[] args) throws IOException {
        User user1 = new User(randomUUID(),"braydenh80141", "strongpassword", "Brayden", "Hayworth");
        user1.createUser();
        user1.updateUsername("braydenh804");
        user1.updatePassword("BetterPass");
        user1.updateFirstName("Brayden");
        user1.updateLastName("Hayworth");
        user1.deleteUser();

    }
}
