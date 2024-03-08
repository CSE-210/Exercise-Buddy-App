package com.example.activeamigo.ui.matches;
//package com.example.activeamigo;

import android.graphics.Color;
import static android.content.ContentValues.TAG;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.util.Log;

import android.content.Intent;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;


public class MatchesPage extends Fragment {
    protected FirebaseAuth auth;
    private FragmentMatchesBinding binding;
    private ListView matchesList;
    protected HashMap<String, String[]> matchedUsers = new HashMap<String, String[]>();
    protected Algorithm algorithm;
    protected MatchesViewModel matchesViewModel;
    protected HashMap<String, String> Filters;
    protected Spinner exerciseSpinner;
    protected Spinner locationSpinner;
    protected Spinner genderSpinner;
    protected Spinner daySpinner;
//    HashMap<String,String> user1 = new HashMap<String, String>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        matchesViewModel = new ViewModelProvider(this).get(MatchesViewModel.class);
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

        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Check if the touch event is outside of the filtersLayout
                    if (filtersLayout.getVisibility() == View.VISIBLE) {
                        Rect outRect = new Rect();
                        filtersLayout.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            filtersLayout.setVisibility(View.GONE);
                            return true; // Return true to indicate the touch event is consumed
                        }
                    }
                }
                return false; // Return false to let other touch events be handled normally
            }
        });


        exerciseSpinner = root.findViewById(R.id.exerciseTypeSpinner);
        locationSpinner = root.findViewById(R.id.locationSpinner);
        genderSpinner = root.findViewById(R.id.genderSpinner);
        daySpinner = root.findViewById(R.id.daySpinner);

        String[] locations= getResources().getStringArray(R.array.location_array);
        setUpSpinner(locationSpinner, locations,null);

        String[] exercises= getResources().getStringArray(R.array.activity_array);
        setUpSpinner(exerciseSpinner, exercises,null);

        String[] gender= getResources().getStringArray(R.array.gender_array);
        setUpSpinner(genderSpinner, gender,null);

        String[] days= getResources().getStringArray(R.array.day_array);
        setUpSpinner(daySpinner, days,null);

        Filters = new HashMap<>();
        initializeDefaultFilters();
        algorithm = new Algorithm();
        fetchMatches();
//        Log.d("CurrentUserData",user1.toString());
        // Set up listeners
        setupSpinnerListener(exerciseSpinner, "exercise");
        setupSpinnerListener(locationSpinner, "location");
        setupSpinnerListener(genderSpinner, "gender");
        setupSpinnerListener(daySpinner, "day");

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
        });


        return root;
    }

    public void setUpSpinner(Spinner spinner, String[] options, String user1Data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if(user1Data!=null) {
            Integer i = 0;
            for (String s : options) {
                if (s.equals(user1Data))
                    break;
                i += 1;
            }
            Log.d("index", Integer.toString(i));
            spinner.setSelection(i);
        }
        else{
            spinner.setSelection(1);
        }
    }

    public void onResume() {
        super.onResume();
        fetchMatches();
    }

    public void fetchMatches() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail();
            if (userEmail != null) {
                algorithm.fetchUserDocumentsAndProcess(userEmail, Filters, "Accounts", new Algorithm.OnFetchCompleteListener() {
                    @Override
                    public void onFetchComplete(List<HashMap<String, Object>> matches, HashMap<String, String> user1) {
                        ArrayList<String> matchNames = new ArrayList<>();
//                        user1=currentUserData;
                        Log.d("Filters",Filters.toString());
                        if(Filters.get("location")==null) {
                            String[] locations = getResources().getStringArray(R.array.location_array);
                            setUpSpinner(locationSpinner, locations, user1.get("location"));
                        }
                        if(Filters.get("exercise")==null) {
                            String[] exercises = getResources().getStringArray(R.array.activity_array);
                            setUpSpinner(exerciseSpinner, exercises, user1.get("exercise"));
                        }
                        if(Filters.get("gender")==null) {
                            String[] gender = getResources().getStringArray(R.array.gender_array);
                            setUpSpinner(genderSpinner, gender, user1.get("gender"));
                        }
                        if(Filters.get("day")==null){
                            String[] days= getResources().getStringArray(R.array.day_array);
                            LocalDate currentDate = LocalDate.now();
                            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
                            String u1Day=dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toLowerCase();
                            setUpSpinner(daySpinner, days,u1Day);
                        }
                        for (HashMap<String, Object> match : matches) {
                            String name = (String) match.get("name");
                            matchNames.add(name);
                            matchedUsers.put(match.get("name").toString(),new String[] {match.get("bio").toString(),match.get("exercise").toString(),match.get("location").toString(),match.get("email").toString()});
                            //Log.d("Test", "User: " + match.get("name") + ", Score: " + match.get("score"));
                        }
                        ArrayList<String> matchedUsersNames = new ArrayList<String>();
                        matchedUsersNames.addAll(matchedUsers.keySet());
                        matchesViewModel.setList(matchNames);
                    }
                    @Override
                    public void onFetchError(Exception e) {
                        Log.e(TAG, "Error fetching matches: ", e);
                    }
                });
            }
        }
    }

    public void initializeDefaultFilters() {
        String selectedDay = daySpinner.getSelectedItem().toString();
        String selectedExercise = exerciseSpinner.getSelectedItem().toString();
        String selectedLocation = locationSpinner.getSelectedItem().toString();
        String selectedGender = genderSpinner.getSelectedItem().toString();

        Filters.put("day", null);
        Filters.put("exercise", null);
        Filters.put("location", null);
        Filters.put("gender", null);
    }

    public void setupSpinnerListener(Spinner spinner, final String filterKey) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                Filters.put(filterKey, selected);
                fetchMatches();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
