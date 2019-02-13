package com.chess.server.model;

import chessgame.Game;
import chessgame.model.Color;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class GameMessage implements Serializable {
    private Game game;
    private Color clientPlayerColor;

    public GameMessage(Game game, Color clientPlayerColor){
        this.game = game;
        this.clientPlayerColor = clientPlayerColor;
    }

    public ByteBuffer toByteBuffer(){
        return ByteBuffer.wrap(SerializationUtils.serialize(this));
    }

    public Game game(){ return game; }
    public Color clientPlayerColor(){ return clientPlayerColor; }
}
