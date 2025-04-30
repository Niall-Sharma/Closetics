package cloestics;

import closetics.MainApplication;
import jakarta.websocket.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MainApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class LeaderboardSocketSystemTest {

    @LocalServerPort
    private int port;

    private String websocketBaseUri;
    private static final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private static final Logger logger = LoggerFactory.getLogger(LeaderboardSocketSystemTest.class);
    private Session clientSession;

    @Before
    public void setUp() {
        websocketBaseUri = "ws://localhost:" + port + "/leaderboard/";
        messageQueue.clear(); // Clear queue before each test
    }

    @Test
    public void testConnectAndReceiveLeaderboardType1() throws Exception {
        String testUsername = "leaderboardUser1";
        int testType = 1; // Most Valuable Clothing

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        TestClientEndpoint clientEndpoint = new TestClientEndpoint();
        URI uri = new URI(websocketBaseUri + testUsername + "/" + testType);

        logger.info("Connecting to: {}", uri);
        clientSession = container.connectToServer(clientEndpoint, uri);
        assertNotNull(clientSession, "Client session should not be null after connection");
        assertTrue(clientSession.isOpen(), "Client session should be open");

        // Wait for the initial leaderboard message
        String initialMessage = messageQueue.poll(10, TimeUnit.SECONDS); // Wait up to 10 seconds
        logger.info("Received initial message: {}", initialMessage);

        assertNotNull(initialMessage, "Should receive an initial leaderboard message");
        // Basic validation: Check if it's a valid JSON array
        try {
            JSONArray leaderboard = new JSONArray(initialMessage);
            assertTrue(leaderboard.length() <= 10, "Leaderboard should have 0 to 10 entries");
            if (leaderboard.length() > 0) {
                 JSONObject firstEntry = leaderboard.getJSONObject(0);
                 // Assert based on the provided structure for Most Valuable Clothing (Type 1)
                 assertTrue(firstEntry.has("clothesId"), "Leaderboard entry should have clothesId");
                 assertTrue(firstEntry.has("itemName"), "Leaderboard entry should have itemName");
                 assertTrue(firstEntry.has("price"), "Leaderboard entry should have price");
                 assertTrue(firstEntry.has("user"), "Leaderboard entry should have user object");
                 assertTrue(firstEntry.getJSONObject("user").has("userId"), "User object should have userId");
                 assertTrue(firstEntry.getJSONObject("user").has("username"), "User object should have username");
                 assertTrue(firstEntry.has("clothingStats"), "Leaderboard entry should have clothingStats object");
                 assertTrue(firstEntry.getJSONObject("clothingStats").has("timesWorn"), "ClothingStats object should have timesWorn");
                 assertTrue(firstEntry.has("clothingType"), "Leaderboard entry should have clothingType");
                 // Can add type checks or value checks if specific values are expected
                 // e.g., assertEquals(expectedId, firstEntry.getLong("clothesId"));
            }
        } catch (Exception e) {
            fail("Received message is not a valid JSON array or structure is unexpected: " + initialMessage, e);
        }

        // Close session
        if (clientSession != null && clientSession.isOpen()) {
             logger.info("Closing session");
             clientSession.close();
        }
    }

    @Test
    public void testConnectAndReceiveLeaderboardType2() throws Exception {
        String testUsername = "leaderboardUser2";
        int testType = 2; // Users by Clothing Count

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        TestClientEndpoint clientEndpoint = new TestClientEndpoint();
        URI uri = new URI(websocketBaseUri + testUsername + "/" + testType);

        logger.info("Connecting to: {}", uri);
        clientSession = container.connectToServer(clientEndpoint, uri);
        assertNotNull(clientSession, "Client session should not be null after connection");
        assertTrue(clientSession.isOpen(), "Client session should be open");

        // Wait for the initial leaderboard message
        String initialMessage = messageQueue.poll(10, TimeUnit.SECONDS); // Wait up to 10 seconds
        logger.info("Received message for type 2: {}", initialMessage);

        assertNotNull(initialMessage, "Should receive an initial leaderboard message for type 2");
        // Basic validation: Check if it's a valid JSON array of arrays
        try {
            JSONArray leaderboard = new JSONArray(initialMessage);
            assertTrue(leaderboard.length() <= 10, "Leaderboard should have 0 to 10 entries");

            int previousCount = Integer.MAX_VALUE; // Initialize for order check

            for (int i = 0; i < leaderboard.length(); i++) {
                Object item = leaderboard.get(i);
                assertTrue(item instanceof JSONArray, "Leaderboard entry " + i + " should be a JSON array");
                JSONArray entry = (JSONArray) item;

                assertEquals(2, entry.length(), "Inner array should have length 2 (userId, clothingCount)");
                assertTrue(entry.get(0) instanceof Number, "First element (userId) should be a number");
                assertTrue(entry.get(1) instanceof Number, "Second element (clothingCount) should be a number");

                int currentCount = entry.getInt(1); // Get clothingCount
                assertTrue(currentCount <= previousCount, "Leaderboard should be sorted descending by clothingCount. Entry " + i + " violates order.");
                previousCount = currentCount; // Update for next comparison
            }

        } catch (Exception e) {
            fail("Received message is not a valid JSON array of [userId, clothingCount] pairs or structure is unexpected for type 2: " + initialMessage, e);
        }

        // Close session
        if (clientSession != null && clientSession.isOpen()) {
            logger.info("Closing session for type 2 test");
            clientSession.close();
        }
    }

    @Test
    public void testConnectAndReceiveLeaderboardType3() throws Exception {
        String testUsername = "leaderboardUser3";
        int testType = 3; // Most Expensive Outfits

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        TestClientEndpoint clientEndpoint = new TestClientEndpoint();
        URI uri = new URI(websocketBaseUri + testUsername + "/" + testType);

        logger.info("Connecting to: {}", uri);
        clientSession = container.connectToServer(clientEndpoint, uri);
        assertNotNull(clientSession, "Client session should not be null after connection");
        assertTrue(clientSession.isOpen(), "Client session should be open");

        // Wait for the initial leaderboard message
        String initialMessage = messageQueue.poll(10, TimeUnit.SECONDS); // Wait up to 10 seconds
        logger.info("Received message for type 3: {}", initialMessage);

        assertNotNull(initialMessage, "Should receive an initial leaderboard message for type 3");
        // Basic validation: Check if it's a valid JSON array of objects
        try {
            JSONArray leaderboard = new JSONArray(initialMessage);
            assertTrue(leaderboard.length() <= 10, "Leaderboard should have 0 to 10 entries");

            double previousPrice = Double.MAX_VALUE; // Initialize for order check

            for (int i = 0; i < leaderboard.length(); i++) {
                Object item = leaderboard.get(i);
                assertTrue(item instanceof JSONObject, "Leaderboard entry " + i + " should be a JSON object");
                JSONObject entry = (JSONObject) item;

                // Assert structure based on actual Type 3 data
                assertTrue(entry.has("outfitId"), "Type 3 entry should have outfitId");
                assertTrue(entry.get("outfitId") instanceof Number, "outfitId should be a number");
                assertTrue(entry.has("totalPrice"), "Type 3 entry should have totalPrice");
                assertTrue(entry.get("totalPrice") instanceof Number, "totalPrice should be a number");

                // Verify descending order by totalPrice
                double currentPrice = entry.getDouble("totalPrice");
                assertTrue(currentPrice <= previousPrice, "Leaderboard should be sorted descending by totalPrice. Entry " + i + " violates order.");
                previousPrice = currentPrice; // Update for next comparison
            }
        } catch (Exception e) {
            fail("Received message is not a valid JSON array of {totalPrice, outfitId} objects or structure is unexpected for type 3: " + initialMessage, e);
        }

        // Close session
        if (clientSession != null && clientSession.isOpen()) {
            logger.info("Closing session for type 3 test");
            clientSession.close();
        }
    }

    // --- Inner Client Endpoint Class ---

    @ClientEndpoint
    public static class TestClientEndpoint {
        private final Logger logger = LoggerFactory.getLogger(TestClientEndpoint.class);

        @OnOpen
        public void onOpen(Session session) {
            logger.info("[Client] Connected: Session {}", session.getId());
        }

        @OnMessage
        public void onMessage(String message) {
            logger.info("[Client] Message Received: {}", message);
            try {
                // Add message to the queue for the main thread
                messageQueue.put(message);
            } catch (InterruptedException e) {
                logger.error("Interrupted while adding message to queue", e);
                Thread.currentThread().interrupt();
            }
        }

        @OnClose
        public void onClose(Session session, CloseReason reason) {
            logger.info("[Client] Disconnected: Session {}, Reason: {}", session.getId(), reason);
        }

        @OnError
        public void onError(Session session, Throwable throwable) {
            logger.error("[Client] Error: Session {}", session.getId(), throwable);
        }
    }
}
