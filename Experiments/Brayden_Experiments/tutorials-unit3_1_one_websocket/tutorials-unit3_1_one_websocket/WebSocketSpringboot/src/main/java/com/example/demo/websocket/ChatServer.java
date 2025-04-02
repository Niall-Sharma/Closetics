package com.example.demo.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Represents a WebSocket chat server for handling real-time communication
 * between users. Each user connects to the server using their unique
 * username.
 *
 * This class is annotated with Spring's `@ServerEndpoint` and `@Component`
 * annotations, making it a WebSocket endpoint that can handle WebSocket
 * connections at the "/chat/{username}" endpoint.
 *
 * Example URL: ws://localhost:8080/chat/username
 *
 * The server provides functionality for broadcasting messages to all connected
 * users and sending messages to specific users.
 */
@ServerEndpoint("/chat/{username}")
@Component
public class ChatServer {

    // Store all socket session and their corresponding username
    // Two maps for the ease of retrieval by key
    private static Map < Session, String > sessionUsernameMap = new Hashtable < > ();
    private static Map < String, Session > usernameSessionMap = new Hashtable < > ();

    // server side logger
    private final Logger logger = LoggerFactory.getLogger(ChatServer.class);

    /**
     * This method is called when a new WebSocket connection is established.
     *
     * @param session represents the WebSocket session for the connected user.
     * @param username username specified in path parameter.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {

        // server side log
        logger.info("[onOpen] " + username);

        // Handle the case of a duplicate username
        if (usernameSessionMap.containsKey(username)) {
            session.getBasicRemote().sendText("Username already exists");
            session.close();
        }
        else {
            // map current session with username
            sessionUsernameMap.put(session, username);

            // map current username with session
            usernameSessionMap.put(username, session);

            // send to the user joining in
            sendMessageToPArticularUser(username, "Welcome to the chat server, "+username);

            // send to everyone in the chat
            broadcast("User: " + username + " has Joined the Chat");
        }
    }

    /**
     * Handles incoming WebSocket messages from a client.
     *
     * @param session The WebSocket session representing the client's connection.
     * @param message The message received from the client.
     */
    // Store active RPS games: key is the user who created the challenge, value is their choice
    private static Map<String, String> rpsChallenges = new HashMap<>();

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        String username = sessionUsernameMap.get(session);

        // Handle Rock-Paper-Scissors challenge with an initial move
        if (message.startsWith("/rps")) {
            String[] parts = message.split("\\s+");
            if (parts.length != 2) {
                sendMessageToPArticularUser(username, "Invalid format. Use /rps <rock|paper|scissors>.");
                return;
            }

            String choice = parts[1].toLowerCase();
            if (!choice.equals("rock") && !choice.equals("paper") && !choice.equals("scissors")) {
                sendMessageToPArticularUser(username, "Invalid choice. Use /rps <rock|paper|scissors>.");
                return;
            }

            if (rpsChallenges.containsKey(username)) {
                sendMessageToPArticularUser(username, "You already issued a challenge. Wait for an opponent.");
                return;
            }

            // Store the challenger's move
            rpsChallenges.put(username, choice);
            broadcast(username + " has started a Rock Paper Scissors game type /rock, /paper, or /scissors to accept the challenge.");
            return;
        }

        // Handle opponent response
        if (message.equalsIgnoreCase("/rock") || message.equalsIgnoreCase("/paper") || message.equalsIgnoreCase("/scissors")) {
            if (rpsChallenges.isEmpty()) {
                sendMessageToPArticularUser(username, "No active challenges. Start a game with /rps <rock|paper|scissors>.");
                return;
            }

            // Find the first available challenger
            String challenger = null;
            for (String player : rpsChallenges.keySet()) {
                challenger = player;
                break;
            }

            if (challenger == null || challenger.equals(username)) {
                sendMessageToPArticularUser(username, "You cannot accept your own challenge.");
                return;
            }

            String challengerChoice = rpsChallenges.get(challenger);
            String opponentChoice = message.substring(1); // Remove the '/' character

            // Calculate and broadcast the result
            String result = determineWinner(challenger, challengerChoice, username, opponentChoice);
            broadcast(result);

            // Clear the game data
            rpsChallenges.remove(challenger);
            return;
        }

        // Default: broadcast normal chat messages
        broadcast(username + ": " + message);
    }

    /**
     * Determines the winner of Rock Paper Scissors
     */
    private String determineWinner(String player1, String choice1, String player2, String choice2) {
        if (choice1.equals(choice2)) {
            return "It's a tie! " + player1 + " and " + player2 + ", please try again.";
        }

        if ((choice1.equals("rock") && choice2.equals("scissors")) ||
                (choice1.equals("scissors") && choice2.equals("paper")) ||
                (choice1.equals("paper") && choice2.equals("rock"))) {
            return player1 + " (" + choice1 + ") beats " + player2 + " (" + choice2 + ")! " + player1 + " wins!";
        } else {
            return player2 + " (" + choice2 + ") beats " + player1 + " (" + choice1 + ")! " + player2 + " wins!";
        }
    }

    /**
     * Handles the closure of a WebSocket connection.
     *
     * @param session The WebSocket session that is being closed.
     */
    @OnClose
    public void onClose(Session session) throws IOException {

        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // server side log
        logger.info("[onClose] " + username);

        // remove user from memory mappings
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);

        // send the message to chat
        broadcast(username + " disconnected");
    }

    /**
     * Handles WebSocket errors that occur during the connection.
     *
     * @param session   The WebSocket session where the error occurred.
     * @param throwable The Throwable representing the error condition.
     */
    @OnError
    public void onError(Session session, Throwable throwable) {

        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // do error handling here
        logger.info("[onError]" + username + ": " + throwable.getMessage());
    }

    /**
     * Sends a message to a specific user in the chat (DM).
     *
     * @param username The username of the recipient.
     * @param message  The message to be sent.
     */
    private void sendMessageToPArticularUser(String username, String message) {
        try {
            usernameSessionMap.get(username).getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.info("[DM Exception] " + e.getMessage());
        }
    }

    /**
     * Broadcasts a message to all users in the chat.
     *
     * @param message The message to be broadcasted to all users.
     */
    private void broadcast(String message) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.info("[Broadcast Exception] " + e.getMessage());
            }
        });
    }
}