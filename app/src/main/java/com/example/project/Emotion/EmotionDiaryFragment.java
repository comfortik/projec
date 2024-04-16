package com.example.project.Emotion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.project.OnHideFragmentContainerListener;
import com.example.project.databinding.FragmentEmotionDiaryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class EmotionDiaryFragment extends Fragment {

    private EmotionDiaryViewModel mViewModel;
    OnHideFragmentContainerListener onHideFragmentContainerListener;
    FragmentEmotionDiaryBinding binding;
    FirebaseFirestore fb;
    FirebaseAuth mAuth;
    FirebaseUser user;

    ReactForEmotions reactForEmotions;

    public static EmotionDiaryFragment newInstance() {
        return new EmotionDiaryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEmotionDiaryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        fb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        reactForEmotions = new ReactForEmotions();
        EmotionUtils emotionUtils = new EmotionUtils( reactForEmotions);
        emotionUtils.setListeners(getActivity(), binding);
        emotionUtils.setOnNote(emotion -> emotionUtils.alertNote(getActivity(), getActivity().getLayoutInflater(), fb, mAuth, null, emotion));
        emotionUtils.setOnCloseDialogEmotionListener(emotion -> emotionUtils.alertEmotion(fb, mAuth, null, emotion));



        return view ;
    }

    public interface OnIdReturnedListener {
        void onIdReturned(String userId);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EmotionDiaryViewModel.class);
        // TODO: Use the ViewModel
    }



}