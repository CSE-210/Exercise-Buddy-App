package com.example.activeamigo;

import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Collections;
import java.util.concurrent.ExecutionException;


public class Algorithm {

    private static final String TAG = "Algorithm";

    public interface OnFetchCompleteListener {
        void onFetchComplete(List<HashMap<String, Object>> matches);
        void onFetchError(Exception e);
    }

    public void fetchUserDocumentsAndProcess(String user1, HashMap<String, String> filters, OnFetchCompleteListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("mockedAccounts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> userDocuments = task.getResult().getDocuments();
                        Log.d(TAG, "Retrieval success");
                        List<HashMap<String, Object>> matches = findSimilarSchedulesForUser(user1, userDocuments, filters);
                        listener.onFetchComplete(matches);
                    } else {
                        Log.e(TAG, "Error getting user documents: ", task.getException());
                        listener.onFetchError(task.getException());
                    }
                });
    }

    private static List<HashMap<String,Object>> findSimilarSchedulesForUser(String user1, List<DocumentSnapshot> userDocuments,HashMap<String,String> Filters) {
        List<HashMap<String,Object>> similarUsers = new ArrayList<>();

        // Retrieve the schedule of user1 from the userDocuments list
        HashMap<String, Object> user1Schedule = new HashMap<>();
        user1Schedule = null;
        String user1Exercise = null;
        String user1Location = null;
        String user1Gender = null;
        String user1Day = null;

        for (DocumentSnapshot document : userDocuments) {
            if (document.getId().equals(user1)) {
                user1Schedule = (HashMap<String,Object>) document.get("Calendar");
                if(Filters.get("Exercise") == null)
                    user1Exercise = document.getString("Exercise");
                else
                    user1Exercise = Filters.get("Exercise");
                if(Filters.get("Location") == null)
                    user1Location = document.getString("Location");
                else
                    user1Location = Filters.get("Location");
                if(Filters.get("Gender") == null)
                    user1Gender = document.getString("Gender");
                else
                    user1Gender = Filters.get("Gender");
                break;
            }
        }
        if (user1Schedule == null) {
            System.out.println("Warning: No schedule found");
            return similarUsers; // Return an empty list if user1's schedule is not found or incomplete
        }
        LocalDate currentDate = LocalDate.now();

        // Get the day of the week
        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
        if(Filters.get("Day") == null)
            user1Day = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        // Get the day name abbreviated
        else
            user1Day = Filters.get("Day");
        // Calculate similarity scores for other users' schedules
        for (DocumentSnapshot document : userDocuments) {
            String userId = document.getId();
            String userExercise = document.getString("Exercise");
            String userLocation = document.getString("Location");
            String userGender = document.getString("Gender");

            if (userId.equals(user1)) continue; // Skip user1
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
            //Log.d(TAG, "User: " + userId);
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
                    HashMap<String, Object> userData = new HashMap<>();
                    userData.put("Exercise",userExercise);
                    userData.put("Name",document.getString("Name"));
                    userData.put("Bio",document.getString("Bio"));
                    userData.put("Email",document.getString("Email"));
                    userData.put("Location",userLocation);
                    userData.put("Gender",userGender);
                    userData.put("Score",currScore);
                    similarUsers.add(userData);
                } else {
                    Log.e(TAG, "Not appropraite data struct in schedule");
                }

            }
        }

        // Sort similarUsers based on score in descending order
        Collections.sort(similarUsers, (firstMap, secondMap) -> {
            Integer firstValue = (Integer)firstMap.get("Score");
            Integer secondValue = (Integer)secondMap.get("Score");
            return secondValue.compareTo(firstValue);
        });

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
