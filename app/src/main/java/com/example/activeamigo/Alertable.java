package com.example.activeamigo;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

/** Creates dialog boxes in the current context**/
public interface Alertable {

    /** Creates a dialog box of the given context with the message ID
     USAGE: showAlert(this, R.string.~~~~) **/
    default void showAlert(Context context, int messageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(messageId)
                .setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
