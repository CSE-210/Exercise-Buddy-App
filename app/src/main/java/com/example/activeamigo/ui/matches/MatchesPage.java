package com.example.activeamigo.ui.matches;
//package com.example.activeamigo;
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

public class MatchesPage extends Fragment {

    private FragmentMatchesBinding binding;
    private ListView matchesList;
    HashMap<String, String[]> matchedUsers = new HashMap<String, String[]>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MatchesViewModel matchesViewModel =
                new ViewModelProvider(this).get(MatchesViewModel.class);

        binding = FragmentMatchesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Filter Button Interactions
        Button filterButton = root.findViewById(R.id.filterButton);
        LinearLayout filtersLayout = root.findViewById(R.id.filtersLayout);

        filterButton.setOnClickListener(v -> {
            if (filtersLayout.getVisibility() == View.GONE) {
                filtersLayout.setVisibility(View.VISIBLE);
            } else {
                filtersLayout.setVisibility(View.GONE);
            }
        });

        setUpSpinner(root.findViewById(R.id.locationSpinner), new String[]{"Location 1", "Location 2"});
        setUpSpinner(root.findViewById(R.id.exerciseTypeSpinner), new String[]{"Yoga", "Running"});
        setUpSpinner(root.findViewById(R.id.genderSpinner), new String[]{"Male", "Female", "Other"});
        setUpSpinner(root.findViewById(R.id.daySpinner), new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"});

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
        algorithm.fetchUserDocumentsAndProcess("affxe97@ucsd.edu", Filters,"mockedAccounts", new Algorithm.OnFetchCompleteListener() {
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
