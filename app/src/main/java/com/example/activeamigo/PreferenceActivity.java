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
        // TODO: Save -> put into firebase table
        // TODO: Modify collections path based off user here*
        edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO:1. Check if all info is filled, then if conditional to the rest

                // 2. Save info to database
                DocumentReference docRef = db.collection("Emails").document("Preference");

                // Create a HashMap to store the data
                Map<String, Object> data = new HashMap<>();
                data.put("exercise",exercise_choice.getSelectedItem().toString());
                data.put("location", location_choice.getSelectedItem().toString());
                data.put("gender", gender);
                data.put("dob",dobEditText.getText().toString());
                data.put("bio",bio.getText().toString());

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
        });



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
}