package com.example.activeamigo;

import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RunWith(AndroidJUnit4.class)
public class CreateMockUsers {
    private static final String[] NAMES = {"John Adams", "Emily Johnson", "Michael Brown", "Sarah Davis", "David Wilson"};
    private static final String[] DATES_OF_BIRTH = {"03/08/2024", "12/15/2023", "07/22/2024", "04/28/2024", "11/03/2023"};
    private static final String[] BIOS = {"Hi, love to connect!", "Fitness enthusiast here!", "Looking for workout partners", "New to the area", "Let's get fit together!"};
    private static final String[] GENDERS = {"female", "male"};
    private static final String[] LOCATIONS = {"Rimac", "24hr Fitness UTC", "Canyonview", "Graduate Housing"};
    private static final String[] EXERCISES = {"Gym", "Volleyball", "Basketball", "Soccer", "Tennis"};

    public void setUp() {
    }

    @Test
    public void createUsers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference accountsCollection = db.collection("Accounts");
        Random random = new Random();

        for (int i = 0; i < 2; i++) {
            String name = NAMES[random.nextInt(NAMES.length)];
            String dob = DATES_OF_BIRTH[random.nextInt(DATES_OF_BIRTH.length)];
            String bio = BIOS[random.nextInt(BIOS.length)];
            String gender = GENDERS[random.nextInt(GENDERS.length)];
            String location = LOCATIONS[random.nextInt(LOCATIONS.length)];
            String exercise = EXERCISES[random.nextInt(EXERCISES.length)];
            Map<String, ArrayList<Long>> calendar = generateRandomCalendar();

            Map<String, Object> userData = new HashMap<>();
            userData.put("name", name);
            userData.put("dob", dob);
            userData.put("bio", bio);
            userData.put("gender", gender);
            userData.put("location", location);
            userData.put("exercise", exercise);
            userData.put("calendar", calendar);

            accountsCollection.add(userData);
//            System.out.println("Added document with ID: " + future.get().getUpdateTime());
        }

        assertTrue(true);
    }

    public static Map<String, ArrayList<Long>> generateRandomCalendar() {
        Map<String, ArrayList<Long>> calendar = new HashMap<>();
        String[] days = {"mon", "tue", "wed", "thu", "fri", "sat", "sun"};
        Random random = new Random();

        for (String day : days) {
            ArrayList<Long> dayCalendar = new ArrayList<>();
            for (int i = 0; i < 24; i++) {
                Long num = (long)random.nextInt(2);
                dayCalendar.add(num);
            }
            calendar.put(day, dayCalendar);
        }

        return calendar;
    }
}
