package com.chess.chessclientapp;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.chess.chessclientapp.helper.AppStatus;


/**
 * TODO: Try on real device + real wifi connection
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!AppStatus.isOnline(getApplicationContext())){
            openNoInternetDialog();
        }
    }

    /**
     * The function below is called when user clicks on the button "Send"
     */
    public void sendMessage(View view){
        Intent intent = new Intent(this, ChessBoardActivity.class);
        startActivity(intent);
    }


    public void openNoInternetDialog() {
        DialogFragment newFragment = new NoInternetDialog();
        newFragment.show(getFragmentManager(), "noInternet");
    }
}
