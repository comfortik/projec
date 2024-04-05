package com.example.project.Sounds;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.Emotion.EmotionDiaryFragment;
import com.example.project.OnHideFragmentContainerListener;
import com.example.project.Profile.ProfileFragment;
import com.example.project.R;
import com.example.project.ReplaceFragment;
import com.example.project.Settings.SettingsFragment;
import com.example.project.databinding.FragmentSoundBinding;
import com.google.android.material.navigation.NavigationBarView;

public class SoundFragment extends Fragment {

    private SoundViewModel mViewModel;
    FragmentSoundBinding binding;
    private OnHideFragmentContainerListener hideFragmentContainerListener;

    public static SoundFragment newInstance() {
        return new SoundFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSoundBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.bottomNavigationView.setSelectedItemId(R.id.bottom_sound);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        ReplaceFragment replaceFragment = new ReplaceFragment();
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.bottom_settings) {
                    replaceFragment.replaceFragment(fragmentManager,new SettingsFragment());
                } else if (menuItem.getItemId() == R.id.bottom_emotionDiary) {
                    replaceFragment.replaceFragment(fragmentManager,new EmotionDiaryFragment());
                } else if (menuItem.getItemId() == R.id.bottom_timer) {
                    fragmentManager.beginTransaction().remove(SoundFragment.this).commit();
                    ((OnHideFragmentContainerListener)getActivity()).onButtonTimerClick();
                } else if (menuItem.getItemId() == R.id.bottom_sound) {
                    replaceFragment.replaceFragment(fragmentManager,new SoundFragment());
                } else if (menuItem.getItemId() == R.id.bottom_profile) {
                    replaceFragment.replaceFragment(fragmentManager, new ProfileFragment());
                }


                return false;
            }
        });
        return view ;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SoundViewModel.class);
        // TODO: Use the ViewModel
    }
    public void setOnHideFragmentContainerListener(OnHideFragmentContainerListener listener) {
        this.hideFragmentContainerListener = listener;
    }

}