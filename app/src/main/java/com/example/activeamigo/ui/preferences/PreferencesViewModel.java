package com.example.activeamigo.ui.preferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PreferencesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private MutableLiveData<String> dateOfBirth = new MutableLiveData<>();

    public PreferencesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("My Account");

    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getDateOfBirth() {
        return dateOfBirth;

    }
    public void setDateOfBirth(String dob) {
        dateOfBirth.setValue(dob);
    }
}