package com.example.activeamigo.ui.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.activeamigo.CalendarActivity;
import com.example.activeamigo.MainActivity;
import com.example.activeamigo.R;
import com.example.activeamigo.databinding.FragmentCalendarBinding;

public class CalendarPage extends Fragment {

    private FragmentCalendarBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Intent intent = new Intent(getActivity(), CalendarActivity.class);

        CalendarViewModel calendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);

        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        startActivity(intent);
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_matches, container, false);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}