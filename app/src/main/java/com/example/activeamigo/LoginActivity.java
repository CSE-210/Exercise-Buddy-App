package com.example.activeamigo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

public class LoginActivity extends Activity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Login Button Action
        findViewById(R.id.buttonLogIn).setOnClickListener(view -> {
            showAlert(R.string.press_test);

        });

        // Launch Register Page
        findViewById(R.id.buttonRegister).setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(intent);

        });


        // Launch forgot password
        findViewById(R.id.forgotPasswordText).setOnClickListener(view -> showAlert(R.string.press_test));
    }

    // used to show dialogs
    protected void showAlert(int messageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(messageId)
                .setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
