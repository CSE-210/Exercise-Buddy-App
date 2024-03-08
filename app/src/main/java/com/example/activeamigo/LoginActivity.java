package com.example.activeamigo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends Activity implements Alertable, DAO{
    private FirebaseFirestore db = null;
    private EditText emailView = null;
    private EditText passwordView = null;

    private FirebaseAuth auth = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        String fsName = "Accounts";

        emailView = findViewById(R.id.editTextEmailLG);
        passwordView = findViewById(R.id.editTextPasswordLG);

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
                        DocumentSnapshot ds = task.getResult();
                        if(ds == null || !ds.exists()) {
                            showAlert(this, R.string.notAccountFound);
                            emailView.setText("");
                            passwordView.setText("");
                        }
                        // If the account exists
                        else {
                            auth.signInWithEmailAndPassword(email, password)
                                    // If login fails
                                    .addOnFailureListener(e -> {
                                        showAlert(LoginActivity.this, R.string.misMatchAccountInfo);
                                        passwordView.setText("");
                                    })
                                    // If login succeeds
                                    .addOnSuccessListener(authResult -> {
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    });
                        }
                    }
                }).addOnFailureListener(task -> showAlert(this, R.string.queryError));
            }
        });

        // Launch Register Page
        findViewById(R.id.buttonRegister).setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        });


        // Launch forgot password
        findViewById(R.id.forgotPasswordText).setOnClickListener(view ->{
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

    }

    // Makes sure the fields are filled in
    protected boolean checkInputs(String email, String password){
        return !email.trim().isEmpty() && !password.trim().isEmpty();
    }

}
