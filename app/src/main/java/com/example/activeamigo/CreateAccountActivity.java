package com.example.activeamigo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CreateAccountActivity extends AppCompatActivity implements Alertable {
    private enum Day {Mon, Tue, Wed, Thu, Fri, Sat, Sun}
    protected FirebaseFirestore db;

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

        // Find the EditText views by their IDs
        editTextName = findViewById(R.id.editTextNameAC);
        editTextEmailAddress = findViewById(R.id.editTextEmailAddressAC);
        editTextPassword = findViewById(R.id.editTextPasswordAC);
        editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirmAC);

        // Setting up action bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        // When the button is clicked
        findViewById(R.id.buttonCreateAccount).setOnClickListener(view -> {

            // Get the text from the EditText fields
            String name = editTextName.getText().toString();
            String emailAddress = editTextEmailAddress.getText().toString();
            String password = editTextPassword.getText().toString();
            String passwordConfirm = editTextPasswordConfirm.getText().toString();

            // Call validateInformation function before adding account to database
            if (validateInformation(name, emailAddress, password, passwordConfirm)) {
                checkEmail(name, emailAddress, password, fSName, this.db);
            }
        });
    }

    // Makes sure the information in the from is filled in, a good email, matching passwords
    private boolean validateInformation(String name, String emailAddress, String password, String passwordConfirm) {
        int domainPos = emailAddress.indexOf("@");
        String domain = emailAddress.substring(domainPos + 1);

        // If a section is left empty
        if (name.isEmpty() || emailAddress.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            showAlert(this, R.string.createAccountErrorFilledIn);
        }
        // Bad email or non ucsd email
        else if (domainPos < 1 || !domain.equals(getString(R.string.ucsdDomain))) {
            showAlert(this, R.string.createAccountErrorBadEmail);
        }
        // If the passwords do not match
        else if (!password.equals(passwordConfirm)) {
            showAlert(this, R.string.createAccountErrorMismatchPassword);
            clearForm(true);
        }
        else return true;

        return false;
    }

    // Check if the email already exists in the database
    protected void checkEmail(String name, String emailAddress, String password, String dbName, FirebaseFirestore fs) {
        fs.collection(dbName)
                .whereEqualTo("email", emailAddress)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Email already exists, show error message
                            showAlert(this, R.string.accountCreationEmailExists);
                        } else {
                            // Email does not exist, proceed with adding the account
                            addAccount(name, emailAddress, password, dbName);
                        }
                    } else {
                        // Error occurred while checking for email existence
                        showAlert(this, R.string.queryError);
                    }
                });
    }

    // Adds account to database
    protected void addAccount(String name, String emailAddress, String password, String dbName) {
        HashMap<String, Object> accountData = makeAccount(name, emailAddress, password);


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

    private HashMap<String, Object> makeAccount(String name, String emailAddress, String password){
        HashMap<String, Object> res = new HashMap<>();
        HashMap<String, Object> calendar = new HashMap<>();

        res.put("name", name);
        res.put("email", emailAddress);
        res.put("password", password);
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
