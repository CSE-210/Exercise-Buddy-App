package com.example.activeamigo;

import static org.junit.Assert.assertEquals;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CalendarPageTests {

    @Before
    public void setUp() {
    }

    @Test
    public void testDayButtonClicked() {
        // Creates activity to test
        ActivityScenario<CalendarActivity> scenario = ActivityScenario.launch(CalendarActivity.class);
        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        for (int i=0; i<CalendarActivity.dayButtonIds.size(); i++) {
            int dayButtonId = CalendarActivity.dayButtonIds.get(i);
            String day = daysOfWeek[i];

            // Click on a day button
            Espresso.onView(ViewMatchers.withId(dayButtonId)).perform(ViewActions.click());

            assertEquals("globalDay should be updated after clicking on a day button",
                    day, CalendarActivity.globalDay);
        }
        scenario.close();
    }

//    @Test
//    public void testTimeSlotClicked() {
//        // Creates activity to test
//        ActivityScenario<CalendarActivity> scenario = ActivityScenario.launch(CalendarActivity.class);
//
//        // Replace with the actual ID of a time slot TextView
//        int timeSlotId = R.id.textViewTimeSlot;
//
//        // Replace with the actual ID of a day button (e.g., Sun)
//        int dayButtonId = R.id.buttonSunday;
//
//        // Click on a day button to set the global day
//        Espresso.onView(ViewMatchers.withId(dayButtonId)).perform(ViewActions.click());
//
//        // Click on a time slot
//        Espresso.onView(ViewMatchers.withId(timeSlotId)).perform(ViewActions.click());
//
//        // Check if the background color of the clicked time slot has changed
//        Espresso.onView(ViewMatchers.withId(timeSlotId))
//                .check(ViewAssertions.matches(ViewMatchers.withBackgroundColor(R.color.selectedTimeColor)));
//
//        scenario.close();
//    }

//    @Test
//    public void testUpdateCalendar() {
//        // Creates activity to test
//        ActivityScenario<CalendarActivity> scenario = ActivityScenario.launch(CalendarActivity.class);
//
//        // Replace with the actual ID of a time slot TextView
//        int timeSlotId = R.id.textViewTimeSlot;
//
//        // Replace with the actual ID of a day button (e.g., Sun)
//        int dayButtonId = R.id.buttonSunday;
//
//        // Click on a day button to set the global day
//        Espresso.onView(ViewMatchers.withId(dayButtonId)).perform(ViewActions.click());
//
//        // Click on a time slot to update the calendar
//        Espresso.onView(ViewMatchers.withId(timeSlotId)).perform(ViewActions.click());
//
//        // Check if the calendar has been updated (you may need to check the Firestore data)
//        // You'll need to add assertions based on your specific implementation for updating the calendar data in Firestore
//        // Example: Verify that the availability for the selected day and time has been updated in Firestore
//        // ...
//
//        scenario.close();
//    }
//
//    @Test
//    public void testResetDayColors() {
//        // Creates activity to test
//        ActivityScenario<CalendarActivity> scenario = ActivityScenario.launch(CalendarActivity.class);
//
//        // Replace with the actual ID of a day button (e.g., Sun)
//        int dayButtonId = R.id.buttonSunday;
//
//        // Click on a day button to set the global day
//        Espresso.onView(ViewMatchers.withId(dayButtonId)).perform(ViewActions.click());
//
//        // Perform a second click to simulate a reset (change in color)
//        Espresso.onView(ViewMatchers.withId(dayButtonId)).perform(ViewActions.click());
//
//        // Check if the background color of the day button has changed back to the initial state
//        Espresso.onView(ViewMatchers.withId(dayButtonId))
//                .check(ViewAssertions.matches(ViewMatchers.withBackgroundColor(R.color.initialDayColor)));
//
//        scenario.close();
//    }
//
//    @Test
//    public void testResetTimeColors() {
//        // Creates activity to test
//        ActivityScenario<CalendarActivity> scenario = ActivityScenario.launch(CalendarActivity.class);
//
//        // Replace with the actual ID of a time slot TextView
//        int timeSlotId = R.id.textViewTimeSlot;
//
//        // Click on a time slot to set the color
//        Espresso.onView(ViewMatchers.withId(timeSlotId)).perform(ViewActions.click());
//
//        // Perform a second click to simulate a reset (change in color)
//        Espresso.onView(ViewMatchers.withId(timeSlotId)).perform(ViewActions.click());
//
//        // Check if the background color of the time slot has changed back to the initial state
//        Espresso.onView(ViewMatchers.withId(timeSlotId))
//                .check(ViewAssertions.matches(ViewMatchers.withBackgroundColor(R.color.initialTimeColor)));
//
//        scenario.close();
//    }
}
