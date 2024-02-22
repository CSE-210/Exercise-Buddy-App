package com.example.activeamigo;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Objects;

public class Algorithm {

    private static final String TAG = "Algorithm";

    public void fetchUserDocumentsAndProcess(String user1) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Accounts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            List<DocumentSnapshot> userDocuments = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                userDocuments.add(document);
                            }
                            Log.e(TAG, "Reterival success ");
                            processUserDocuments(user1, userDocuments);

                        } else {
                            Log.e(TAG, "Error getting user documents: ", task.getException());
                        }
                    }
                });
    }

    private void processUserDocuments(String user1, List<DocumentSnapshot> userDocuments) {
        // Process the user documents and schedule data with your algorithm
        List<List<Object>> similarUsers = findSimilarSchedulesForUser(user1, userDocuments);
        for (List<Object> user : similarUsers) {
            Log.d(TAG, "User: " + user.get(1) + ", Score: " + user.get(0));
        }
    }
    public static List<List<Object>> findSimilarSchedulesForUser(String user1, List<DocumentSnapshot> userDocuments) {
        List<List<Object>> similarUsers = new ArrayList<>();

        // Retrieve the schedule of user1 from the userDocuments list
        HashMap<String, Object> user1Schedule = new HashMap<>();
        user1Schedule = null;
        String user1Day = null;
        String user1Exercise = null;
        String user1Location = null;
        String user1Gender = null;

        for (DocumentSnapshot document : userDocuments) {
            if (document.getId().equals(user1) && document.contains("Calendar")) {
                user1Schedule = (HashMap<String,Object>) document.get("Calendar");
                user1Exercise = document.getString("Exercise");
                user1Location = document.getString("Location");
                user1Gender = document.getString("Gender");
                user1Day = document.getString("Day");
            }
        }
        if (user1Schedule == null) {
            System.out.println("Warning: No schedule found");
            return similarUsers; // Return an empty list if user1's schedule is not found or incomplete
        }

        // Calculate similarity scores for other users' schedules
        for (DocumentSnapshot document : userDocuments) {
            String userId = document.getId();
            String userExercise = document.getString("Exercise");
            String userLocation = document.getString("Location");
            String userGender = document.getString("Gender");

//            if (userId.equals(user1)) continue; // Skip user1
            if (user1Gender != null) {
                if (!user1Gender.equals(userGender))
                    continue;
            }
            if (user1Exercise != null) {
                if (!user1Exercise.equals(userExercise))
                    continue;
            }
            if (user1Location != null) {
                if (!user1Location.equals(userLocation))
                    continue;
            }
            Log.d(TAG, "User: " + userId);
            HashMap<String, Object> uSchedule = null;
            if (document.contains("Calendar")) {
                uSchedule = (HashMap<String, Object>) document.get("Calendar");
            }

            // Calculate score only if user's schedule exists and has the same day as user1's day prefernce
            if (uSchedule.containsKey(user1Day)) {
                Object uScheduleForDayObject = uSchedule.get(user1Day);
                Object user1ScheduleForDayObject = user1Schedule.get(user1Day);
                if (uScheduleForDayObject instanceof List && user1ScheduleForDayObject instanceof List) {
                    List<Long> uScheduleForDay = (List<Long>) uScheduleForDayObject;
                    List<Long> user1ScheduleForDay = (List<Long>) user1ScheduleForDayObject;
                    int currScore = calculateScore(user1ScheduleForDay, uScheduleForDay);
                    similarUsers.add(Arrays.asList(currScore, userId));
                } else {
                    Log.e(TAG, "Not appropraite data struct in schedule");
                }

            }
        }

        // Sort similarUsers based on score in descending order
        similarUsers.sort((a, b) -> (int) b.get(0) - (int) a.get(0));

        return similarUsers;
    }

    // Helper method to calculate similarity score between two schedules
        private static int calculateScore(List<Long> schedule1, List<Long> schedule2) {
            int score = 0;
            int i = 0;
            while (i < schedule1.size() && i < schedule2.size()) {
                if (schedule1.get(i)==schedule2.get(i) && schedule1.get(i)==1) {
                    score++;
                }
                i++;
            }
            return score;
        }
}
