package com.example.activeamigo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {
    
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        
        db = FirebaseFirestore.getInstance();

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

            // Call validateInformation function before adding account to database
            if(validateInformation(name, emailAddress, password, passwordConfirm)){
                dbCalls(name, emailAddress, password);
            }
        });
    }

    private boolean validateInformation(String name, String emailAddress, String password, String passwordConfirm) {
        int domainPos = emailAddress.indexOf("@");
        String domain = emailAddress.substring(domainPos + 1);
        boolean res = false;

        // If a section is left empty
        if (name.isEmpty() || emailAddress.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            showAlert(R.string.createAccountErrorFilledIn);
        }
        // Bad email or non ucsd email
        else if (domainPos < 1 || !domain.equals(getString(R.string.ucsdDomain))) {
            showAlert(R.string.createAccountErrorBadEmail);
        }
        // If the passwords do not match
        else if (!password.equals(passwordConfirm)) {
            showAlert(R.string.createAccountErrorMismatchPassword);
        }
        else res = true;
        return res;
    }

    private void showAlert(int messageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccountActivity.this);
        builder.setMessage(messageId)
                .setTitle("Warning")
                .setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void dbCalls(String name, String emailAddress, String password) {
        Query query = db.collection("mockAccounts").whereEqualTo("Email", emailAddress);

        query.get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();

                // An account with this email address already exists
                if (querySnapshot != null && !querySnapshot.isEmpty()) {

                    showAlert(R.string.accountCreationEmailExists);
                }

                else {
                    Map<String, Object> accountData = new HashMap<>();
                    accountData.put("Name", name);
                    accountData.put("Email", emailAddress);
                    accountData.put("Password", password);

                    // Add the account data to db
                    db.collection("mockAccounts").document().set(accountData, SetOptions.merge())
                            // Success!
                            .addOnSuccessListener(aVoid -> {
                                showAlert(R.string.accountCreationSuccess);
                            })
                            // Failure :(
                            .addOnFailureListener(e -> {

                                showAlert(R.string.accountCreationFailed);
                            });
                }
            }
            // db query failed
            else {
                showAlert(R.string.queryError);
            }
        });
    }
}
