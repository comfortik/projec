package com.example.project.Settings;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.Emotion.EmotionDiaryFragment;
import com.example.project.Main.MainActivity;
import com.example.project.OnHideFragmentContainerListener;
import com.example.project.Profile.ProfileFragment;
import com.example.project.R;
import com.example.project.ReplaceFragment;
import com.example.project.Sounds.SoundFragment;
import com.example.project.databinding.FragmentSettingsBinding;
import com.google.android.material.navigation.NavigationBarView;

public class SettingsFragment extends Fragment  {

    private SettingsViewModel mViewModel;
    FragmentSettingsBinding binding;
    OnHideFragmentContainerListener onHideFragmentContainerListener;


    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        ReplaceFragment replaceFragment = new ReplaceFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        return view  ;
    }
    public void setOnHideFragmentContainerListener(OnHideFragmentContainerListener onHideFragmentContainerListener){
        this.onHideFragmentContainerListener = onHideFragmentContainerListener;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        // TODO: Use the ViewModel
    }

}