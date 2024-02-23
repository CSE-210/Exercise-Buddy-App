package com.example.activeamigo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PreferenceActivity extends AppCompatActivity {

    private EditText dobEditText;
    private Button edit_save;
    private Spinner exercise_choice;
    private Spinner location_choice;
    private RadioGroup genderGroup;
    private String gender;
    private EditText bio;

    private String collection="Accounts";
    private String document = "test@ucsd.edu";



    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_preferences);
        edit_save = findViewById(R.id.save);
        edit_save.setText("Save");

        //View variables
        exercise_choice = findViewById(R.id.exercise);
        location_choice = findViewById(R.id.location);
        genderGroup = findViewById(R.id.gender_radio_group);
        bio = findViewById(R.id.bio);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // showing the back button in action bar

        dobEditText = findViewById(R.id.dob_edit_text);
        dobEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


        /** Modifying Drop Down Arrays **/
        //Go to strings.xml under activity_array to change/add drop down options
        Spinner exerciseSpinner = findViewById(R.id.exercise);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.activity_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(adapter);
        exerciseSpinner.setSelection(adapter.getPosition("Select Activity"));

        //Go to strings.xml under location_array to change/add drop down options
        Spinner locationSpinner = findViewById(R.id.location);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.location_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter2);
        locationSpinner.setSelection(adapter2.getPosition("Select Location"));

        // Gender Selection
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // on below line we are getting radio button from our group.
                RadioButton radioButton = findViewById(checkedId);

                gender = radioButton.getText().toString();

            }
        });

        /** Save Button Action **/
        edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO:1. Check if all info is filled, then if conditional to the rest
                if (checkFieldsCompleted()) {
                    // 2. Save info to database
                    DocumentReference docRef = db.collection(collection).document(document);

                    // Create a HashMap to store the data
                    Map<String, Object> data = new HashMap<>();
                    data.put("exercise", exercise_choice.getSelectedItem().toString());
                    data.put("location", location_choice.getSelectedItem().toString());
                    data.put("gender", gender);
                    data.put("dob", dobEditText.getText().toString());
                    data.put("bio", bio.getText().toString());

                    // Set the data to the document with document ID "LA"
                    docRef.set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(PreferenceActivity.this, "Preferences Saved!", Toast.LENGTH_LONG).show();

                                    Log.d("API", "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(PreferenceActivity.this, "Preferences Failed!", Toast.LENGTH_LONG).show();
                                    Log.w("API", "Error writing document", e);
                                }
                            });
                    //Exit
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }

        });

        // Populate database data into UI
        loadDataFromFirestore();
    }

    /** Calender pop up to select DOB **/
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update the EditText with the chosen date
                        String dateFormat = "MM/dd/yyyy";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
                        calendar.set(year, month, dayOfMonth);
                        String dob = simpleDateFormat.format(calendar.getTime());
                        dobEditText.setText(dob);
                        Log.d("SelectedDate", "Selected date: " + dob);
                    }
                },
                year, month, day);

        // Set the maximum date allowed to the current date
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }


    /** Back button: function is not to save edited data to firebase **/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /** Enable XML Views**/
    public void enableAllViews(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            // Enable clickable attribute
            child.setClickable(true);
            child.setFocusable(true);

            if (child instanceof ViewGroup) {
                enableAllViews((ViewGroup) child);
            }
        }
    }

    // Method to load data from Firestore
    private void loadDataFromFirestore() {
        db.collection(collection).document(document).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Get data from Firestore
                            String exercise = documentSnapshot.getString("exercise");
                            String location = documentSnapshot.getString("location");
                            String gender = documentSnapshot.getString("gender");
                            String dob = documentSnapshot.getString("dob");
                            String bioText = documentSnapshot.getString("bio");

                            // Update UI with fetched data
                            updateUI(exercise, location, gender, dob, bioText);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Toast.makeText(PreferenceActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                        Log.e("PreferenceActivity", "Error fetching data: " + e.getMessage());
                    }
                });
    }

    // Method to update UI with fetched data
    private void updateUI(String exercise, String location, String gender, String dob, String bioText) {
        // Update exercise spinner
        ArrayAdapter<CharSequence> exerciseAdapter = (ArrayAdapter<CharSequence>) exercise_choice.getAdapter();
        if (exerciseAdapter != null) {
            int exercisePosition = exerciseAdapter.getPosition(exercise);
            exercise_choice.setSelection(exercisePosition);
        }

        // Update location spinner
        ArrayAdapter<CharSequence> locationAdapter = (ArrayAdapter<CharSequence>) location_choice.getAdapter();
        if (locationAdapter != null) {
            int locationPosition = locationAdapter.getPosition(location);
            location_choice.setSelection(locationPosition);
        }

        // Update gender radio button
        if (gender != null) {
            int radioButtonId = -1;
            switch (gender) {
                case "Male":
                    radioButtonId = R.id.male_button;
                    break;
                case "Female":
                    radioButtonId = R.id.female_button;
                    break;
            }
            if (radioButtonId != -1) {
                genderGroup.check(radioButtonId);
            }
        }

        // Update date of birth edit text
        if (dobEditText != null) {
            dobEditText.setText(dob);
        }

        // Update bio edit text
        if (bio != null) {
            bio.setText(bioText);
        }
    }

    private boolean checkFieldsCompleted(){
        if (exercise_choice.getSelectedItemPosition() == 0 || location_choice.getSelectedItemPosition() == 0) {
            // Spinner is at index 0 (first item) indicating "Select Activity" or "Select Location" is still selected
            Toast.makeText(this, "Please select valid options for activity and location", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (gender == null) {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (dobEditText.getText().toString().isEmpty()){
            Toast.makeText(this, "Please fill Date of birth field", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (bio.getText().toString().isEmpty() || bio.getText().toString().equals("Bio")){
                Toast.makeText(this, "Please fill bio fields", Toast.LENGTH_SHORT).show();
                return false;
        }
        else if (gender == null) {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }

    }



}