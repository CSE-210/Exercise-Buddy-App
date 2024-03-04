package com.example.activeamigo.ui.preferences;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PreferencesViewModel extends ViewModel {

    private MutableLiveData<String> dateOfBirth;
    private MutableLiveData<String> bio;
    private MutableLiveData<String> gender = new MutableLiveData<>();
    private MutableLiveData<String> exercise = new MutableLiveData<>();
    private MutableLiveData<String> location = new MutableLiveData<>();

    private MutableLiveData<String> firebaseData = new MutableLiveData<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected FirebaseAuth auth = FirebaseAuth.getInstance();
    private String db_collection="Accounts";
    private String db_email;

    public PreferencesViewModel() {
        dateOfBirth = new MutableLiveData<>();
        bio = new MutableLiveData<>();


    }

    public void setUserEmail() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            // User is signed in
            Log.d("PreferenceViewModel", "user is signed in");
            db_email = user.getEmail();
        } else {
            // No user is signed in
            Log.d("PreferenceViewModel", "no user is signed in");
            db_email = "cn@ucsd.edu"; // for testing purposes
        }
    }


    public LiveData<String> getDateOfBirth() {

        return dateOfBirth;
    }
    public LiveData<String> getBio() {

        return bio;
    }
    public LiveData<String> getGender() {

        return gender;
    }
    public LiveData<String> getExercise() {
        return exercise;
    }
    public LiveData<String> getLocation() {
        return location;
    }

    public LiveData<String> getFirebaseData() {
        return firebaseData;
    }

    public void setDateOfBirth(String dob) {

        dateOfBirth.setValue(dob);
    }
    public void setBio(String bio_text) {

        bio.setValue(bio_text);
    }
    public void setGender(String g) {

        gender.setValue(g);
    }
    public void setExercise(String ex) {
        exercise.setValue(ex);
    }
    public void setLocation(String loc) {
        location.setValue(loc);
    }

    /**TODO: Modify collections path based off user here**/
    public void fetchDataFromFirebase() {
        // Fetch data from Firebase
        setUserEmail();
        DocumentReference docRef = db.collection(db_collection).document(db_email);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // If document exists, fetch data
                String exerciseText = documentSnapshot.getString("exercise");
                String locationText = documentSnapshot.getString("location");
                String genderText = documentSnapshot.getString("gender");
                String dobText = documentSnapshot.getString("dob");
                String bioText = documentSnapshot.getString("bio");

                Log.d("API", "ViewModel" + bioText+ " " + dobText + " " + genderText + " " + locationText + " " + exerciseText);

                // Update LiveData with fetched data
                setDateOfBirth(dobText);
                setBio(bioText);
                setGender(genderText);
                setExercise(exerciseText);
                setLocation(locationText);

            }
        }).addOnFailureListener(e -> {
            Log.d("API", "Failed to fetch data from firebase -- Preference View Model");
        });
    }

}