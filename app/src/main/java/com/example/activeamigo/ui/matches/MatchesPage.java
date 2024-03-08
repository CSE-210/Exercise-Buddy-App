package com.example.activeamigo.ui.matches;
//package com.example.activeamigo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

//import com.example.activeamigo.ui.matches.R;
import com.example.activeamigo.Algorithm;
import com.example.activeamigo.R;
import com.example.activeamigo.databinding.FragmentMatchesBinding;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.util.Log;

import android.content.Intent;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;

public class MatchesPage extends Fragment {
    protected FirebaseAuth auth;
    private FragmentMatchesBinding binding;
    private ListView matchesList;
    HashMap<String, String[]> matchedUsers = new HashMap<String, String[]>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MatchesViewModel matchesViewModel =
                new ViewModelProvider(this).get(MatchesViewModel.class);

        binding = FragmentMatchesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        auth = FirebaseAuth.getInstance();
        // Filter Button Interactions
        Button filterButton = root.findViewById(R.id.filterButton);
        LinearLayout filtersLayout = root.findViewById(R.id.filtersLayout);
        filtersLayout.setBackgroundColor(Color.WHITE);

        filterButton.setOnClickListener(v -> {
            if (filtersLayout.getVisibility() == View.GONE) {
                filtersLayout.setVisibility(View.VISIBLE);
            } else {
                filtersLayout.setVisibility(View.GONE);
            }
        });
        String[] locations= getResources().getStringArray(R.array.location_array);
        setUpSpinner(root.findViewById(R.id.locationSpinner), locations);
        String[] exercises= getResources().getStringArray(R.array.activity_array);
        setUpSpinner(root.findViewById(R.id.exerciseTypeSpinner), exercises);
        String[] gender= getResources().getStringArray(R.array.gender_array);
        setUpSpinner(root.findViewById(R.id.genderSpinner), gender);
        String[] days= getResources().getStringArray(R.array.day_array);
        setUpSpinner(root.findViewById(R.id.daySpinner), days);

//
//        final TextView textView = binding.textMatches;
//        matchesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        matchesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        //Loading the Matches List dynamically after calling the ALgo
        matchesViewModel.getMyListLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                matchesList = root.findViewById(R.id.matchesList);
                ArrayAdapter<String> matchesAdaptor = new ArrayAdapter<>(
                        getActivity(),
                        android.R.layout.simple_list_item_1,
                        strings
                );
                matchesList.setAdapter(matchesAdaptor);


                // Setting the click listener for each item in the list
                matchesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Get the clicked item's data
                        String selectedItem = strings.get(position);

                        // Create an Intent to start DetailActivity
                        Intent intent = new Intent(getActivity(), DetailActivity.class);

                        // Pass data to DetailActivity
                        intent.putExtra("SELECTED_ITEM", selectedItem);

                        String[] userDetails = matchedUsers.get(selectedItem);
                        String bio = userDetails[0]; // Access the bio
                        String exercise = userDetails[1]; // Access the exercise
                        String location = userDetails[2]; // Access the location
                        String email = userDetails[3]; // Access the email
                        intent.putExtra("BIO_KEY", bio); // Assuming bio is a String variable
                        intent.putExtra("EXERCISE_KEY", exercise); // Assuming exercise is not already a String
                        intent.putExtra("LOCATION_KEY", location); // Assuming location is not already a String
                        intent.putExtra("EMAIL_KEY", email); // Assuming email is not already a String
                        // Start DetailActivity
                        startActivity(intent);
                    }
                });

            }

            ;
//
        });



        Algorithm algorithm = new Algorithm();
//DataPopulator.populateDummyData();
// Call the method
        HashMap<String, String> Filters = new HashMap<>();
        Filters.put("day", "tue");
        Filters.put("exercise", null);
        Filters.put("location", null);
        Filters.put("gender", null);
// Create an instance of the Algorithm class
        algorithm = new Algorithm();


// Call fetchUserDocumentsAndProcess method for a user
        FirebaseUser user = auth.getCurrentUser();
        algorithm.fetchUserDocumentsAndProcess(user.getEmail(), Filters,"Accounts", new Algorithm.OnFetchCompleteListener() {
            @Override
            public void onFetchComplete(List<HashMap<String, Object>> matches) {

                for (HashMap<String, Object> match : matches) {
                    matchedUsers.put(match.get("name").toString(),new String[] {match.get("bio").toString(),match.get("exercise").toString(),match.get("location").toString(),match.get("email").toString()});
                    //Log.d("Test", "User: " + match.get("name") + ", Score: " + match.get("score"));
                }
                ArrayList<String> matchedUsersNames = new ArrayList<String>();
                matchedUsersNames.addAll(matchedUsers.keySet());
                matchesViewModel.setList(matchedUsersNames);
            }

            @Override
            public void onFetchError(Exception e) {
                // Handle errors here
                Log.e("Test", "Error fetching user documents", e);
            }
        });

        return root;
    }
    private void setUpSpinner(Spinner spinner, String[] options) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
