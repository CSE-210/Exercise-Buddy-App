package com.example.activeamigo.ui.preferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PreferencesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PreferencesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is preferences fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}