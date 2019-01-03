package com.example.myfirstapp.Chess;

public class ChessPiece {
    private PieceType pieceType;
    private ChessColor color;

    public ChessPiece(PieceType pieceType, ChessColor color){
        this.pieceType = pieceType;
        this.color = color;
    }

    public String getCode(){
        return pieceType.getCode() + color.getCode();
    }
}
