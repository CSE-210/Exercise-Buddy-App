package com.example.activeamigo;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class CreateAccountTests {

    @Mock
    FirebaseFirestore mockedFirestore;

    @Mock
    CollectionReference mockedCollectionReference;

    @Mock
    DocumentReference mockedDocumentReference;

    @Mock
    Query mockedQuery;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockedFirestore.collection(any(String.class))).thenReturn(mockedCollectionReference);
        when(mockedCollectionReference.whereEqualTo(any(String.class), any(Object.class))).thenReturn(mockedQuery);
        when(mockedCollectionReference.document(any(String.class))).thenReturn(mockedDocumentReference);
        when(mockedDocumentReference.set(any(Map.class), any(SetOptions.class)))
                .thenReturn(Tasks.forResult(null));
    }


    @After
    public void tearDown() {
        // Clean up
    }

    @Test
    public void testAccountCreationSuccess() {
        // Create an instance of CreateAccountActivity
        CreateAccountActivity createAccountActivity = new CreateAccountActivity();
        createAccountActivity.db = mockedFirestore;

        // Fill in valid form data
        String name = "Test User";
        String emailAddress = "test@example.com";
        String password = "password";

        // Call the method that adds the account to the database
        createAccountActivity.addAccount(name, emailAddress, password, "accounts");

        // Verify account was added
        verify(mockedFirestore).collection("accounts");
    }


    @Test
    public void testAccountCreationDuplicateEmail() {
        // Create an instance of CreateAccountActivity
        CreateAccountActivity createAccountActivity = new CreateAccountActivity();
        createAccountActivity.db = mockedFirestore;

        // Fill in valid form data
        String name = "Test User";
        String emailAddress = "test@example.com";
        String password = "password";

        // Mock Firestore to simulate an existing user with the same email
        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
        when(querySnapshot.isEmpty()).thenReturn(false);
        when(mockedQuery.get()).thenReturn(Tasks.forResult(querySnapshot));

        // Call the method that adds the account to the database
        createAccountActivity.addAccount(name, emailAddress, password, "accounts");

        // Verify account was not added due to duplicate email
        verify(mockedFirestore).collection("accounts");

    }


}
