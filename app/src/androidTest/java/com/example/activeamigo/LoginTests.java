package com.example.activeamigo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class LoginTests {


    @Test
    public void loginTest(){
        String email = System.currentTimeMillis() +"@ucsd.edu";
        String password = String.valueOf(System.currentTimeMillis());

        // Check if user is already logged in
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // Log out the current user
            FirebaseAuth.getInstance().signOut();
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            // Start the activity
            ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class);

            //Checks if current activity is LoginActivity
            scenario.onActivity(Assert::assertNotNull);

            // Enter login information
            onView(withId(R.id.editTextpasswordLG)).perform(replaceText(email));
            onView(withId(R.id.editTextpasswordLG)).perform(replaceText(password));

            // Click on login
            onView(withId(R.id.buttonLogIn)).perform(click());

            //Check if current activity is MainActivity
            ActivityScenario<MainActivity> mainScenario = ActivityScenario.launch(MainActivity.class);
            mainScenario.onActivity(Assert::assertNotNull);

            assertNotNull(FirebaseAuth.getInstance().getCurrentUser());

            // Used to delete user
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user != null){
                user.delete().addOnCompleteListener(task2->{
                    if(task2.isSuccessful()){
                        Log.d("TEST", "SUCCESSFUL DELETE");
                    }
                    else{
                        Log.d("TEST", "UNSUCCESSFUL DELETE");
                    }
                });
            }
        });
    }

    @Test
    public void logOutTest(){
        String email = System.currentTimeMillis() +"@ucsd.edu";
        String password = String.valueOf(System.currentTimeMillis());

        // Check if user is already logged in
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // Log out the current user
            FirebaseAuth.getInstance().signOut();
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            // Start the activity
            ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class);

            //Checks if current activity is LoginActivity
            scenario.onActivity(Assert::assertNotNull);

            // Enter login information
            onView(withId(R.id.editTextpasswordLG)).perform(replaceText(email));
            onView(withId(R.id.editTextpasswordLG)).perform(replaceText(password));

            // Click on login
            onView(withId(R.id.buttonLogIn)).perform(click());

            //Check if current activity is MainActivity
            ActivityScenario<MainActivity> mainScenario = ActivityScenario.launch(MainActivity.class);
            mainScenario.onActivity(Assert::assertNotNull);

            //Users exists
            assertNotNull(FirebaseAuth.getInstance().getCurrentUser());

            // Sign Out
            FirebaseAuth.getInstance().signOut();
            assertNull(FirebaseAuth.getInstance().getCurrentUser());

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(task1 ->{
                // Used to delete user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    user.delete().addOnCompleteListener(task2->{
                        if(task2.isSuccessful()){
                            Log.d("TEST", "SUCCESSFUL DELETE");
                        }
                        else{
                            Log.d("TEST", "UNSUCCESSFUL DELETE");
                        }
                    });
                }
            });

        });
    }
}
