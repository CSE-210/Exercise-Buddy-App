package com.example.activeamigo;
import androidx.test.espresso.Espresso;
import static androidx.test.espresso.Espresso.onView;
import android.view.View;
import android.widget.ListView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;

import static org.hamcrest.Matchers.allOf;
import androidx.test.espresso.action.ViewActions;
//import androidx.test.espresso.contrib.RecyclerViewActions;
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;

import java.util.List;

public class MatchesScrollTest {
    public ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
//    public ActivityScenarioRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);
    @Test
    public void checkIfItemIsVisibleonScroll(){
        onView(withId(R.id.matchesList)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.matchesList)).perform(ViewActions.swipeUp()).check(matches(isDisplayed()));
    }
}
