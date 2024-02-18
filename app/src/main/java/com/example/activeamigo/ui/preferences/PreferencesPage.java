package com.example.activeamigo.ui.preferences;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.activeamigo.PreferenceActivity;
import com.example.activeamigo.R;
import com.example.activeamigo.databinding.FragmentPreferencesBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PreferencesPage extends Fragment {

    private FragmentPreferencesBinding binding;
    private EditText dobEditText;
    private Button edit_save;
    private Spinner exercise_choice;
    private Spinner location_choice;
    private RadioGroup genderGroup;
    private EditText bio;
    private PreferencesViewModel preferencesViewModel;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        preferencesViewModel =
                new ViewModelProvider(this).get(PreferencesViewModel.class);

        binding = FragmentPreferencesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        // Disable all views except the Save button
        disableAllViewsExceptSaveButton((ViewGroup) root, edit_save);

        // Initialize the button
        edit_save = root.findViewById(R.id.save);
        edit_save.setText("Edit");

        exercise_choice = root.findViewById(R.id.exercise);
        location_choice = root.findViewById(R.id.location);
        genderGroup = root.findViewById(R.id.gender_radio_group);
        bio = root.findViewById(R.id.bio);
        dobEditText = root.findViewById(R.id.dob_edit_text);



        // Set OnClickListener for the button: Open preference activity to edit data
        edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), PreferenceActivity.class));
                Intent intent = new Intent(getActivity(), PreferenceActivity.class);
                preferenceActivityLauncher.launch(intent);
            }
        });


        updateDOB(preferencesViewModel);
        updateBio(preferencesViewModel);
        updateRadioButtonByValue(preferencesViewModel);
        updateExercise(preferencesViewModel);
        updateLocation(preferencesViewModel);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private final ActivityResultLauncher<Intent> preferenceActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Update UI with new values
                    preferencesViewModel.fetchDataFromFirebase();
                    updateDOB(preferencesViewModel);
                    updateBio(preferencesViewModel);
                    updateRadioButtonByValue(preferencesViewModel);
                    updateExercise(preferencesViewModel);
                    updateLocation(preferencesViewModel);
                }
            }
    );

    /** Disable Editing on Preference Page**/
    public void disableAllViewsExceptSaveButton(ViewGroup viewGroup, Button saveButton) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            // Set clickable attribute to false for all child views except the saveButton
            if (child != saveButton) {
                child.setClickable(false);
                child.setFocusable(false);
                // If the child is a ViewGroup, recursively call disableAllViewsExceptSaveButton
                if (child instanceof ViewGroup) {
                    disableAllViewsExceptSaveButton((ViewGroup) child, saveButton);
                }
            }
        }
    }

    private int getSpinnerIndex(Spinner spinner, String value) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        return adapter.getPosition(value);
    }

    private void updateDOB(PreferencesViewModel preferencesViewModel) {
        preferencesViewModel.getDateOfBirth().observe(getViewLifecycleOwner(), dob -> {
            // Update the UI with the new date of birth value
            Log.d("API","DOB  "+ dob);
            if (dob!=null){
                dobEditText.setText(dob);
            }
            else{
                dobEditText.setText("");
            }

        });
    }
    private void updateBio(PreferencesViewModel preferencesViewModel) {
        preferencesViewModel.getBio().observe(getViewLifecycleOwner(), bio_text -> {
            // Update the UI with the new date of birth value
            Log.d("API","BIO "+ bio_text);
            if (bio!=null){
                bio.setText(bio_text);
            }
            else{
                bio.setText("");
            }

        });
    }
    private void updateRadioButtonByValue(PreferencesViewModel preferencesViewModel) {
        preferencesViewModel.getGender().observe(getViewLifecycleOwner(), gender_text -> {
            // Update the UI with the new gender value
            Log.d("API", "Gender " + gender_text);
            if(gender_text!=null){
                int radioButtonId = -1;
                switch (gender_text) {
                    case "Male":
                        radioButtonId = R.id.male_button;
                        break;
                    case "Female":
                        radioButtonId = R.id.female_button;
                        break;
                    case "Other":
                        // No specific radio button for "Other", leave it unchecked
                        break;
                }

                if (radioButtonId != -1) {
                    RadioButton radioButton = genderGroup.findViewById(radioButtonId);
                    radioButton.setChecked(true);
                }
                else{
                    genderGroup.clearCheck();
                }
            }
            else{
                genderGroup.clearCheck();
            }
        });
    }
    private void updateExercise(PreferencesViewModel preferencesViewModel){
        preferencesViewModel.getExercise().observe(getViewLifecycleOwner(), selection -> {
            // Update Spinner selection based on observed value
            Log.d("API", "Exercise Choice: " + selection);
            updateSpinnerSelection(exercise_choice, selection);
        });
    }
    private void updateLocation(PreferencesViewModel preferencesViewModel){
        preferencesViewModel.getLocation().observe(getViewLifecycleOwner(), selection -> {
            // Update Spinner selection based on observed value
            Log.d("API", "Location Choice: " + selection);
            updateSpinnerSelection(location_choice, selection);
        });
    }

    private void updateSpinnerSelection(Spinner spinner, String selection) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.activity_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        if (selection != null) {
            int spinnerPosition = adapter.getPosition(selection);
            spinner.setSelection(spinnerPosition);
        }
    }


}