package com.example.activeamigo;

import static android.app.PendingIntent.getActivity;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.base.CharMatcher.is;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static java.util.function.Predicate.not;

import android.view.Gravity;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.activeamigo.ui.preferences.PreferencesPage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PrefernceUITest {
    @Before
    public void setUp() {
    }

    @Test
    public void testSuccessfulPreferencePageScenario() {
        ActivityScenario<PreferenceActivity> scenario = ActivityScenario.launch(PreferenceActivity.class);

        // Filling in the exercise spinner
        onView(withId(R.id.exercise)).perform(click());
        onView(withText("Gym")).perform(click());

        // Filling in the location spinner
        onView(withId(R.id.location)).perform(click());
        onView(withText("Rimac")).perform(click());

        // Selecting gender as Male
        onView(withId(R.id.male_button)).perform(click());

        // Filling in the date of birth field
        onView(withId(R.id.dob_edit_text)).perform(replaceText("1/11/2000"));

        // Filling in the bio field
        onView(withId(R.id.bio)).perform(replaceText("Bio"));

        // Clicking the save button
        onView(withId(R.id.save)).perform(click());

        // Filling in the bio field
        onView(withId(R.id.bio)).perform(replaceText("I love running"));

        // Clicking the save button
        onView(withId(R.id.save)).perform(click());



        scenario.close();

//        ActivityScenario<MainActivity> scenario2 = ActivityScenario.launch(MainActivity.class);
//        Espresso.onView(ViewMatchers.withId(R.id.nav_preferences)).perform(ViewActions.click());
//        Espresso.onView(ViewMatchers.withText("Preferences")).perform(ViewActions.click());
//
//        // Simulate interactions with the fragment's views
//        onView(withId(R.id.exercise)).perform(click());
//        onView(withId(R.id.location)).perform(click());
//        onView(withId(R.id.male_button)).perform(click());
//        onView(withId(R.id.dob_edit_text)).perform(replaceText("1/11/2000"));
//        onView(withId(R.id.bio)).perform(replaceText("Bio"));
//        onView(withId(R.id.save)).perform(click());
//
//        // Add assertions to verify the fragment's behavior
//        // For example, you can check if certain text is displayed after clicking the save button
//        onView(withId(R.id.bio)).check(matches(withText("I love running")));
//        scenario2.close();
    }

}
