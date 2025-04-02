package com.example.androidexample;

import org.java_websocket.handshake.ServerHandshake;

/**
 * Interface defining methods for handling WebSocket events.
 * Implement this interface to listen for WebSocket connection,
 * message, closure, and error events.
 */
public interface WebSocketListener {

    /**
     * Called when the WebSocket connection is successfully opened.
     *
     * @param handshakedata Information about the server handshake.
     */
    void onWebSocketOpen(ServerHandshake handshakedata);

    /**
     * Called when a WebSocket message is received.
     *
     * @param message The received WebSocket message.
     */
    void onWebSocketMessage(String message);

    /**
     * Called when the WebSocket connection is closed.
     *
     * @param code   The status code indicating the reason for closure.
     * @param reason A human-readable explanation for the closure.
     * @param remote Indicates whether the closure was initiated by the remote endpoint.
     */
    void onWebSocketClose(int code, String reason, boolean remote);

    /**
     * Called when an error occurs in the WebSocket communication.
     *
     * @param ex The exception that describes the error.
     */
    void onWebSocketError(Exception ex);
}
