package com.example.activeamigo.ui.preferences;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.activeamigo.PreferenceActivity;
import com.example.activeamigo.databinding.FragmentPreferencesBinding;

public class PreferencesPage extends Fragment {

    private FragmentPreferencesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PreferencesViewModel preferencesViewModel =
                new ViewModelProvider(this).get(PreferencesViewModel.class);

        binding = FragmentPreferencesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textPreferences;
//        preferencesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        startActivity(new Intent(getActivity(), PreferenceActivity.class));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}