package com.example.activeamigo.ui.matches;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

public class MatchesViewModel extends ViewModel {

    private MutableLiveData<ArrayList<String>> myListLiveData;
    public MutableLiveData<ArrayList<String>> getMyListLiveData() {
        if (myListLiveData == null) {
            myListLiveData = new MutableLiveData<>();
        }
        return myListLiveData;
    }

    // Method to update the list held by the MutableLiveData
    public void setList(ArrayList<String> newList) {
        myListLiveData.setValue(newList);
    }
}