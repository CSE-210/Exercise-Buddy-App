package com.example.activeamigo;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import java.util.*;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CalendarActivity extends AppCompatActivity {
    protected FirebaseFirestore db;
    protected static String globalDay;
    protected static List<Integer> timeTextViewIds;
    protected static List<Integer> dayButtonIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        globalDay = null;
        timeTextViewIds = new ArrayList<>();
        dayButtonIds = new ArrayList<>();

        // Setting up the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // showing the back button in action bar

        // Create the main layout (LinearLayout)
        LinearLayout mainLayout = createMainLayout(this);

        // Create the header with buttons to switch between days
        LinearLayout headerLayout = createHeaderLayout(this);
        mainLayout.addView(headerLayout);

        // Create the scrollable section underneath the header
        ScrollView scrollableSection = createScrollableSection(this);
        mainLayout.addView(scrollableSection);

        // Set the main layout as the content view for the activity
        setContentView(mainLayout);

        Log.d("Calendar", "Time Slot Ids");
        for (int i: timeTextViewIds) {
            Log.d("Calendar", i + " ");
        }

        Log.d("Calendar", "Day Button Ids");
        for (int i: dayButtonIds) {
            Log.d("Calendar", i + ": ");
        }
    }

    /** Set Back Button **/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Creates the main layout for the CalendarActivity.
     *
     * @param context The context in which the layout will be created.
     * @return The main layout (LinearLayout).
     */
    public LinearLayout createMainLayout(Context context) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        LinearLayout mainLayout = new LinearLayout(context);
        mainLayout.setLayoutParams(layoutParams);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        return mainLayout;
    }

    /**
     * Creates the header layout with buttons to switch between days.
     *
     * @param context The context in which the layout will be created.
     * @return The header layout (LinearLayout).
     */
    public LinearLayout createHeaderLayout(Context context) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        LinearLayout headerLayout = new LinearLayout(context);
        headerLayout.setLayoutParams(layoutParams);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Add buttons for each day of the week
        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : daysOfWeek) {
            Button dayButton = createDayButton(context, day);

            // Assign unique IDs to each timeTextView
            int dayButtonId = View.generateViewId();
            dayButton.setId(dayButtonId);
            dayButtonIds.add(dayButtonId);

            headerLayout.addView(dayButton);
        }
        return headerLayout;
    }

    /**
     * Helper Method
     * Creates a day button for the header layout.
     *
     * @param context The context in which the button will be created.
     * @param day     The day text for the button.
     * @return The day button (Button).
     */
    public Button createDayButton(Context context, String day) {
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                2
        );

        Button dayButton = new Button(context);
        dayButton.setLayoutParams(buttonParams);
        dayButton.setText(day);
        dayButton.setBackgroundColor(Color.WHITE);

        // Set onClickListener to handle button clicks (switch between days)
        dayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetdayColors((LinearLayout) dayButton.getParent());
                dayButton.setBackgroundColor(Color.parseColor("#CCE5FF"));
                // Implement logic to switch between days here
                // You can use the 'day' variable to get the selected time slot
                // Grab the data that matches the week clicked
                globalDay = day;
                resetTimeColors();
                displayCalendar();
            }
        });
        return dayButton;
    }

    // Helper Method to reset the days clicked
    private void resetdayColors(LinearLayout headerLayout) {
        Log.d("CalendarEntry", "childCount: "+headerLayout.getChildCount());
        for (int i = 0; i < headerLayout.getChildCount(); i++) {
            View child = headerLayout.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setBackgroundColor(Color.WHITE);
            }
        }
    }

    // Helper method to reset the time slots clicked
    private void resetTimeColors() {
        for (int i=0; i<timeTextViewIds.size(); i++) {
            int id = timeTextViewIds.get(i);
            findViewById(id).setBackgroundColor(Color.WHITE);
        }
    }

    /**
     * Creates the scrollable section underneath the header.
     *
     * @param context The context in which the section will be created.
     * @return The scrollable section (ScrollView).
     */
    private ScrollView createScrollableSection(Context context) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        ScrollView scrollView = new ScrollView(context);
        scrollView.setLayoutParams(layoutParams);

        // Create a vertical linear layout for displaying different times of the day
        LinearLayout timeLayout = createTimeLayout(context);

        scrollView.addView(timeLayout);

        return scrollView;
    }

    /**
     * Creates a vertical linear layout for displaying different times of the day.
     *
     * @param context The context in which the layout will be created.
     * @return The time layout (LinearLayout).
     */
    private LinearLayout createTimeLayout(Context context) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        LinearLayout timeLayout = new LinearLayout(context);

        timeLayout.setLayoutParams(layoutParams);
        timeLayout.setOrientation(LinearLayout.VERTICAL);

        // Add TextViews to represent different times of the day
        for (int i = 0; i < 24; i++) {
            TextView timeTextView = createClickableTextView(context, i + ":00");

            // Assign unique IDs to each timeTextView
            int textViewId = View.generateViewId();
            timeTextView.setId(textViewId);
            timeTextViewIds.add(textViewId);

            timeLayout.addView(timeTextView);
        }
        return timeLayout;
    }

    /**
     * Creates a clickable TextView representing a time slot on the UI.
     *
     * @param context The context in which the TextView will be created.
     * @param time    The time value associated with the TextView.
     * @return A TextView instance representing a clickable time slot.
     */
    private TextView createClickableTextView(Context context, String time) {
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        TextView textView = new TextView(context);
        textView.setLayoutParams(textViewParams);
        textView.setText(time);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(30, 50, 16, 50);
        textView.setBackgroundColor(Color.WHITE);

        // Set onClickListener for each time slot
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (globalDay != null) {
                    // You can use the 'time' variable to get the selected time slot
                    if (((ColorDrawable) textView.getBackground()).getColor() == Color.WHITE) {
                        textView.setBackgroundColor(Color.parseColor("#CCE5FF"));
                        updateCalendar(true, time); // add selected time from calendar
                    } else {
                        textView.setBackgroundColor(Color.WHITE);
                        // remove time slot from DB
                        updateCalendar(false, time); // remove selected time from calendar
                    }
                }
            }
        });
        return textView;
    }

    /**
     * Helper method to update (add/remove) available time slots on Firestore.
     *
     * @param add           True if adding availability, False if removing.
     * @param selectedTime  The selected time in the format "HH:mm".
     */
    public void updateCalendar(Boolean add, String selectedTime) {
        Log.d("CalendarEntry:",  "inside updateCalendar-())");
        Log.d("CalendarEntry:",  "  --  " + selectedTime);
        DocumentReference docRef = db.collection("Accounts").document("mj@ucsd.edu");
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            Log.d("CalendarEntry:", "documentSnapshot found");
            if (documentSnapshot.exists()) {
                Log.d("CalendarEntry:", "  --  HERE 0");
                // If document exists, fetch data
                Map<String, Object> data = documentSnapshot.getData();
                if (data != null) {
                    // Check if the calendar field exists in the document
                    if (data.containsKey("calendar")) {
                        Log.d("CalendarEntry:", "  --  HERE 1");
                        // Get the calendar field
                        Object calendarObj = data.get("calendar");
                        if (calendarObj instanceof HashMap) {
                            // Cast it to HashMap<String, ArrayList> if it is a HashMap
                            HashMap<String, List<Integer>> calendarMap = (HashMap<String, List<Integer>>) calendarObj;
                            Log.d("CalendarEntry", "found it: " + "Key: " + globalDay);
                            Log.d("CalendarEntry", "Value: " + calendarMap.get(globalDay));

                            // Parse the selected time
                            int time = Integer.parseInt(selectedTime.split(":")[0]);

                            Log.d("CalendarEntry", "selectedTime: " + time);

                            // Update the availability
                            List<Integer> availabilityList = calendarMap.get(globalDay);
                            if (availabilityList != null && time < availabilityList.size()) {
                                if (add) { // add availability
                                    availabilityList.set(time, 1);
                                    Log.d("CalendarEntry", "Selected time will be added");
                                }
                                else { // remove availability
                                    availabilityList.set(time, 0);  // TODO: MODIFY HERE TO BE THE OPPOSITE VAL 0,1
                                    Log.d("CalendarEntry", "Selected time will be removed");
                                }
                                calendarMap.put(globalDay, availabilityList);

                                // Update Firestore document
                                docRef.update("calendar", calendarMap)
                                        .addOnSuccessListener(aVoid -> Log.d("CalendarEntry", "DocumentSnapshot successfully updated!"))
                                        .addOnFailureListener(e -> Log.w("CalendarEntry", "Error updating document", e));
                            } else {
                                Log.d("CalendarEntry", "Invalid time or availability list for the selected day");
                            }
                        }
                    } else {
                        // Calendar field does not exist in the document
                        Log.d("CalendarEntry", "Calendar field does not exist in the document");
                    }
                }
            }
        }).addOnFailureListener(e -> {
            // Handle failure
            Log.e("CalendarEntry", "Error fetching document", e);
        });
    }

    /**
     * Helper method to fetch calendar information to display on the UI.
     */
    void displayCalendar() {
        Log.d("CalendarEntry", "inside displayCalendar");
        DocumentReference docRef = db.collection("Accounts").document("mj@ucsd.edu");
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            Log.d("CalendarEntry", "documentSnapshot found");
            if (documentSnapshot.exists()) {
                Log.d("CalendarEntry", "documentSnapshot exists");
                Map<String, Object> data = documentSnapshot.getData();
                if (data != null && data.containsKey("calendar")) {
                    Log.d("CalendarEntry", "calendar data exists");
                    Object calendarObj = data.get("calendar");
                    if (calendarObj instanceof HashMap) {
                        HashMap<String, List<Long>> calendarMap = (HashMap<String, List<Long>>) calendarObj;
                        List<Long> availabilityList = calendarMap.get(globalDay);
                        if (availabilityList != null) {
                            // Display availability on UI
                            for (int i = 0; i < availabilityList.size(); i++) {
                                if (availabilityList.get(i) == 1) {
                                    Log.d("CalendarEntry", i + ": " + availabilityList.get(i));
                                    int id = timeTextViewIds.get(i); // set the background for textID
                                    findViewById(id).setBackgroundColor(Color.parseColor("#CCE5FF"));
//                                    Log.d("CalendarEntry", i + ": " + Integer.toString(timeTextViewIds.get(i)));
                                }
                            }
                        }
                    }
                }
            }
        }).addOnFailureListener(e -> {
            // Handle failure
            Log.e("CalendarEntry", "Error fetching document", e);
        });
    }
}