package com.example.activeamigo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class CreateAccountActivity extends AppCompatActivity {
    String inputError = "Must fill out all sections";
    String ucsdDomain = "ucsd.edu";
    String emailError = "Must be a valid UCSD email";
    String passwordError = "Passwords do not match";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        findViewById(R.id.buttonCreateAccount).setOnClickListener(view -> {
            // Find the EditText views by their IDs
            EditText editTextName = findViewById(R.id.editTextName);
            EditText editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
            EditText editTextPassword = findViewById(R.id.editTextPassword);
            EditText editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirm);

            // Get the text from the EditText fields
            String name = editTextName.getText().toString();
            String emailAddress = editTextEmailAddress.getText().toString();
            String password = editTextPassword.getText().toString();
            String passwordConfirm = editTextPasswordConfirm.getText().toString();

            int domainPos = emailAddress.indexOf("@");

            // If a section is left empty
            if(name.isEmpty() || emailAddress.isEmpty() ||
                    password.isEmpty() || passwordConfirm.isEmpty()){
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccountActivity.this);
                builder.setMessage(inputError)
                        .setTitle("Warning")
                        .setPositiveButton("OK", null); // No action on click
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            // Bad email or non ucsd email
            else if(domainPos == -1 || !emailAddress.substring(domainPos+1).equals(ucsdDomain)){
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccountActivity.this);
                builder.setMessage(emailError)
                        .setTitle("Warning")
                        .setPositiveButton("OK", null); // No action on click
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            // If the password do not match
            else if(!password.equals(passwordConfirm)){
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccountActivity.this);
                builder.setMessage(passwordError)
                        .setTitle("Warning")
                        .setPositiveButton("OK", null); // No action on click
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}