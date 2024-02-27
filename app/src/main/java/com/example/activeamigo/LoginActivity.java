package com.example.activeamigo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends Activity implements Alertable{
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

        // Check if user is already logged in
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }

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
                            auth.signInWithEmailAndPassword(email, password)
                                    // If login fails
                                    .addOnFailureListener(e -> showAlert(LoginActivity.this, R.string.misMatchAccountInfo))
                                    // If login succeeds
                                    .addOnSuccessListener(authResult -> {
                                        Log.d("USER:",auth.getCurrentUser().getEmail() + auth.getCurrentUser().toString());
                                        Toast.makeText(LoginActivity.this, R.string.loggedIn, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    });
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

    // Makes sure the fields are filled in
    protected boolean checkInputs(String email, String password){
        return !email.trim().isEmpty() && !password.trim().isEmpty();
    }

}
