package com.example.activeamigo;

import com.example.activeamigo.PreferenceActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import android.util.Log;

@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class AlgorithmUnitTest {

    @Mock
    FirebaseFirestore mockedFirestore;

    // Creates a mocked collection in the database
    @Mock
    CollectionReference mockedCollectionReference;

    // Creates a mock document in the collection
    @Mock
    DocumentReference mockedDocumentReference;

    // Makes a mocked query call to the database
    @Mock
    Query mockedQuery;

    PreferenceActivity preferenceActivity;

    @Before
    public void setUp() {
        openMocks(this);
        // Mocking the document reference
        mockedFirestore = Mockito.mock(FirebaseFirestore.class);

        Mockito.when(mockedFirestore.collection(any(String.class))).thenReturn(mockedCollectionReference);
        Mockito.when(mockedCollectionReference.whereEqualTo(any(String.class), any(Object.class))).thenReturn(mockedQuery);
        Mockito.when(mockedCollectionReference.document(any(String.class))).thenReturn(mockedDocumentReference);
        Mockito.when(mockedDocumentReference.set(any(Map.class), any(SetOptions.class)))
                .thenReturn(Tasks.forResult(null));

        preferenceActivity = new PreferenceActivity();
        // Act
        HashMap<String,Object> data1 = createNewUser("user1@ucsd.edu","user1","Gym");
        mockedFirestore.collection("testCollection").document("user1@ucsd.edu").set(data1);
        HashMap<String,Object> data2 = createNewUser("user2@ucsd.edu","user2","Jump");
        mockedFirestore.collection("testCollection").document("user2@ucsd.edu").set(data2);
        HashMap<String,Object> data3 = createNewUser("user3@ucsd.edu","user3","Gym");
        mockedFirestore.collection("testCollection").document("user3@ucsd.edu").set(data3);
        HashMap<String,Object> data4 = createNewUser("user4@ucsd.edu","user4","Running");
        mockedFirestore.collection("testCollection").document("user4@ucsd.edu").set(data4);
    }


    @Test
    public void testSimilarUsers()
    {
        HashMap<String, String> Filters = new HashMap<>();
        Filters.put("day","tue");
        Filters.put("exercise",null);
        Filters.put("location",null);
        Filters.put("gender",null);
        mockedFirestore.collection("testCollection")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> userDocuments = task.getResult().getDocuments();
                        List<HashMap<String, Object>> matches = Algorithm.findSimilarSchedulesForUser("user1@ucsd.edu", userDocuments, Filters);
                        assertEquals("user3", matches.get(0).get("name"));
                        assertEquals(12, matches.get(0).get("score"));

                    }
                });

    }

   public HashMap<String,Object> createNewUser(String email, String name,String exercise)
   {
       String location = "Rimac";
       String gender = "Male";
       String dob = "01/01/2000";
       String bio = "A bio";
       Map<String, Object> calendar = new HashMap<>();
       for (String day : new String[]{"mon", "tue", "wed", "thu", "fri"}) {
           ArrayList<Integer> hours = new ArrayList<Integer>();
           for (int i = 0; i < 24; i++) {
               if(i%2 == 0)
                   hours.add(1);
               else
                   hours.add(0);// Randomly assign 0 or 1 to each hour
           }
           calendar.put(day, hours);
       }
       HashMap<String, Object> data = new HashMap<>();
       data.put("name",name);
       data.put("email",email);
       data.put("exercise", exercise);
       data.put("location", location);
       data.put("gender", gender);
       data.put("dob", dob);
       data.put("bio", bio);
       data.put("calendar",calendar);
       return data;
   }
}
