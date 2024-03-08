package com.example.activeamigo;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Objects;

public interface DAO{
    // Grabs the account from the dataBase
    default Task<DocumentSnapshot> checkAccount(String emailAddress, String dbName, FirebaseFirestore fs) {
        final TaskCompletionSource<DocumentSnapshot> tcs = new TaskCompletionSource<>();

        String lowercaseEmail = emailAddress.toLowerCase(); // Convert provided email to lowercase for comparison

        fs.collection(dbName)
                .document(lowercaseEmail) // Use document ID (email) directly
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        tcs.setResult(document);
                    } else {
                        tcs.setException(Objects.requireNonNull(task.getException()));
                    }
                });

        return tcs.getTask();
    }




    // Adds account to database
    default void addAccount(HashMap<String, Object> account, FirebaseFirestore db, String dbName) {
        // Add the account data to db
        db.collection(dbName)
                .document((String) Objects.requireNonNull(account.get("email")))
                .set(account, SetOptions.merge());

    }

}

