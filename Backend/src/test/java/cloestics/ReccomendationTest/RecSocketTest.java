//package cloestics.ReccomendationTest;
//
//import closetics.Clothes.Clothing;
//import closetics.Clothes.ClothingRepository;
//import closetics.MainApplication;
//import closetics.Outfits.Outfit;
//import closetics.Outfits.OutfitRepository;
//import closetics.Users.User;
//import closetics.Users.UserProfile.UserProfile;
//import closetics.Users.UserProfile.UserProfileRepository;
//import closetics.Users.UserRepository;
//import jakarta.websocket.ClientEndpoint;
//import jakarta.websocket.ContainerProvider;
//import jakarta.websocket.DeploymentException;
//import jakarta.websocket.OnMessage;
//import jakarta.websocket.Session;
//import jakarta.websocket.WebSocketContainer;
//import org.json.JSONArray;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.io.IOException;
//import java.net.URI;
//import java.util.List;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(
//        classes = MainApplication.class,
//        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
//)
//public class RecSocketTest {
//
//    @LocalServerPort
//    private int port;
//
//    private BlockingQueue<String> messages;
//
//    private User user;
//    private UserProfile userProfile;
//
//    private User user2;
//    private UserProfile userProfile2;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private ClothingRepository clothingRepository;
//
//    @Autowired
//    private OutfitRepository outfitRepository;
//
//    @Autowired
//    private UserProfileRepository userProfileRepository;
//
//    @BeforeEach
//    public void setup() {
//        messages = new LinkedBlockingQueue<>();
//        user = new User();
//        user.setUserId(1);
//
//        userRepository.save(user);
//
//        Clothing clothing = new Clothing();
//        clothing.setUser(user);
//        clothingRepository.save(clothing);
//
//        Outfit outfit = new Outfit();
//        outfit.setOutfitItems(List.of(clothing));
//        outfit.setUser(user);
//
//        outfitRepository.save(outfit);
//
//        userProfile = new UserProfile();
//        userProfile.addOutfit(outfit);
//        user.SetUserProfile(userProfile);
//        userProfileRepository.save(userProfile);
//        userRepository.save(user);
//
//        user2 = new User();
//        user2.setUserId(2);
//
//        userRepository.save(user2);
//
//        Clothing clothing2 = new Clothing();
//        clothing2.setUser(user2);
//        clothingRepository.save(clothing2);
//
//        Outfit outfit2 = new Outfit();
//        outfit2.setOutfitItems(List.of(clothing2));
//        outfit2.setUser(user2);
//
//        outfitRepository.save(outfit2);
//
//        userProfile2 = new UserProfile();
//        userProfile.addOutfit(outfit2);
//        user.SetUserProfile(userProfile2);
//        userProfileRepository.save(userProfile2);
//        userRepository.save(user2);
//    }
//
//    @ClientEndpoint
//    public class TestClientEndpoint {
//        @OnMessage
//        public void onMessage(String message) {
//            messages.add(message);
//        }
//    }
//
//    private Session connectWebSocket(long uid) throws DeploymentException, IOException {
//        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
//        URI uri = URI.create("ws://localhost:" + port + "/recommendation/" + uid);
//        return container.connectToServer(new TestClientEndpoint(), uri);
//    }
//
//    @Test
//    public void ReceiveRecommendations() throws Exception {
//        Session session = connectWebSocket(1L);
//        session.getBasicRemote().sendText(" ");
//
//        String json = messages.poll(5, TimeUnit.SECONDS);
//        System.out.println("Received message: " + json);
//        assertNotNull(json, "Expected a JSON array from server");
//
//        //JSONArray arr = new JSONArray(json);
//        //assertTrue(arr.length() > 0, "Expected at least one recommendation");
//        //assertTrue(arr.length() <= 5, "Expected no more than 5 recommendations");
//        session.close();
//    }
//
//    @Test
//    public void NoMessageOnConnect() throws Exception {
//        Session session = connectWebSocket(1L);
//        String maybe = messages.poll(1, TimeUnit.SECONDS);
//        assertNull(maybe, "Did not expect any message on open");
//        session.close();
//    }
//}
