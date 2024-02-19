package com.example.activeamigo;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                1
        );

        Button dayButton = new Button(context);
        dayButton.setLayoutParams(buttonParams);
        dayButton.setText(day);
        dayButton.setBackgroundColor(Color.LTGRAY);

        // Set onClickListener to handle button clicks (switch between days)
        dayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetdayColors((LinearLayout) dayButton.getParent());
                dayButton.setBackgroundColor(Color.YELLOW);
                // Implement logic to switch between days here
                // You can use the 'day' variable to get the selected time slot
                // Grab the data that matches the week clicked
                handleDayClick(day);
            }
        });
        return dayButton;
    }

    // Helper Method to reset the days clicked
    private void resetdayColors(LinearLayout headerLayout) {
        for (int i = 0; i < headerLayout.getChildCount(); i++) {
            View child = headerLayout.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setBackgroundColor(Color.LTGRAY);
            }
        }
    }

    // Method to handle the click on a time slot
    private void handleDayClick(String day) {
        // Modify to import the time selected to display
        Toast.makeText(this, "Day of the week: " + day, Toast.LENGTH_SHORT).show();
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
                // You can use the 'time' variable to get the selected time slot
                if (((ColorDrawable) textView.getBackground()).getColor() == Color.WHITE) {
                    textView.setBackgroundColor(Color.YELLOW);
                    handleTimeSlotClick(time); // add time slot to DB
                } else {
                    textView.setBackgroundColor(Color.WHITE);
                    // remove time slot from DB
                }
            }
        });
        return textView;
    }

    // Method to handle the click on a time slot
    private void handleTimeSlotClick(String selectedTime) {
        // Modify to import the time selected to display
        Toast.makeText(this, "Selected Time: " + selectedTime, Toast.LENGTH_SHORT).show();
    }
}