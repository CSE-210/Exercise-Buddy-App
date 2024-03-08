package com.example.activeamigo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class CreateAccountActivity extends AppCompatActivity implements Alertable, DAO {
    private enum Day {mon, tue, wed, thu, fri, sat, sun}
    protected FirebaseFirestore db;
    private FirebaseAuth auth;
    static final String desiredDomain = "ucsd.edu";
    private static final int REQUEST_CODE_PREFERENCE_ACTIVITY = 1;
    private static final int REQUEST_CODE_CALENDAR_ACTIVITY = 2;

    EditText editTextName = null;
    EditText editTextEmailAddress = null;
    EditText editTextPassword = null;
    EditText editTextPasswordConfirm = null;

    private String pref_location_check="";
    private String pref_gender_check=null;
    private String pref_excercise_check="";


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

                checkAccount(emailAddress, fSName, this.db).addOnCompleteListener(task -> {
                   if(task.isSuccessful()) {
                       DocumentSnapshot ds = task.getResult();
                       if (ds != null && ds.exists()) {
                           // Checks email
                           showAlert(this, R.string.accountCreationEmailExists);
                       }
                       else {
                           // Sets up authentication account
                        auth.createUserWithEmailAndPassword(emailAddress, password).addOnSuccessListener(task1-> {
                            //Makes the account
                            HashMap<String, Object> account = makeAccount(name, emailAddress);
                            // Adds account to database
                            addAccount(account, this.db, "Accounts");
                            Intent intent = new Intent(this, PreferenceActivity.class);
                            intent.putExtra("firstTimeUser", true);
                            intent.putExtra("email",emailAddress);
                            preferenceActivityResultLauncher.launch(intent);


                        }).addOnFailureListener(task2-> showAlert(this, R.string.accountCreationFailed));
                        }
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
            clearForm();
        }
        // If the passwords do not match
        else if (password.length() < 6) {
            showAlert(this, R.string.createAccountShortPassword);
            clearForm();
        } else if (password.contains(" ")) {
            showAlert(this, R.string.passwordWithWhiteSpace);
            clearForm();
        } else return true;

        return false;
    }

    /** Used to clear the text inputs
     If true will only clear the passwords **/
    private void clearForm() {
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

    protected HashMap<String, Object> makeAccount(String name, String emailAddress){
        HashMap<String, Object> res = new HashMap<>();
        HashMap<String, ArrayList<Integer>> calendar = new HashMap<>();

        res.put("name", name);
        res.put("email", emailAddress.toLowerCase());
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

    private CompletableFuture<Boolean> checkPreferenceInfo(String dbName, String emailAddress) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        DocumentReference docRef = db.collection(dbName).document(emailAddress);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()) {
                    // If document exists, fetch data
                    String exerciseText = documentSnapshot.getString("exercise");
                    String locationText = documentSnapshot.getString("location");

                    pref_excercise_check = exerciseText;
                    pref_location_check = locationText;
                    Log.d("API", exerciseText + "     " + locationText + "     --createacc");

                    // Check if all required preferences are present
                    boolean preferencesValid = exerciseText != null && locationText != null;
                    future.complete(preferencesValid);
                } else {
                    // Document doesn't exist, preferences not present
                    future.complete(false);
                }
            } else {
                // Failed to fetch data
                Log.d("API", "Failed to fetch data from firebase -- Preference View Model", task.getException());
                future.complete(false);
            }
        });

        return future;
    }
    private ActivityResultLauncher<Intent> preferenceActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            // PreferenceActivity finished successfully, start CalendarActivity
                            Intent intent = new Intent(this, CalendarActivity.class);
                            startActivity(intent);
                        }
//                        else {
////                            showAlert(this, R.string.preferenceCheckFailed);
//                        }
                    });
}
