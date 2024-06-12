package com.example.project.Emotion;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.R;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.project.OnHideFragmentContainerListener;
import com.example.project.databinding.FragmentEmotionDiaryBinding;
import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class EmotionDiaryFragment extends Fragment {

    private static final String TAG = "EmotionDiaryFragment";
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



        EmotionUtils emotionUtils = new EmotionUtils(reactForEmotions);
        emotionUtils.setListeners(getActivity(), binding);
        emotionUtils.setOnNote(emotion ->
                emotionUtils.alertNote(getActivity(), getActivity().getLayoutInflater(), fb, mAuth, null, emotion));
        emotionUtils.setOnCloseDialogEmotionListener(emotion ->
                emotionUtils.alertEmotion(fb, mAuth, null, emotion));

        return view;
    }

    public interface OnIdReturnedListener {
        void onIdReturned(String userId);
    }
    private static final String[] words = {
            "Депрессивный", "Угнетенный", "Опустошенный", "Разочарованный", "Безнадежный",
            "Отчаявшийся", "Тоскливый", "Меланхоличный", "Печальный", "Несчастный",
            "Раздраженный", "Злой", "Гневный", "Разозленный", "В отчаянии",
            "Обескураженный", "Разочарованный", "Удрученный", "Загнанный в угол", "Подавленный"
    };

    public void setButtons(String [] words){
        if (binding.flexbox.getChildCount() == 0){
            for (String word : words) {
                Context context = new ContextThemeWrapper(getContext(), R.style.btn);
                MaterialButton button = new MaterialButton(context);
                button.setText(word);
                button.setAllCaps(false);
                button.setTextColor(Color.WHITE);

                FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 12, 0);
                button.setLayoutParams(params);
                button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                button.setOnClickListener(v -> {
                    Log.d(TAG, "Button clicked: " + word);
                });
                binding.flexbox.addView(button);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EmotionDiaryViewModel.class);
        // TODO: Use the ViewModel
    }
}
