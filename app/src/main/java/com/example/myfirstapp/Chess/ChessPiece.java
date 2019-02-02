package com.example.myfirstapp.Chess;

import java.util.LinkedList;
import java.util.List;


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

    public static List<ChessPiece> getPieces(){
        List<ChessPiece> pieces = new LinkedList<>();
        for (PieceType pieceType: PieceType.values()) {
            for (ChessColor color: ChessColor.values()) {
                pieces.add(new ChessPiece(pieceType, color));
            }
        }
        return pieces;
    }
}
