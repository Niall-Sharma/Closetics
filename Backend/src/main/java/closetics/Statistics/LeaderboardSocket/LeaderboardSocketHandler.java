package closetics.Statistics.LeaderboardSocket;

import closetics.Clothes.Clothing;
import closetics.Clothes.ClothingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@ServerEndpoint("/leaderboard/  {username}")
@Component
public class LeaderboardSocketHandler {

    private static ClothingRepository clothingRepository;

    // Static injection workaround
    @Autowired
    public void setClothingRepository(ClothingRepository repo) {
        clothingRepository = repo;
    }

    private static final Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static final Map<String, Session> usernameSessionMap = new Hashtable<>();
    private final Logger logger = LoggerFactory.getLogger(LeaderboardSocketHandler.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        logger.info("[onOpen] " + username);

        if (usernameSessionMap.containsKey(username)) {
            session.getBasicRemote().sendText("Username already exists");
            session.close();
        } else {
            sessionUsernameMap.put(session, username);
            usernameSessionMap.put(username, session);

            // Send current leaderboard
            sendLeaderboardToAll();
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        logger.info("[onMessage] " + message);
        sendLeaderboardToAll(); // trigger leaderboard update on message (if needed)
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        String username = sessionUsernameMap.get(session);
        logger.info("[onClose] " + username);
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        String username = sessionUsernameMap.get(session);
        logger.error("[onError] " + username + ": " + throwable.getMessage());
    }

    private void sendMessageToUser(String username, String message) {
        try {
            Session session = usernameSessionMap.get(username);
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            logger.error("Failed to send message to " + username, e);
        }
    }

    private void sendLeaderboardToAll() {
        try {
            List<Clothing> leaderboard = clothingRepository.findAll().stream()
                    .sorted((c1, c2) -> {
                        try {
                            double p1 = Double.parseDouble(c1.getPrice());
                            double p2 = Double.parseDouble(c2.getPrice());
                            return Double.compare(p2, p1);
                        } catch (Exception e) {
                            return 0;
                        }
                    })
                    .limit(10)
                    .collect(Collectors.toList());

            String leaderboardJson = objectMapper.writeValueAsString(leaderboard);

            for (Session session : sessionUsernameMap.keySet()) {
                if (session.isOpen()) {
                    session.getBasicRemote().sendText(leaderboardJson);
                }
            }
        } catch (Exception e) {
            logger.error("Error sending leaderboard", e);
        }
    }
}
