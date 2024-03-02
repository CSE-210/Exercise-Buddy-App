package com.example.activeamigo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ForgotPasswordActivity extends AppCompatActivity implements Alertable, DAO{
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
                         emailText.setText("");
                     }
                 }
            });

        });

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