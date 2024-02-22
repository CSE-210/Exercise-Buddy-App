package com.example.activeamigo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DataPopulator {

    public static void populateDummyData() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference accountsCollection = firestore.collection("Accounts");

        // Define the number of dummy users to create
        int numUsers = 20;

        // Define the range of random values for generating dummy data
        Random random = new Random();
        String[] genders = {"Male", "Female"};
        String[] activities = {"Running", "Swimming", "Cycling", "Yoga", "Gym"};
        String[] locations = {"Rimac", "Graduate Housing", "Main Gym"};
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

        // Create dummy data for each user
        for (int i = 1; i <= numUsers; i++) {
            // Generate a random email
            String email = generateRandomEmail(i);

            // Generate other random fields
            String name = "User" + i;
            String password = "password" + i;
            String bio = "Hi, I am " + name;
            String dob = generateRandomDateOfBirth();
            String Day = days[random.nextInt(days.length)];
            String gender = genders[random.nextInt(genders.length)];
            String exercise = activities[random.nextInt(activities.length)];
            String location = locations[random.nextInt(locations.length)];

            // Generate dummy calendar data (one-hot encoding)
            Map<String, Object> calendar = generateDummyCalendar();

            // Create a map representing the user data
            Map<String, Object> userData = new HashMap<>();
            userData.put("Email", email);
            userData.put("Name", name);
            userData.put("Password", password);
            userData.put("Bio", bio);
            userData.put("Dob", dob);
            userData.put("Gender", gender);
            userData.put("Exercise", exercise);
            userData.put("Location", location);
            userData.put("Day", Day);
            userData.put("Calendar", calendar);

            // Add the user data to Firestore as a document
            DocumentReference userDocument = accountsCollection.document(email);
            userDocument.set(userData, SetOptions.merge());

            System.out.println("User added: " + email);
        }
    }

    // Helper method to generate a random email
    private static String generateRandomEmail(int index) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return sb.toString() + index + "@ucsd.edu";
    }

    // Helper method to generate a random date of birth
    private static String generateRandomDateOfBirth() {
        Random random = new Random();
        int year = 1970 + random.nextInt(30);
        int month = 1 + random.nextInt(12);
        int day = 1 + random.nextInt(28); // Assuming all months have 28 days for simplicity
        return String.format("%d-%02d-%02d", year, month, day);
    }

    // Helper method to generate dummy calendar data (one-hot encoding)
    private static Map<String, Object> generateDummyCalendar() {
        Random random = new Random();
        Map<String, Object> calendar = new HashMap<>();
        for (String day : new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"}) {
            ArrayList<Integer> hours = new ArrayList<Integer>();
            for (int i = 0; i < 24; i++) {
                hours.add(random.nextInt(2)); // Randomly assign 0 or 1 to each hour
            }
            calendar.put(day, hours);
        }
        return calendar;
    }
}
