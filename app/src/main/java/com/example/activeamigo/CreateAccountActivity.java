package com.example.activeamigo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CreateAccountActivity extends AppCompatActivity implements Alertable {
    private enum Day {mon, tue, wed, thu, fri, sat, sun}
    protected FirebaseFirestore db;
    private FirebaseAuth auth;
    static final String desiredDomain = "ucsd.edu";

    EditText editTextName = null;
    EditText editTextEmailAddress = null;
    EditText editTextPassword = null;
    EditText editTextPasswordConfirm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        String fSName = getResources().getString(R.string.dbAccounts);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Find the EditText views by their IDs
        editTextName = findViewById(R.id.editTextNameAC);
        editTextEmailAddress = findViewById(R.id.editTextEmailAddressAC);
        editTextPassword = findViewById(R.id.editTextPasswordAC);
        editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirmAC);

        // Setting up action bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.buttonCreateAccount).setOnClickListener(view -> {

            // Get the text from the EditText fields
            String name = editTextName.getText().toString();
            String emailAddress = editTextEmailAddress.getText().toString();
            String password = editTextPassword.getText().toString();
            String passwordConfirm = editTextPasswordConfirm.getText().toString();

            // Call validateInformation function before adding account to database
            if (validateInformation(name, emailAddress, password, passwordConfirm)) {
                checkEmail(emailAddress, fSName, this.db).addOnCompleteListener(task -> {
                   if(task.isSuccessful()){
                        DocumentSnapshot ds = task.getResult();
                        if(ds != null){
                            // Checks email
                            showAlert(this, R.string.accountCreationEmailExists);
                        }
                        else{
                            // Adds account to authentication and database
                            auth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(task1 -> {
                                if(task1.isSuccessful()){
                                    addAccount(name, emailAddress, fSName);
                                    clearForm(false);
                                }
                                else{
                                    showAlert(CreateAccountActivity.this, R.string.accountCreationFailed);
                                }
                            });
                        }
                   }
                   else{
                        showAlert(this, R.string.queryError);
                   }
                });
            }
        });
    }

    // Makes sure the information in the from is filled in, a good email, matching passwords
    private boolean validateInformation(String name, String emailAddress, String password, String passwordConfirm) {
        String emailPattern = "^[A-Za-z0-9._%+-]+@" + desiredDomain + "$"
                ;
        if (name.isEmpty() || emailAddress.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty() ||
                name.trim().isEmpty() || emailAddress.trim().isEmpty() || password.trim().isEmpty()||
              passwordConfirm.trim().isEmpty()) {
            showAlert(this, R.string.createAccountErrorFilledIn);
        }
        // Bad email or non ucsd email
        else if (!emailAddress.matches(emailPattern)) {
            showAlert(this, R.string.createAccountErrorBadEmail);
        }
        // If the passwords do not match
        else if (!password.equals(passwordConfirm)) {
            showAlert(this, R.string.createAccountErrorMismatchPassword);
            clearForm(true);
        }
        // If the passwords do not match
        else if (password.length() < 6) {
            showAlert(this, R.string.createAccountShortPassword);
            clearForm(true);
        } else if (password.contains(" ")) {
            showAlert(this, R.string.passwordWithWhiteSpace);
            clearForm(true);
        } else return true;

        return false;
    }

    // Check if the email already exists in the database
    protected Task<DocumentSnapshot> checkEmail(String emailAddress, String dbName, FirebaseFirestore fs) {
        final TaskCompletionSource<DocumentSnapshot> tcs  = new TaskCompletionSource<>();
        fs.collection(dbName)
                .whereEqualTo("email", emailAddress)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            tcs.setResult(querySnapshot.getDocuments().get(0));
                        }
                        else{
                            tcs.setResult(null);
                        }
                    }
                    else {
                        // Error occurred while checking for email existence
                        showAlert(this, R.string.queryError);
                    }
                });
        return tcs.getTask();
    }

    // Adds account to database
    protected void addAccount(String name, String emailAddress, String dbName) {
        HashMap<String, Object> accountData = makeAccount(name, emailAddress);

        // Add the account data to db
        db.collection(dbName)
                .document(emailAddress)
                .set(accountData, SetOptions.merge())
                // Success!
                .addOnSuccessListener(aVoid -> {
                    showAlert(this, R.string.accountCreationSuccess);
                    clearForm(false);
                })
                // Failure :(
                .addOnFailureListener(e -> showAlert(this, R.string.accountCreationFailed));
    }

    /** Used to clear the text inputs
     If true will only clear the passwords **/
    private void clearForm(boolean pass) {
        if (!pass) {
            editTextName.setText("");
            editTextEmailAddress.setText("");
        }
        editTextPassword.setText("");
        editTextPasswordConfirm.setText("");
    }

    /** Back button: function is not to save edited data to firebase **/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private HashMap<String, Object> makeAccount(String name, String emailAddress){
        HashMap<String, Object> res = new HashMap<>();
        HashMap<String, Object> calendar = new HashMap<>();

        res.put("name", name);
        res.put("email", emailAddress);
        res.put("bio", "");
        res.put("dob", "");
        res.put("exercise", "");
        res.put("gender", "");
        res.put("location", "");

        Day[] days = Day.values();
        int numOfDays = 7;
        for (int i = 0; i < numOfDays && i < days.length; i++) {
            int numOfHours = 24;
            calendar.put(days[i].toString(), new ArrayList<>(Collections.nCopies(numOfHours, 0)));
        }

        res.put("calendar", calendar);
        return res;
    }
}
