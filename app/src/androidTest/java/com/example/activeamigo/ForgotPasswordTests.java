package com.example.activeamigo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ForgotPasswordTests {


    @Test
    public void cantFindEmail(){
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            FirebaseAuth.getInstance().signOut();
        }

        // Start Activity
        ActivityScenario<ForgotPasswordActivity> scenario = ActivityScenario.launch(ForgotPasswordActivity.class);
        // Fill in false email
        onView(withId(R.id.editTextEmailAddressFP)).perform(replaceText("FooBar"));
        // Click send button
        onView(withId(R.id.buttonResetPassword)).perform(click());

        // Check if the dialog pops up with the correct message
        onView(withText(R.string.notAccountFound))
                // checks if dialog is popped up
                .inRoot(isDialog())
                // Checks the dialog text
                .check(matches(withText(R.string.notAccountFound)));

        // ends tests
        scenario.close();

    }

    @Test
    public void foundEmail(){
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            FirebaseAuth.getInstance().signOut();
        }

        // Start Activity
        ActivityScenario<ForgotPasswordActivity> scenario = ActivityScenario.launch(ForgotPasswordActivity.class);
        // Fill in false email
        onView(withId(R.id.editTextEmailAddressFP)).perform(replaceText("mj@ucsd.edu"));
        // Click send button
        onView(withId(R.id.buttonResetPassword)).perform(click());

        // Check if the dialog pops up with the correct message
        onView(withText(R.string.resetPasswordSuccess))
                // checks if dialog is popped up
                .inRoot(isDialog())
                // Checks the dialog text
                .check(matches(withText(R.string.resetPasswordSuccess)));

        // ends tests
        scenario.close();

    }
}
