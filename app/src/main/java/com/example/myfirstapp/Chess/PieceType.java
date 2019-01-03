package com.example.myfirstapp.Chess;

public enum PieceType {
    KING,
    QUEEN,
    ROOK,
    BISHOP,
    KNIGHT,
    PAWN;

    public String getCode(){
        switch (this){
            case KING: return "K";
            case QUEEN: return "Q";
            case ROOK: return "R";
            case BISHOP: return "B";
            case KNIGHT: return "N";
            case PAWN: return "P";
            default: return "UNKNOWN";
        }
    }
}
