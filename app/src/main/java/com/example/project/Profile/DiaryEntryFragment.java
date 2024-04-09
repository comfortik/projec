package com.example.project.Profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.Main.DiaryEntry;
import com.example.project.R;
import com.example.project.databinding.FragmentDiaryEntryBinding;

public class DiaryEntryFragment extends Fragment {

    private DiaryEntryViewModel mViewModel;
    DiaryEntry diaryEntry;
    FragmentDiaryEntryBinding binding;
    public static DiaryEntryFragment newInstance(DiaryEntry diaryEntry) {
        return new DiaryEntryFragment(diaryEntry);
    }
    public DiaryEntryFragment(DiaryEntry diaryEntry){
        this.diaryEntry=diaryEntry;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= FragmentDiaryEntryBinding.inflate(inflater, null, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DiaryEntryViewModel.class);
        // TODO: Use the ViewModel
    }

}