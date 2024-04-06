package com.example.project.Profile;

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
import com.example.project.Main.MainActivity;
import com.example.project.OnHideFragmentContainerListener;
import com.example.project.R;
import com.example.project.ReplaceFragment;
import com.example.project.Settings.SettingsFragment;
import com.example.project.Sounds.SoundFragment;

import com.example.project.databinding.FragmentProfileBinding;
import com.google.android.material.navigation.NavigationBarView;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private OnHideFragmentContainerListener hideFragmentContainerListener;
    FragmentProfileBinding binding;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.bottomNavigationView.setSelectedItemId(R.id.bottom_profile);
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
                    ((OnHideFragmentContainerListener)getActivity()).onButtonTimerClick();
                    getActivity().getSupportFragmentManager().beginTransaction().remove(ProfileFragment.this).commit();
                } else if (menuItem.getItemId() == R.id.bottom_sound) {
                    replaceFragment.replaceFragment(fragmentManager,new SoundFragment());
                } else if (menuItem.getItemId() == R.id.bottom_profile) {
                    replaceFragment.replaceFragment(fragmentManager, new ProfileFragment());
                }


                return false;
            }
        });
        return view;
    }

    public void setOnHideFragmentContainerListener(OnHideFragmentContainerListener listener) {
        this.hideFragmentContainerListener = listener;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}