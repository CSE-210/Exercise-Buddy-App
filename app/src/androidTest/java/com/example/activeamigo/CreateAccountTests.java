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

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CreateAccountTests {

    @Test
    public void testDialogShowsOnEmptyFieldsHasNameEmail() {
        // Creates activity to test
        ActivityScenario<CreateAccountActivity> scenario = ActivityScenario.launch(CreateAccountActivity.class);

        //Enters name and email into input fields of ID
        onView(withId(R.id.editTextNameAC)).perform(replaceText("Foo Bar"));
        onView(withId(R.id.editTextEmailAddressAC)).perform(replaceText("fbar@ucsd.edu"));

        // Click on the create account button without filling out any fields
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Check if the dialog pops up with the correct message
        onView(withText(R.string.createAccountErrorFilledIn))
                // checks if dialog is popped up
                .inRoot(isDialog())
                // Checks the dialog text
                .check(matches(withText(R.string.createAccountErrorFilledIn)));

        // ends tests
        scenario.close();
    }

    @Test
    public void testDialogShowsOnEmptyFieldsAll() {
        // Creates a current copy of the activity to test
        ActivityScenario<CreateAccountActivity> scenario = ActivityScenario.launch(CreateAccountActivity.class);

        // Click on the create account button without filling out any fields
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Check if the dialog pops up with the correct message
        onView(withText(R.string.createAccountErrorFilledIn))
                .inRoot(isDialog())
                .check(matches(withText(R.string.createAccountErrorFilledIn)));

        scenario.close();
    }

    @Test
    public void testDialogShowsOnEmptyFieldsHasName() {
        ActivityScenario<CreateAccountActivity> scenario = ActivityScenario.launch(CreateAccountActivity.class);
        // Enters Name
        onView(withId(R.id.editTextNameAC)).perform(replaceText("Foo Bar"));

        // Click on the create account button without filling out any fields
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Check if the dialog pops up with the correct message
        onView(withText(R.string.createAccountErrorFilledIn))
                .inRoot(isDialog())
                .check(matches(withText(R.string.createAccountErrorFilledIn)));

        scenario.close();
    }

    @Test
    public void testDialogShowsOnEmptyFieldsHasEmail() {
        ActivityScenario<CreateAccountActivity> scenario = ActivityScenario.launch(CreateAccountActivity.class);
        // Enters email
        onView(withId(R.id.editTextEmailAddressAC)).perform(replaceText("Foo Bar"));

        // Click on the create account button without filling out any fields
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Check if the dialog pops up with the correct message
        onView(withText(R.string.createAccountErrorFilledIn))
                .inRoot(isDialog())
                .check(matches(withText(R.string.createAccountErrorFilledIn)));

        scenario.close();
    }

    @Test
    public void testDialogShowsOnEmptyFieldsHasPassword1() {
        ActivityScenario<CreateAccountActivity> scenario = ActivityScenario.launch(CreateAccountActivity.class);
        // Enters first password
        onView(withId(R.id.editTextPasswordAC)).perform(replaceText("Foo Bar"));

        // Click on the create account button without filling out any fields
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Check if the dialog pops up with the correct message
        onView(withText(R.string.createAccountErrorFilledIn))
                .inRoot(isDialog())
                .check(matches(withText(R.string.createAccountErrorFilledIn)));

        scenario.close();
    }

    @Test
    public void testDialogShowsOnEmptyFieldsHasPassword2() {
        ActivityScenario<CreateAccountActivity> scenario = ActivityScenario.launch(CreateAccountActivity.class);
        //Enters second password
        onView(withId(R.id.editTextPasswordConfirmAC)).perform(replaceText("Foo Bar"));

        // Click on the create account button without filling out any fields
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Check if the dialog pops up with the correct message
        onView(withText(R.string.createAccountErrorFilledIn))
                .inRoot(isDialog())
                .check(matches(withText(R.string.createAccountErrorFilledIn)));

        scenario.close();
    }

    @Test
    public void testDialogShowsOnEmptyFieldsHasNamePassword1() {
        ActivityScenario<CreateAccountActivity> scenario = ActivityScenario.launch(CreateAccountActivity.class);
        //Enters name and password1
        onView(withId(R.id.editTextNameAC)).perform(replaceText("Foo Bar"));
        onView(withId(R.id.editTextPasswordAC)).perform(replaceText("password"));

        // Click on the create account button without filling out any fields
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Check if the dialog pops up with the correct message
        onView(withText(R.string.createAccountErrorFilledIn))
                .inRoot(isDialog())
                .check(matches(withText(R.string.createAccountErrorFilledIn)));

        scenario.close();
    }

    @Test
    public void testDialogShowsOnEmptyFieldsHasNamePassword2() {
        ActivityScenario<CreateAccountActivity> scenario = ActivityScenario.launch(CreateAccountActivity.class);
        //Enters name and password2
        onView(withId(R.id.editTextNameAC)).perform(replaceText("Foo Bar"));
        onView(withId(R.id.editTextPasswordConfirmAC)).perform(replaceText("password"));

        // Click on the create account button without filling out any fields
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Check if the dialog pops up with the correct message
        onView(withText(R.string.createAccountErrorFilledIn))
                .inRoot(isDialog())
                .check(matches(withText(R.string.createAccountErrorFilledIn)));

        scenario.close();
    }

    @Test
    public void testDialogShowsOnEmptyFieldsHasEmailPassword1() {
        ActivityScenario<CreateAccountActivity> scenario = ActivityScenario.launch(CreateAccountActivity.class);
        //Enters email and password
        onView(withId(R.id.editTextEmailAddressAC)).perform(replaceText("fbar@ucsd.edu"));
        onView(withId(R.id.editTextPasswordAC)).perform(replaceText("password"));

        // Click on the create account button without filling out any fields
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Check if the dialog pops up with the correct message
        onView(withText(R.string.createAccountErrorFilledIn))
                .inRoot(isDialog())
                .check(matches(withText(R.string.createAccountErrorFilledIn)));

        scenario.close();
    }

    @Test
    public void testDialogShowsOnEmptyFieldsHasEmailPassword2() {
        ActivityScenario<CreateAccountActivity> scenario = ActivityScenario.launch(CreateAccountActivity.class);
        //Enters email and password2
        onView(withId(R.id.editTextEmailAddressAC)).perform(replaceText("fbar@ucsd.edu"));
        onView(withId(R.id.editTextPasswordConfirmAC)).perform(replaceText("password"));

        // Click on the create account button without filling out any fields
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Check if the dialog pops up with the correct message
        onView(withText(R.string.createAccountErrorFilledIn))
                .inRoot(isDialog())
                .check(matches(withText(R.string.createAccountErrorFilledIn)));

        scenario.close();
    }

    @Test
    public void testDialogShowsOnEmptyFieldsHasPasswords() {
        ActivityScenario<CreateAccountActivity> scenario = ActivityScenario.launch(CreateAccountActivity.class);
        //Enters passwords
        onView(withId(R.id.editTextPasswordAC)).perform(replaceText("Foo Bar"));
        onView(withId(R.id.editTextPasswordConfirmAC)).perform(replaceText("Foo Bar"));

        // Click on the create account button without filling out any fields
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Check if the dialog pops up with the correct message
        onView(withText(R.string.createAccountErrorFilledIn))
                .inRoot(isDialog())
                .check(matches(withText(R.string.createAccountErrorFilledIn)));

        scenario.close();
    }

    @Test
    public void testDialogShowsOnEmptyFieldsHasNoPassword2() {
        ActivityScenario<CreateAccountActivity> scenario = ActivityScenario.launch(CreateAccountActivity.class);
        //Enters name email, password1
        onView(withId(R.id.editTextNameAC)).perform(replaceText("Foo Bar"));
        onView(withId(R.id.editTextEmailAddressAC)).perform(replaceText("fbar@ucsd.edu"));
        onView(withId(R.id.editTextPasswordAC)).perform(replaceText("password"));

        // Click on the create account button without filling out any fields
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Check if the dialog pops up with the correct message
        onView(withText(R.string.createAccountErrorFilledIn))
                .inRoot(isDialog())
                .check(matches(withText(R.string.createAccountErrorFilledIn)));

        scenario.close();
    }

    @Test
    public void testDialogShowsOnEmptyFieldsHasNoPassword1() {
        ActivityScenario<CreateAccountActivity> scenario = ActivityScenario.launch(CreateAccountActivity.class);
        //Enters name email, password2
        onView(withId(R.id.editTextNameAC)).perform(replaceText("Foo Bar"));
        onView(withId(R.id.editTextEmailAddressAC)).perform(replaceText("fbar@ucsd.edu"));
        onView(withId(R.id.editTextPasswordConfirmAC)).perform(replaceText("password"));

        // Click on the create account button without filling out any fields
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Check if the dialog pops up with the correct message
        onView(withText(R.string.createAccountErrorFilledIn))
                .inRoot(isDialog())
                .check(matches(withText(R.string.createAccountErrorFilledIn)));

        scenario.close();
    }

    @Test
    public void testDialogShowsOnEmptyFieldsHasNoEmail() {
        ActivityScenario<CreateAccountActivity> scenario = ActivityScenario.launch(CreateAccountActivity.class);
        //Enters name email, password2
        onView(withId(R.id.editTextNameAC)).perform(replaceText("Foo Bar"));
        onView(withId(R.id.editTextPasswordAC)).perform(replaceText("password"));
        onView(withId(R.id.editTextPasswordConfirmAC)).perform(replaceText("password"));

        // Click on the create account button without filling out any fields
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Check if the dialog pops up with the correct message
        onView(withText(R.string.createAccountErrorFilledIn))
                .inRoot(isDialog())
                .check(matches(withText(R.string.createAccountErrorFilledIn)));

        scenario.close();
    }

    @Test
    public void testDialogShowsOnEmptyFieldsHasNoName() {
        ActivityScenario<CreateAccountActivity> scenario = ActivityScenario.launch(CreateAccountActivity.class);
        //Enters name email, password2
        onView(withId(R.id.editTextEmailAddressAC)).perform(replaceText("fbar@ucsd.edu"));
        onView(withId(R.id.editTextPasswordAC)).perform(replaceText("password"));
        onView(withId(R.id.editTextPasswordConfirmAC)).perform(replaceText("password"));

        // Click on the create account button without filling out any fields
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Check if the dialog pops up with the correct message
        onView(withText(R.string.createAccountErrorFilledIn))
                .inRoot(isDialog())
                .check(matches(withText(R.string.createAccountErrorFilledIn)));

        scenario.close();
    }

    @Test
    public void testDialogShowsOnNotAnEmail() {
        ActivityScenario<CreateAccountActivity> scenario = ActivityScenario.launch(CreateAccountActivity.class);
        onView(withId(R.id.editTextNameAC)).perform(replaceText("Foo Bar"));

        // Fill out the email field with an invalid email
        onView(withId(R.id.editTextNameAC)).perform(replaceText("name"));
        onView(withId(R.id.editTextPasswordAC)).perform(replaceText("password"));
        onView(withId(R.id.editTextPasswordConfirmAC)).perform(replaceText("password"));
        onView(withId(R.id.editTextEmailAddressAC)).perform(replaceText("vfvnfn3nnvn"));

        // Click on the create account button
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Check if the dialog pops up with the correct message
        onView(withText(R.string.createAccountErrorBadEmail))
                .inRoot(isDialog())
                .check(matches(withText(R.string.createAccountErrorBadEmail)));

        scenario.close();
    }

    @Test
    public void testDialogShowsOnWrongEmail() {
        ActivityScenario<CreateAccountActivity> scenario = ActivityScenario.launch(CreateAccountActivity.class);
        onView(withId(R.id.editTextNameAC)).perform(replaceText("Foo Bar"));

        // Fill out the email field with an invalid email
        onView(withId(R.id.editTextNameAC)).perform(replaceText("name"));
        onView(withId(R.id.editTextPasswordAC)).perform(replaceText("password"));
        onView(withId(R.id.editTextPasswordConfirmAC)).perform(replaceText("password"));
        onView(withId(R.id.editTextEmailAddressAC)).perform(replaceText("foobar@gmail.com"));

        // Click on the create account button
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Check if the dialog pops up with the correct message
        onView(withText(R.string.createAccountErrorBadEmail))
                .inRoot(isDialog())
                .check(matches(withText(R.string.createAccountErrorBadEmail)));

        scenario.close();
    }

    @Test
    public void testDialogShowsOnAtSign() {
        ActivityScenario<CreateAccountActivity> scenario = ActivityScenario.launch(CreateAccountActivity.class);
        onView(withId(R.id.editTextNameAC)).perform(replaceText("Foo Bar"));

        // Fill out the email field with an invalid email
        onView(withId(R.id.editTextNameAC)).perform(replaceText("name"));
        onView(withId(R.id.editTextPasswordAC)).perform(replaceText("password"));
        onView(withId(R.id.editTextPasswordConfirmAC)).perform(replaceText("password"));
        onView(withId(R.id.editTextEmailAddressAC)).perform(replaceText("@ucsd.edu"));

        // Click on the create account button
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Check if the dialog pops up with the correct message
        onView(withText(R.string.createAccountErrorBadEmail))
                .inRoot(isDialog())
                .check(matches(withText(R.string.createAccountErrorBadEmail)));

        scenario.close();
    }

    @Test
    public void testDialogShowsOnPasswordMismatch() {
        ActivityScenario<CreateAccountActivity> scenario = ActivityScenario.launch(CreateAccountActivity.class);

        // Fill out the password fields with mismatched passwords
        onView(withId(R.id.editTextNameAC)).perform(replaceText("name"));
        onView(withId(R.id.editTextEmailAddressAC)).perform(replaceText("email@ucsd.edu"));
        onView(withId(R.id.editTextPasswordAC)).perform(replaceText("password1"));
        onView(withId(R.id.editTextPasswordConfirmAC)).perform(replaceText("password2"));

        // Click on the create account button
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Check if the dialog pops up with the correct message
        onView(withText(R.string.createAccountErrorMismatchPassword))
                .inRoot(isDialog())
                .check(matches(withText(R.string.createAccountErrorMismatchPassword)));

        scenario.close();
    }

    @Test
    public void testClearFormOnPasswordMismatch() {
        ActivityScenario<CreateAccountActivity> scenario = ActivityScenario.launch(CreateAccountActivity.class);

        // Fill out the password fields with mismatched passwords
        onView(withId(R.id.editTextNameAC)).perform(replaceText("name"));
        onView(withId(R.id.editTextEmailAddressAC)).perform(replaceText("email@ucsd.edu"));
        onView(withId(R.id.editTextPasswordAC)).perform(replaceText("password1"));
        onView(withId(R.id.editTextPasswordConfirmAC)).perform(replaceText("password2"));

        // Click on the create account button
        onView(withId(R.id.buttonCreateAccount)).perform(click());

        // Check if the dialog pops up with the correct message
        onView(withText(R.string.createAccountErrorMismatchPassword))
                .inRoot(isDialog())
                .check(matches(withText(R.string.createAccountErrorMismatchPassword)));
        onView(withText("OK")).inRoot(isDialog()).perform(click());

        // Check if the form is cleared after mismatching passwords are entered
        onView(withId(R.id.editTextNameAC)).check(matches(withText("name")));
        onView(withId(R.id.editTextEmailAddressAC)).check(matches(withText("email@ucsd.edu")));
        onView(withId(R.id.editTextPasswordAC)).check(matches(withText("")));
        onView(withId(R.id.editTextPasswordConfirmAC)).check(matches(withText("")));

        scenario.close();
    }

}
