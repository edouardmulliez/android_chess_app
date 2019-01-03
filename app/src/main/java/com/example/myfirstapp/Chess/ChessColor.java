package com.example.myfirstapp.Chess;

public enum ChessColor {
    BLACK,
    WHITE;

    public String getCode(){
        switch (this){
            case BLACK: return "B";
            case WHITE: return "W";
            default: return "UNKNOWN";
        }
    }
}
