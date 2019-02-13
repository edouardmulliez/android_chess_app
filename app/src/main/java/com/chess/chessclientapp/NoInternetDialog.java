package com.chess.chessclientapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class NoInternetDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle(R.string.dialog_no_internet)
                .setMessage(R.string.dialog_no_internet_details)
                .setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}