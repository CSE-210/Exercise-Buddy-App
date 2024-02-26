package com.example.activeamigo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
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

import java.util.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class CalendarActivityTests {

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
    public void testDisplayCalendar() {
        CalendarActivity calendarActivity = new CalendarActivity();
        calendarActivity.db = mockedFirestore;

        // Mock Firestore DocumentReference
        String userEmail = "mj@ucsd.edu";
        DocumentReference mockDocRef = Mockito.mock(DocumentReference.class);
        when(mockedFirestore.collection("Accounts").document("mj@ucsd.edu")).thenReturn(mockDocRef);

        // Mock Firestore documentSnapshot
        DocumentSnapshot mockDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);
        when(mockDocumentSnapshot.exists()).thenReturn(true);
        when(mockDocumentSnapshot.getData()).thenReturn(getMockCalendarData()); // You can customize this based on your needs

        // Mock Firestore get() method
        when(mockDocRef.get()).thenReturn(Tasks.forResult(mockDocumentSnapshot));

        // Perform the test
        calendarActivity.displayCalendar(userEmail);
    }

    private Map<String, Object> getMockCalendarData() {
        // Customize this based on your calendar data structure
        Map<String, Object> mockData = new HashMap<>();
        Map<String, List<Long>> mockCalendarMap = new HashMap<>();
        List<Long> mockAvailabilityList = Arrays.asList(1L, 0L, 1L, 0L); // Example availability list
        mockCalendarMap.put("Mon", mockAvailabilityList); // Example day
        mockData.put("calendar", mockCalendarMap);
        return mockData;
    }

    @Test
    public void testUpdateCalendar() {
        CalendarActivity calendarActivity = new CalendarActivity();
        calendarActivity.db = mockedFirestore;

        // Mock Firestore DocumentReference
        String userEmail = "mj@ucsd.edu";
        DocumentReference mockDocRef = Mockito.mock(DocumentReference.class);
        when(mockedFirestore.collection("Accounts").document(userEmail)).thenReturn(mockDocRef);

        // Mock Firestore documentSnapshot
        DocumentSnapshot mockDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);
        when(mockDocumentSnapshot.exists()).thenReturn(true);
        when(mockDocumentSnapshot.getData()).thenReturn(null); // You can customize this based on your needs

        // Mock Firestore get() method
        when(mockDocRef.get()).thenReturn(Tasks.forResult(mockDocumentSnapshot));

        // Perform the test
        calendarActivity.updateCalendar(true, "12:00",  userEmail);


        // Verify that the get() method was called on the DocumentReference
        verify(mockDocRef).get();

        // Verify that the update() method was called on the DocumentReference
        // Note: This assumes that your updateCalendar method calls update on docRef
//        verify(mockDocRef).update(eq("calendar"), anyMap());

        // Alternatively, if you have a specific expected map, you can use:
        // verify(mockDocRef).update(eq("calendar"), eq(expectedMap));
    }
}
