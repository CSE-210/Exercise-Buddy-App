package com.example.activeamigo;

import com.google.firebase.firestore.FirebaseFirestore;

/** To interact with the database, create an instance of this class **/
public class DAO {
    // Access a Cloud Firestore instance from your Activity
    static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addAccount(){};

    public void addToCalendar(){};
}