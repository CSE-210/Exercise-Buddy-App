package com.example.activeamigo;

//import static androidx.appcompat.graphics.drawable.DrawableContainerCompat.Api21Impl.getResources;

import android.util.Log;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Collections;


public class Algorithm {

    private static final String TAG = "Algorithm";
    private HashMap<String,String> currentUserData = new HashMap<>();
    public interface OnFetchCompleteListener {
        void onFetchComplete(List<HashMap<String, Object>> matches, HashMap<String, String> currentUserData);
        void onFetchError(Exception e);
    }

    // Method to fetch user documents from Firestore and process them
    public void fetchUserDocumentsAndProcess(String user1, HashMap<String, String> filters, String collectionPath, OnFetchCompleteListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collectionPath)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> userDocuments = task.getResult().getDocuments();
                        Log.d(TAG, "Retrieval success");
                        currentUserData = getUser1Data(user1, userDocuments);
                        List<HashMap<String, Object>> matches = findSimilarSchedulesForUser(user1, userDocuments, filters);
                        listener.onFetchComplete(matches,currentUserData);
                    } else {
                        Log.e(TAG, "Error getting user documents: ", task.getException());
                        listener.onFetchError(task.getException());
                    }
                });
    }


    private static HashMap<String,String> getUser1Data(String user1, List<DocumentSnapshot> userDocuments){
        // Initialize hashmap and variables to hold the extracted data. Initially set to null.
        HashMap<String,String> user1Data = new HashMap<String ,String>();
        String user1Exercise = null;
        String user1Location = null;
        String user1Gender = null;
        // Iterate over each document in the list of user documents.
        for (DocumentSnapshot doc: userDocuments){
            // Check if the current document's ID matches the user ID we're interested in (user1).
            if(doc.getId().equals(user1)){
                // If it matches, extract the exercise, gender, and location data from the document.
                user1Exercise = doc.getString("exercise");
                user1Gender = doc.getString("gender");
                user1Location = doc.getString("location");
            }
        }
        // Once all documents have been checked and data extracted (if found), add the data to the HashMap.
        user1Data.put("exercise",user1Exercise);
        user1Data.put("location",user1Location);
        user1Data.put("gender",user1Gender);
        Log.d("User1Data algo",user1Data.toString());
        return user1Data;
    }

    // Method to find similar schedules for a given user
    static List<HashMap<String,Object>> findSimilarSchedulesForUser(String user1, List<DocumentSnapshot> userDocuments, HashMap<String, String> Filters) {
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
                user1Schedule = (HashMap<String,Object>) document.get("calendar");
                if(Filters.get("exercise") == null)
                    user1Exercise = document.getString("exercise");
                else
                    user1Exercise = Filters.get("exercise");
                if(Filters.get("location") == null)
                    user1Location = document.getString("location");
                else
                    user1Location = Filters.get("location");
                if(Filters.get("gender") == null)
                    user1Gender = document.getString("gender");
                else
                    user1Gender = Filters.get("gender");
                break;
            }else {
                Log.e(TAG, "Not appropraite user info are fetched");
            }
        }

        if (user1Schedule == null) {
            System.out.println("Warning: No schedule found");
            return similarUsers; // Return an empty list if user1's schedule is not found or incomplete
        }
        LocalDate currentDate = LocalDate.now();

        // Get the day of the week
        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
        if(Filters.get("day") != null)
            user1Day = Filters.get("day").toLowerCase();
        else
            user1Day = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toLowerCase();
        // Calculate similarity scores for other users' schedules
        for (DocumentSnapshot document : userDocuments) {
            String userId = document.getId();
            String userExercise = document.getString("exercise");
            String userLocation = document.getString("location");
            String userGender = document.getString("gender");

            if (userId.equals(user1)) continue; // Skip user1
            if (user1Gender != null) {
                if(!user1Gender.equals("Select Gender"))
                if (!user1Gender.equals(userGender))
                    continue;
            }
            if (user1Exercise != null) {
                if(!user1Exercise.equals("Select Activity"))
                if (!user1Exercise.equals(userExercise))
                    continue;
            }
            if (user1Location != null) {
                if(!user1Location.equals("Select Location"))
                if (!user1Location.equals(userLocation))
                    continue;
            }
//            if(user1Day!=null ){
//                if(!user1Day.equals(user))
//            }
            HashMap<String, Object> uSchedule = null;
            if (document.contains("calendar")) {
                uSchedule = (HashMap<String, Object>) document.get("calendar");
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
                    userData.put("exercise",userExercise);
                    userData.put("name",document.getString("name"));
                    userData.put("bio",document.getString("bio"));
                    userData.put("email",document.getString("email"));
                    userData.put("location",userLocation);
                    userData.put("gender",userGender);
                    userData.put("score",currScore);
                    if(currScore>0) {
                        similarUsers.add(userData);
                    }
                } else {
                    Log.e(TAG, "Not appropraite data struct in schedule");
                }

            }
        }

        // Sort similarUsers based on score in descending order
        Collections.sort(similarUsers, (firstMap, secondMap) -> {
            Integer firstValue = (Integer)firstMap.get("score");
            Integer secondValue = (Integer)secondMap.get("score");
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
