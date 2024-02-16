package com.example.activeamigo;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.*;
public class Algorithm {
    public static void main(String[] args) {
        File file = new File("/Users/christine/Desktop/CSE210/mock/src/com/company/sample1_1.json");
        ObjectMapper objectMapper = new ObjectMapper();

//        Map<String, List<Object>> data = objectMapper.readValue(file, Map.class);
        Map<String, List<Object>> data = new HashMap<>();
        try {
            data = objectMapper.readValue(file, Map.class);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception (e.g., log it, throw a runtime exception, etc.)
        }

        List<String> users = new ArrayList<>(data.keySet());
//        for (String user : users) {
//            System.out.println(user);
//        }
//
        String user1 = "wafybtvgupqefg";
        List<List<Object>> similarUsers = Algorithm.findSimilarSchedulesForUser(user1, users, data);

        for (List<Object> user : similarUsers) {
            System.out.println( "schedule: " + data.get(user.get(1)) );
            System.out.println("User: " + user.get(1) + ", Score: " + user.get(0));
        }

    }

    public static List<List<Object>> findSimilarSchedulesForUser(String user1, List<String> users, Map<String, List<Object>> data) {
        List<Integer> user1Schedule = (List<Integer>) data.get(user1).get(1);
        int user1Day = (int) data.get(user1).get(0);
        Map<String, Integer> score = new HashMap<>();

        for (String u : users) {
            if (!data.containsKey(u)) continue;
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
}
