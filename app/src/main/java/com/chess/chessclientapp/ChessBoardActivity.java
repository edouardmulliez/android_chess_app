package com.chess.chessclientapp;

// TODO: instead of this package hack, properly put this message in a separate package (separate
// from client and server)


import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.chess.chessclientapp.helper.ChessAdapter;
import com.chess.chessclientapp.helper.Identification;
import com.chess.server.model.GameMessage;

import org.apache.commons.lang3.SerializationUtils;

import chessgame.Game;
import chessgame.model.Color;
import chessgame.model.Move;
import chessgame.model.Position;

public class ChessBoardActivity extends AppCompatActivity {

    private Activity activity;
    private Game game = new Game();
    private Color playerColor;

    private Position lastPosition; // Store the last Position where the player has clicked
    private Position start;
    private boolean isFirstClick = true;

    private WebSocketClient socketClient;

    /**
     * TODO:
     * - Handle connection failure
     * - Handle end of game --> inform who has won, new screen with that info
     * - Think about matching: choice of player? random? score?
     * - use handler instead of AsyncTask?
     *
     * - Do not start the game if the user has not been matched. Waiting for opponent activity?
     */

    private class WebSocketConnection extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            // Connect to websocket
            String user = Identification.id(getApplicationContext());
            socketClient = new WebSocketClient(user);
            return null;
        }

        protected void onPostExecute(Void result) {
            // add listeners for websocket messages
            socketClient.addMessageHandler(new WebSocketClient.TextMessageHandler() {
                @Override
                public void handleMessage(final String message) {
                    final TextView textView = findViewById(R.id.textView);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(message);
                        }
                    });
                }
            });

            socketClient.addMessageHandler(new WebSocketClient.BinaryMessageHandler() {
                @Override
                public void handleMessage(final byte[] message) {
                    GameMessage gameMessage = SerializationUtils.deserialize(message);
                    game = gameMessage.game();
                    playerColor = gameMessage.clientPlayerColor();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final BoardView boardView = findViewById(R.id.boardView);
                            boardView.refreshView(game, playerColor);
                            updateTurnColorView();
                        }
                    });
                }
            });
        }
    }

    private class SendMessageTask extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... messages) {
            for (String message: messages){
                socketClient.sendMessage(message);
            }
            return null;
        }
    }

    private class SendBinaryMessageTask extends AsyncTask<byte[], Void, Void> {
        protected Void doInBackground(byte[]... messages) {
            for (byte[] message: messages){
                socketClient.sendMessage(message);
            }
            return null;
        }
    }

    /**
     * This functions is also called when the screen orientation changes or when user closes
     * and then reopens the app. So, it should:
     * - avoid reopening websocket connection if the connection is already open
     * - ask server for current state each time it is called and refresh accordingly
     *
     *
     * - Better handle state loss: use Android ViewModel? --> separate Activity (interface) vs ViewModel (data)
     * - deconnect websocket when instance is destroyed?
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess_board);
        activity = this;

        new WebSocketConnection().execute();

        playerColor = Color.WHITE;

        final BoardView boardView = findViewById(R.id.boardView);
        boardView.setOnTouchListener(touchListener);
        boardView.setOnClickListener(clickListener);
        updateTurnColorView();
    }

    // the purpose of the touch listener is just to store the touch coordinates
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int clickRow;
            int clickCol;
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                clickCol = (int)(event.getX() / v.getWidth() * 8);
                clickRow = (int)(event.getY() / v.getHeight() * 8);
                lastPosition = new Position(ChessAdapter.gameToViewRow(clickRow, playerColor), clickCol);
            }
            // let the touch event pass on to whoever needs it
            return false;
        }
    };

    // we register the positions of the clicks to move the pieces
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BoardView boardView = (BoardView)v;

            if (isFirstClick){
                start = lastPosition;
                if (isValidStart(start)){
                    boardView.refreshView(game, playerColor, start);
                    isFirstClick = false;
                }
            } else {
                boolean isValidMove = game.move(start, lastPosition);
                if (isValidMove){
                    boardView.refreshView(game, playerColor);
                    updateTurnColorView();
                    // send move to server
                    byte[] message = SerializationUtils.serialize(new Move(start, lastPosition));
                    new SendBinaryMessageTask().execute(message);
                } else {
                    // remove border around selected first square
                    boardView.refreshView(game, playerColor);
                }
                isFirstClick = true;
            }
        }
    };

    private void updateTurnColorView(){
        final TextView textView = findViewById(R.id.textView);
        String message = game.getTurnColor().toString() + " TO PLAY";
        textView.setText(message);
    }

    private boolean isValidStart(Position start){
        return (playerColor == game.getTurnColor() &&
                playerColor == game.getPiece(start).color());
    }
}
