package com.example.activeamigo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ForgotPassword extends AppCompatActivity implements Alertable{
   private FirebaseAuth auth = null;
   private FirebaseFirestore db = null;
   private EditText emailText = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        emailText = findViewById(R.id.editTextEmailAddressFP);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // showing the back button in action bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.buttonResetPassword).setOnClickListener(task->{
            String email = emailText.getText().toString();
            checkEmail(email, this.db).addOnCompleteListener(task2 ->{
                 if(task2.isSuccessful()){
                     DocumentSnapshot ds = task2.getResult();

                     if(ds != null){
                         auth.sendPasswordResetEmail(email);
                         emailText.setText("");
                         showAlert(this, R.string.resetPasswordSuccess);
                     }
                     else{
                         showAlert(this, R.string.notAccountFound);
                     }
                 }
            });

        });

    }

    /** Check if the email already exists in the database **/
    protected Task<DocumentSnapshot> checkEmail(String emailAddress, FirebaseFirestore fs) {
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
                    else {
                        // Error occurred while checking for email existence
                        showAlert(this, R.string.queryError);
                    }
                });
        return tcs.getTask();
    }

    /** Back button: function is not to save edited data to firebase **/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}