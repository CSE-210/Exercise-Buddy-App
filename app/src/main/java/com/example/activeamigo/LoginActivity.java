package com.example.activeamigo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends Activity implements Alertable{
    private FirebaseFirestore db = null;
    private EditText emailView = null;
    private EditText passwordView = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();
        String fsName = "Accounts";

        emailView = findViewById(R.id.editTextEmailLG);
        passwordView = findViewById(R.id.editTextpasswordLG);

        findViewById(R.id.buttonLogIn).setOnClickListener(view -> {
            String email = emailView.getText().toString();
            String password = passwordView.getText().toString();

            // Checks that inputs are valid
            if( !checkInputs(email, password)){
                showAlert(this, R.string.createAccountErrorFilledIn);
            }

            // Log in attempt
            else{
                // Grabs account from database
                Task<DocumentSnapshot> res = checkAccount(email, fsName, this.db);

                res.addOnCompleteListener(task -> {
                    // If it grabs an account successfully
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        // If the account exists
                        if (documentSnapshot != null) {
                            // Grab the password
                            String passwordDB = documentSnapshot.getString("password");
                            // ADD decryption

                            // Check the password
                            if (checkPassword(password, passwordDB))
                                // Logg-in in actions
                                showAlert(this, R.string.loggedIn);

                            else
                                showAlert(this, R.string.misMatchAccountInfo);
                        }
                    }
                });
            }
        });

        // Launch Register Page
        findViewById(R.id.buttonRegister).setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        });


        // Launch forgot password
        findViewById(R.id.forgotPasswordText).setOnClickListener(view -> showAlert(this, R.string.press_test));

    }

    // Grabs the account from the dataBase
    protected Task<DocumentSnapshot> checkAccount(String emailAddress, String dbName, FirebaseFirestore fs) {
        final TaskCompletionSource<DocumentSnapshot> tcs = new TaskCompletionSource<>();
        fs.collection(dbName)
                .whereEqualTo("email", emailAddress)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            tcs.setResult(document);
                        } else {
                            showAlert(this, R.string.notAccountFound);
                        }
                    } else {
                        showAlert(this, R.string.queryError);
                    }
                });

        return tcs.getTask();
    }

    // Checks if password matches the one in the database
    protected boolean checkPassword(String local, String database) {
        return local.equals(database);
    }

    // Makes sure the fields are filled in
    protected boolean checkInputs(String email, String password){
        return !email.trim().isEmpty() && !password.trim().isEmpty();
    }

}
