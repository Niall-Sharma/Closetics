package closetics.Statistics.LeaderboardSocket;

import closetics.Clothes.ClothingRepository;
import closetics.Outfits.OutfitRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.*;

@ServerEndpoint("/leaderboard/{username}/{type}")
@Component
public class LeaderboardSocketHandler {

    private static ClothingRepository clothingRepository;
    private static OutfitRepository outfitRepository;

    @Autowired
    public void setClothingRepository(ClothingRepository repo) {
        clothingRepository = repo;
    }

    @Autowired
    public void setOutfitRepository(OutfitRepository outfitRepo) {
        outfitRepository = outfitRepo;
    }

    private static final Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static final Map<String, Session> usernameSessionMap = new Hashtable<>();
    private static final Map<Session, Integer> sessionTypeMap = new Hashtable<>();
    private final Logger logger = LoggerFactory.getLogger(LeaderboardSocketHandler.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @OnOpen
    public void onOpen(Session session,
                       @PathParam("username") String username,
                       @PathParam("type") int type) throws IOException {
        logger.info("[onOpen] " + username + " with type " + type);

        if (usernameSessionMap.containsKey(username)) {
            session.getBasicRemote().sendText("Username already exists");
            session.close();
        } else {
            sessionUsernameMap.put(session, username);
            usernameSessionMap.put(username, session);
            sessionTypeMap.put(session, type);

            sendLeaderboardToSession(session, type);
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        logger.info("[onMessage] " + message);
        sendLeaderboardToAll(); // trigger leaderboard update on message
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        String username = sessionUsernameMap.get(session);
        logger.info("[onClose] " + username);

        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);
        sessionTypeMap.remove(session);
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

    private List<?> getLeaderboardByType(int type) {
        try {
            switch (type) {
                case 1:
                    return clothingRepository.findTop10MostValuable(PageRequest.of(0, 10));

                case 2:
                    return clothingRepository.findTop10UsersByClothingCount(PageRequest.of(0, 10));

                case 3:
                    return outfitRepository.findTop10MostExpensiveOutfits();
                default:
                    return Collections.emptyList();
            }
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private void sendLeaderboardToSession(Session session, int type) {
        try {
            List<?> leaderboard = getLeaderboardByType(type);
            String leaderboardJson = objectMapper.writeValueAsString(leaderboard);
            if (session.isOpen()) {
                session.getBasicRemote().sendText(leaderboardJson);
            }
        } catch (IOException e) {
            logger.error("Error sending leaderboard to session", e);
        }
    }

    private void sendLeaderboardToAll() {
        for (Session session : sessionUsernameMap.keySet()) {
            int type = sessionTypeMap.getOrDefault(session, 1); // default to 1
            sendLeaderboardToSession(session, type);
        }
    }
}
