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
    // Access a Cloud Firestore instance from your Activity

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String globalDay = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    private LinearLayout createMainLayout(Context context) {
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
    private LinearLayout createHeaderLayout(Context context) {
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
    private Button createDayButton(Context context, String day) {
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
                handleDayClick(day);
                displayCalendar((LinearLayout) dayButton.getParent());
            }
        });
        return dayButton;
    }

    // Helper Method to reset the days clicked
    private void resetdayColors(LinearLayout headerLayout) {
        for (int i = 0; i < headerLayout.getChildCount(); i++) {
            View child = headerLayout.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setBackgroundColor(Color.WHITE);
            }
        }
    }

    // Method to handle the click on a time slot
    private void handleDayClick(String day) {
        // Modify to import the time selected to display
        Toast.makeText(this, "Day of the week: " + day, Toast.LENGTH_SHORT).show();
        // call getCurrentCalendar and color in the time slot selected
        globalDay = day;
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
            timeLayout.addView(timeTextView);
        }
        return timeLayout;
    }

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
                        handleTimeSlotClick(time); // add time slot to DB
                    } else {
                        textView.setBackgroundColor(Color.WHITE);
                        // remove time slot from DB
//                    updateCalendar(false, time); // remove selected time from calendar
                    }
                }
            }
        });
        return textView;
    }

    // Method to handle the click on a time slot
    private void handleTimeSlotClick(String selectedTime) {
        // Modify to import the time selected to display
        Toast.makeText(this, "Selected Time: " + selectedTime, Toast.LENGTH_SHORT).show();
        updateCalendar(true, selectedTime);

        // TASK: modify to add to the right account later
//        db.collection("Accounts").document("User1")
//                .set(calendar, SetOptions.merge())
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d("Firestore", "DocumentSnapshot successfully written!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("Firestore", "Error writing document", e);
//                    }
//                });

//        HashMap<String, Object> current = getCurrentCalendar();
//        for (Map.Entry<String, Object> entry : current.entrySet()) {
//            String key = entry.getKey();
//            Object value = entry.getValue();
//            Log.d("CalendarEntry", "Key: " + key + ", Value: " + value.toString());
//            Log.d("CalendarEntry", "I am here");
//        }

        // set the selected time to be true
//        (ArrayList<Integer>)current.get(globalDay)[selectedTime] = 1;
    }

    public void updateCalendar(Boolean add, String selectedTime) {
        Log.d("CalendarEntry:",  "inside updateCalendar())");
        HashMap<String, Object> current = new HashMap<>();
        DocumentReference docRef = db.collection("Accounts").document("cn@ucsd.edu");
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            Log.d("CalendarEntry:",  "documentSnapshot found");
            if (documentSnapshot.exists()) {
                // If document exists, fetch data
                Map<String, Object> data = documentSnapshot.getData();
                if (data != null) {
                    // Check if the calendar field exists in the document
                    if (data.containsKey("calendar")) {
                        // Get the calendar field
                        Object calendarObj = data.get("calendar");
                        if (calendarObj instanceof HashMap) {
                            // Cast it to HashMap<String, ArrayList> if it is a HashMap
                            HashMap<String, List<Integer>> calendarMap = (HashMap<String, List<Integer>>) calendarObj;
                            Log.d("CalendarEntry", "found it: " + "Key: " + globalDay);
                            Log.d("CalendarEntry", "Value: " + calendarMap.get(globalDay));

                            // parse the selected time
                            String time = selectedTime.split(":")[0];
                            int time1 = Integer.parseInt(selectedTime.split(":")[0]);

                            Log.d("CalendarEntry", "selectedTime: " + time1);
//                            int avail = calendarMap.get(globalDay).toString().charAt(Integer.parseInt(time));
                            Log.d("CalendarEntry", "Current avail at selectedTime" + "avail: " + calendarMap.get(globalDay).get(time1));
//                            calendarMap.get(globalDay).set(time1, 1);
//                            Log.d("CalendarEntry", "After avail at selectedTime" + "avail: " + calendarMap.get(globalDay).get(time1));
                        }
                    } else {
                        // Calendar field does not exist in the document
                        Log.d("CalendarEntry", "Calendar field does not exist in the document");
                    }
                }
            }
        }).addOnFailureListener(e -> {
            // Handle failure
        });
    }

    public void displayCalendar(LinearLayout headerLayout) {
        Log.d("CalendarEntry:",  "inside updateCalendar())");
        HashMap<String, Object> current = new HashMap<>();
        DocumentReference docRef = db.collection("Accounts").document("cn@ucsd.edu");
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            Log.d("CalendarEntry:",  "documentSnapshot found");
            if (documentSnapshot.exists()) {
                // If document exists, fetch data
                Map<String, Object> data = documentSnapshot.getData();
                if (data != null) {
                    // Check if the calendar field exists in the document
                    if (data.containsKey("calendar")) {
                        // Get the calendar field
                        Object calendarObj = data.get("calendar");
                        if (calendarObj instanceof HashMap) {
                            // Cast it to HashMap<String, ArrayList> if it is a HashMap
                            HashMap<String, List<Integer>> calendarMap = (HashMap<String, List<Integer>>) calendarObj;
                            List<Integer> times = calendarMap.get(globalDay);

                            // loop through the array of possible times
                            for (int i=0; i<times.size(); i++) {
                                if (times.get(i) == 1) { // if the time is selected then color it
                                    View child = headerLayout.getChildAt(i);
                                    if (child instanceof TextView) {
                                        ((TextView) child).setBackgroundColor(Color.parseColor("#CCE5FF"));
                                    }
                                }
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
        });
    }

//    public HashMap<String, Object> getCurrentCalendar() {
//        HashMap<String, Object> current = new HashMap<>();
//        DocumentReference docRef = db.collection("Accounts").document("cn@ucsd.edu");
//        docRef.get().addOnSuccessListener(documentSnapshot -> {
//            if (documentSnapshot.exists()) {
//                // If document exists, fetch data
//                Map<String, Object> data = documentSnapshot.getData();
//                if (data != null) {
//                    // Check if the calendar field exists in the document
//                    if (data.containsKey("calendar")) {
//                        // Get the calendar field
//                        Object calendarObj = data.get("calendar");
//                        if (calendarObj instanceof HashMap) {
//                            // Cast it to HashMap<String, ArrayList> if it is a HashMap
//                            HashMap<String, Object> calendarMap = (HashMap<String, Object>) calendarObj;
//                            // Log the contents of the calendarMap
//                            for (Map.Entry<String, Object> entry : calendarMap.entrySet()) {
//                                String key = entry.getKey();
//                                Object value = entry.getValue();
////                                current.put(key,
////                                        value);
//                                // Log key and value
//                                Log.d("CalendarEntry", "Key: " + key + ", Value: " + value.toString());
//                            }
//                            Log.d("CalendarEntry", "Length1: " + current.size());
//                        }
//                    } else {
//                        // Calendar field does not exist in the document
//                        Log.d("CalendarEntry", "Calendar field does not exist in the document");
//                    }
//                }
//            }
//        }).addOnFailureListener(e -> {
//            // Handle failure
//        });
//        Log.d("CalendarEntry", "Length: " + current.size());
//        return current;
//    }
}