package com.example.chessclientapp.helper;

import java.util.HashMap;
import java.util.Map;

import chessgame.model.Color;
import chessgame.model.Piece;
import chessgame.model.PieceType;

public class ChessAdapter {

    private static Map<String, String> codeToResourceName = new HashMap<>();

    static {
        String[] pieceCodes = {"K", "Q", "B", "N", "R", "P"};
        String[] colorCodes = {"B", "W"};
        for (String pieceCode: pieceCodes){
            for (String colorCode: colorCodes){
                String code = pieceCode + colorCode;
                codeToResourceName.put(code, "chess_" + code.toLowerCase());
            }
        }
    }

    public static Map<String, String> codeToResourceName(){
        return codeToResourceName;
    }

    public static String getCode(Piece piece){
        return getCode(piece.pieceType()) + getCode(piece.color());
    }

    public static int gameToViewRow(int gameRow, Color playerColor){
        if (playerColor == Color.BLACK){
            return gameRow;
        } else {
            return 7 - gameRow;
        }

    }

    private static String getCode(PieceType pieceType){
        switch (pieceType){
            case KING:
                return "K";
            case QUEEN:
                return "Q";
            case BISHOP:
                return "B";
            case KNIGHT:
                return "N";
            case ROOK:
                return "R";
            case PAWN:
                return "P";
            case EMPTY:
                return null;
            default:
                return null;
        }
    }

    private static String getCode(Color color){
        switch (color){
            case WHITE:
                return "W";
            case BLACK:
                return "B";
            default:
                return null;
        }
    }
}
