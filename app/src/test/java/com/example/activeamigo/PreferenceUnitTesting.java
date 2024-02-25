package com.example.activeamigo;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Handler;
import android.os.Looper;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class PreferenceUnitTesting {

    // Creates a mocked database
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


    // Sets up the mocked database before the test runs
    @Before
    public void setUp() {
        // Opens Mockito
        MockitoAnnotations.openMocks(this);
        // Creates a firebase
        when(mockedFirestore.collection(any(String.class))).thenReturn(mockedCollectionReference);
        // Creates a collection reference
        when(mockedCollectionReference.whereEqualTo(any(String.class), any(Object.class))).thenReturn(mockedQuery);
        when(mockedCollectionReference.document(any(String.class))).thenReturn(mockedDocumentReference);
        // In case of null error
        when(mockedDocumentReference.set(any(Map.class), any(SetOptions.class)))
                .thenReturn(Tasks.forResult(null));
    }

    // Cleans up after test
    @After
    public void tearDown() {
        // Clean up
    }

    @Test
    public void testAccountCreationSuccess() {
        // Create an instance of CreateAccountActivity
        PreferenceActivity preferenceActivity = new PreferenceActivity();
        preferenceActivity.db = mockedFirestore;

        // Mock Firestore DocumentReference
        DocumentReference mockDocRef = Mockito.mock(DocumentReference.class);
        when(mockedFirestore.collection("Accounts").document("test@ucsd.edu")).thenReturn(mockedDocumentReference);

        // Mock the behavior of DocumentReference.set(Object) to return a non-null Task
        doAnswer(invocation -> {
            // Simulate the completion of the task after a short delay
            Task<Void> task = Tasks.forResult(null);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                ((OnCompleteListener<Void>) invocation.getArgument(0)).onComplete(task);
            }, 100); // Adjust the delay as needed
            return task;
        }).when(mockDocRef).set(any(Map.class), any(SetOptions.class));
//        preferenceActivity.collection = "Accounts";
//        preferenceActivity.document = "be@ucsd.edu";

        // Fill in valid form data
        String exe = "Gym";
        String loc = "Rimac";
        String gen = "Male";
        String date = "01/01/1990";
        String bioText = "I love running";

        // Set mock data in UI
        preferenceActivity.pushNewData(mockedDocumentReference, exe, loc, gen, date, bioText);

        // Verify that the Firestore collection and document were accessed correctly
        verify(mockedFirestore).collection("Accounts");
        verify(mockedFirestore.collection("Accounts")).document("be@ucsd.edu");
    }


}
