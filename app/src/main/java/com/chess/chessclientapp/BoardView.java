package com.chess.chessclientapp;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.chess.chessclientapp.helper.ChessAdapter;

import java.util.HashMap;
import java.util.Map;

import chessgame.Game;
import chessgame.model.Color;
import chessgame.model.Piece;
import chessgame.model.PieceType;
import chessgame.model.Position;

/**
 * Custom view where the chess board and its pieces are drawn
 */
public class BoardView extends View {
    private Map<String, Drawable> chessDrawables;

    private Paint mPaint = new Paint();
    private Color playerColor = Color.WHITE; // is used to put the board in the right orientation
    private Game game = new Game();
    private Position selectedPosition = null;

    public BoardView(Context context) {
        super(context);
        init(context,null, 0);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.BoardView, defStyle, 0);
        a.recycle();

        initChessDrawables(context);
    }

    private void initChessDrawables(Context context){
        chessDrawables = new HashMap<>();
        Resources resources = context.getResources();
        Map<String, String> codeToResourceName = ChessAdapter.codeToResourceName();
        for (Map.Entry<String, String> entry : codeToResourceName.entrySet()) {
            String code = entry.getKey();
            String resourceName = entry.getValue();
            int resourceId = getResources()
                    .getIdentifier(resourceName, "drawable", context.getPackageName());
            chessDrawables.put(code, resources.getDrawable(resourceId));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);

    }

    public void refreshView(Game game, Color playerColor){
        refreshView(game, playerColor, null);
    }
    public void refreshView(Game game, Color playerColor,  Position selectedPosition){
        this.game = game;
        this.playerColor = playerColor;
        this.selectedPosition = selectedPosition;
        invalidate();
    }

    private void drawBoard(Canvas canvas){
        int paddingLeft = 0;
        int paddingTop = 0;
        int paddingRight = 0;
        int paddingBottom = 0;

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        int nRow = 8;
        int squareWidth = contentWidth / nRow;
        int squareHeight = contentHeight / nRow;

        mPaint.setStyle(Paint.Style.FILL);
        int darkSquareColor = ContextCompat.getColor(getContext(), R.color.boardSquareDark);
        int lightSquareColor = ContextCompat.getColor(getContext(), R.color.boardSquareLight);
        int selectedSquareColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);

        // Draw board squares
        for (int row=0; row<nRow; row++){
            for (int col=0; col<nRow; col++){
                if ((col + row) % 2 == 0) {
                    mPaint.setColor(darkSquareColor);
                } else {
                    mPaint.setColor(lightSquareColor);
                }
                    canvas.drawRect(
                        paddingLeft + col * squareWidth,
                        paddingTop + row * squareHeight,
                        paddingLeft + (col + 1) * squareWidth,
                        paddingTop + (row + 1) * squareHeight,
                        mPaint
                    );
            }
        }
        // Border around the board
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(darkSquareColor);
        canvas.drawRect(
                paddingLeft,
                paddingTop,
                paddingLeft + contentWidth,
                paddingTop + contentHeight,
                mPaint
        );

        for (int row = 0; row < nRow; row++){
            for (int col = 0; col < nRow; col++){
                Piece piece = game.getPiece(
                        new Position(ChessAdapter.gameToViewRow(row, playerColor), col));
                if (piece.pieceType() != PieceType.EMPTY){
                    Drawable d = chessDrawables.get(ChessAdapter.getCode(piece));
                    d.setBounds(
                            paddingLeft + col * squareWidth,
                            paddingTop + row * squareWidth,
                            paddingLeft + (col + 1) * squareWidth,
                            paddingTop + (row + 1) * squareWidth);
                    d.draw(canvas);
                }
            }
        }

        // putting a border around the selected position
        if (selectedPosition != null){
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(5);
            mPaint.setColor(selectedSquareColor);
            int row = ChessAdapter.gameToViewRow(selectedPosition.row(), playerColor);
            int col = selectedPosition.col();
            canvas.drawRect(
                    paddingLeft + col * squareWidth,
                    paddingTop + row * squareWidth,
                    paddingLeft + (col + 1) * squareWidth,
                    paddingTop + (row + 1) * squareWidth,
                    mPaint
            );
        }



    }
}
