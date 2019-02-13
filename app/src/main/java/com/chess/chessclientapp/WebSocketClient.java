package com.chess.chessclientapp;

import java.net.URI;
import java.nio.ByteBuffer;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;


// TODO:
// - try secure websocket
//

@ClientEndpoint
public class WebSocketClient {

    Session userSession = null;
    private TextMessageHandler textMessageHandler;
    private BinaryMessageHandler binaryMessageHandler;


    public WebSocketClient(String user) {
        final String DEBUG_HOST = "10.0.2.2";
        final String HOST = "192.168.0.21"; // the current IP of my mac on my local network
        try {
            String host = HOST;
            URI endpointURI = new URI(String.format("ws://%s:8080/%s/", host, user));
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();


            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("opening websocket");
        this.userSession = userSession;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("closing websocket");
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(String message) {
        if (this.textMessageHandler != null) {
            this.textMessageHandler.handleMessage(message);
        }
    }

    @OnMessage
    public void onMessage(byte[] message) {
        if (this.binaryMessageHandler != null) {
            this.binaryMessageHandler.handleMessage(message);
        }
    }

    public void addMessageHandler(TextMessageHandler msgHandler) {
        this.textMessageHandler = msgHandler;
    }

    public void addMessageHandler(BinaryMessageHandler msgHandler) {
        this.binaryMessageHandler = msgHandler;
    }

    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }

    public void sendMessage(byte[] message) {
        this.userSession.getAsyncRemote().sendBinary(ByteBuffer.wrap(message));
    }

    public static interface TextMessageHandler {
        public void handleMessage(String message);
    }

    public static interface BinaryMessageHandler {
        public void handleMessage(byte[] message);
    }

}