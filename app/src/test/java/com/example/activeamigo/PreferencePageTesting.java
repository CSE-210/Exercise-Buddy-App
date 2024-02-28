package com.example.activeamigo;

import com.example.activeamigo.PreferenceActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class PreferencePageTesting {

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
//        DocumentReference mockedDocumentReference = Mockito.mock(DocumentReference.class);
//        Mockito.when(mockedCollectionReference.document("test@ucsd.edu")).thenReturn(mockedDocumentReference);
//        Mockito.when(mockedFirestore.collection("Accounts")).thenReturn(mockedCollectionReference);

        // Rest of your setup remains unchanged
        Mockito.when(mockedFirestore.collection(any(String.class))).thenReturn(mockedCollectionReference);
        Mockito.when(mockedCollectionReference.whereEqualTo(any(String.class), any(Object.class))).thenReturn(mockedQuery);
        Mockito.when(mockedCollectionReference.document(any(String.class))).thenReturn(mockedDocumentReference);
        Mockito.when(mockedDocumentReference.set(any(Map.class), any(SetOptions.class)))
                .thenReturn(Tasks.forResult(null));

        preferenceActivity = new PreferenceActivity();
    }
    @Test
    public void testFirestoreMock() {
        String exercise = "Gym";
        String location = "Rimac";
        String gender = "Male";
        String dob = "01/01/2000";
        String bio = "A bio";
        Map<String, Object> data = new HashMap<>();
        data.put("exercise", exercise);
        data.put("location", location);
        data.put("gender", gender);
        data.put("dob", dob);
        data.put("bio", bio);

        // Act
        mockedFirestore.collection("testCollection").document("testDocument").set(data);

        // Assert
        verify(mockedFirestore).collection("testCollection");
        verify(mockedCollectionReference).document("testDocument");
        verify(mockedDocumentReference).set(data);
    }

    @Test
    public void testPushNewData() {
        // Data
        String exercise = "Gym";
        String location = "Rimac";
        String gender = "Male";
        String dob = "01/01/2000";
        String bio = "A bio";
        Map<String, Object> data = new HashMap<>();
        data.put("exercise", exercise);
        data.put("location", location);
        data.put("gender", gender);
        data.put("dob", dob);
        data.put("bio", bio);

        //set db
        preferenceActivity.setDB(mockedFirestore);


        //Test pushNewData method -> pushing data to firestore
        preferenceActivity.pushNewData(exercise, location, gender, dob, bio);

        // Assert: Verify interactions with mocked instances
        verify(mockedFirestore).collection("Accounts");
        verify(mockedCollectionReference).document("test@ucsd.edu");
        verify(mockedDocumentReference).set(eq(data), any());

    }

}
