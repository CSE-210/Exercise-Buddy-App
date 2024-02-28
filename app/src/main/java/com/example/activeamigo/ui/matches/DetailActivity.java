package com.example.activeamigo.ui.matches;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.activeamigo.R;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matches_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); //
        // Retrieve the data
        String selectedItem = getIntent().getStringExtra("SELECTED_ITEM");
        String bio = getIntent().getStringExtra("BIO_KEY");
        String exercise = getIntent().getStringExtra("EXERCISE_KEY");
        String location = getIntent().getStringExtra("LOCATION_KEY");
        String email = getIntent().getStringExtra("EMAIL_KEY");

        // Use the data to update UI, for example, setting a TextView's text
        TextView detailTextView = findViewById(R.id.matchersName);
        TextView bioTextView = findViewById(R.id.matchersBio); // Assuming you have this TextView for bio
        TextView exerciseTextView = findViewById(R.id.matchersExercise); // And so for exercise
        TextView locationTextView = findViewById(R.id.matchersLocation); // And so for location
        TextView emailTextView = findViewById(R.id.matchersEmail); // And so for email

        detailTextView.setText(selectedItem);
        bioTextView.setText(bio);
        exerciseTextView.setText(exercise);
        locationTextView.setText(location);
        emailTextView.setText(email);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}