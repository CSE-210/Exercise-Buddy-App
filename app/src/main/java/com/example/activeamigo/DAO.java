package com.example.activeamigo;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Objects;

public interface DAO{
    // Grabs the account from the dataBase
    default Task<DocumentSnapshot> checkAccount(String emailAddress, String dbName, FirebaseFirestore fs) {
        final TaskCompletionSource<DocumentSnapshot> tcs = new TaskCompletionSource<>();
        fs.collection(dbName)
                .whereEqualTo("email", emailAddress)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            tcs.setResult(document);
                        }
                        else {
                            tcs.setResult(null);
                        }

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

    /** Check if the email already exists in the database **/
    default Task<DocumentSnapshot> checkEmail(String emailAddress, FirebaseFirestore fs) {
        final TaskCompletionSource<DocumentSnapshot> tcs  = new TaskCompletionSource<>();
        fs.collection("Accounts")
                .whereEqualTo("email", emailAddress)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            tcs.setResult(querySnapshot.getDocuments().get(0));
                        }
                        else{
                            tcs.setResult(null);
                        }
                    }
                });
        return tcs.getTask();
    }

}

