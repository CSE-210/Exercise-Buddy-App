package com.example.activeamigo.ui.matches;
//package com.example.activeamigo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.activeamigo.Algorithm;
import com.example.activeamigo.MatchesActivity;
//import com.example.activeamigo.ui.matches.R;
import com.example.activeamigo.R;
import com.example.activeamigo.databinding.FragmentMatchesBinding;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MatchesPage extends Fragment {

    private FragmentMatchesBinding binding;
    private ListView matchesList;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MatchesViewModel matchesViewModel =
                new ViewModelProvider(this).get(MatchesViewModel.class);

        binding = FragmentMatchesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
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
        setUpSpinner(root.findViewById(R.id.daySpinner), new String[]{"Monday", "Tuesday", "Wednesday","Thursday", "Friday"});

//
//        final TextView textView = binding.textMatches;
//        matchesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        matchesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

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
            };
//
        });
        //Need to get the current user i.e. user1 info from database
        //Need to get all data map and users list from the database
        String user1 = "wafybtvgupqefg";
        File file = new File("/Users/loukiknaik/Downloads/sample1_1.json");
        ObjectMapper objectMapper = new ObjectMapper();

//        Map<String, List<Object>> data = objectMapper.readValue(file, Map.class);
        Map<String, List<Object>> data = new HashMap<>();
        try {
            data = objectMapper.readValue(file, Map.class);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception (e.g., log it, throw a runtime exception, etc.)
        }
        List<Object> objectList = new ArrayList<>(
                Arrays.asList(1, Arrays.asList(5,8,9,11,12,15,16,17,21))
        );
        data.put("wafybtvgupqefg",objectList);
        List<Object> objectList1 = new ArrayList<>(
                Arrays.asList(6, Arrays.asList(2, 4, 10, 14, 20, 22))
        );
        data.put("keyxtaamnivy",objectList1);
        List<Object> objectList2 = new ArrayList<>(
                Arrays.asList(1, Arrays.asList(5,8,9,11,12,15,17,21))
        );
        data.put("lafybtvgupfg",objectList2);

        List<String> users = new ArrayList<>(data.keySet());
        ArrayList<String> matches = findMatches(user1,users,data);
        matchesViewModel.setList(matches);
        return root;
    }
    private void setUpSpinner(Spinner spinner, String[] options) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    public ArrayList<String> findMatches(String user1, List<String> users, Map<String, List<Object>> data){
        ArrayList<String> rankedUsers = new ArrayList<String>();
        Log.d("Start","Start userList creation");
        List<List<Object>> userList = findSimilarSchedulesForUser(user1,users,data);
        Log.d("Success","Passed userList creation");
        int j=20;
        for(List<Object> u: userList){
            String s = u.get(1).toString();
            System.out.println(s);
            rankedUsers.add(s);
            j-=1;
            if(j==0){
                break;
            }
        }
        for(int i=0;i<100;i++){
            rankedUsers.add("Rahul");
            System.out.println("Rahul");
        }
        return rankedUsers;
    }
    public List<List<Object>> findSimilarSchedulesForUser(String user1, List<String> users, Map<String, List<Object>> data) {
        Log.d("Start uswe List", "ejnf");
        Log.d((data).toString(),"Data log");
        List<Integer> user1Schedule = (List<Integer>) data.get(user1).get(1);
        Log.d(user1Schedule.toString(),"user 1 schedule");
        int user1Day = (int) data.get(user1).get(0);
        Map<String, Integer> score = new HashMap<>();

        for (String u : users) {
            if (!data.containsKey(u)|| data.get(u) == null || data.get(u).size() < 2) continue;
            int i = 0, j = 0;
            if (user1Day != (int) data.get(u).get(0)) continue;

            List<Integer> uSchedule = (List<Integer>) data.get(u).get(1);
            int currScore = 0;
            while (i < user1Schedule.size() && j < uSchedule.size()) {
                if (user1Schedule.get(i).equals(uSchedule.get(j))) {
                    currScore++;
                    i++;
                    j++;
                } else if (user1Schedule.get(i) < uSchedule.get(j)) {
                    i++;
                } else {
                    j++;
                }
            }
            score.put(u, currScore);
        }

        List<List<Object>> similarUserList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : score.entrySet()) {
            similarUserList.add(Arrays.asList(entry.getValue(), entry.getKey()));
        }
        similarUserList.sort((a, b) -> (int) b.get(0) - (int) a.get(0));
        return similarUserList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}