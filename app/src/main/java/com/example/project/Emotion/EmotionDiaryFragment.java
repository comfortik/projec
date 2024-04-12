package com.example.project.Emotion;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.Main.FirestoreGetId;
import com.example.project.OnHideFragmentContainerListener;
import com.example.project.OnNote;
import com.example.project.Profile.ProfileFragment;
import com.example.project.R;
import com.example.project.ReplaceFragment;
import com.example.project.Settings.SettingsFragment;
import com.example.project.Sounds.SoundFragment;
import com.example.project.databinding.FragmentEmotionDiaryBinding;
import com.google.android.material.navigation.NavigationBarView;
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
    AlertDialog.Builder builder;

    ReactForEmotions reactForEmotions;
    FirestoreEmotion firestoreEmotion;
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
        FirestoreGetId firestoreGetId = new FirestoreGetId(fb);
        firestoreEmotion = new FirestoreEmotion(getActivity(), fb, user);
        EmotionUtils emotionUtils = new EmotionUtils(firestoreEmotion, reactForEmotions);
        emotionUtils.setListeners(getActivity(), binding);
        emotionUtils.setOnNote(new OnNote() {
            @Override
            public void onNote(Emotion emotion) {
                emotionUtils.alertNote(getActivity(), getActivity().getLayoutInflater(), fb, mAuth, null, emotion);
            }
        });
        emotionUtils.setOnCloseDialogEmotionListener(new OnCloseDialogEmotionListener() {
            @Override
            public void onHideDialog(Emotion emotion) {
                emotionUtils.alertEmotion(fb, mAuth, null, emotion);
            }
        });



        return view ;
    }





//    public void setListeners(){
//        binding.imgPloho.setOnClickListener(v -> {
//            Emotion emotion= new Emotion(1);
//            emotionBtns(emotion);
//        });
//        binding.imgTakoe.setOnClickListener(v -> {
//            Emotion emotion= new Emotion(2);
//            emotionBtns(emotion);
//        });
//        binding.imgNorm.setOnClickListener(v -> {
//            Emotion emotion= new Emotion(3);
//            emotionBtns(emotion);
//        });
//        binding.imgWow.setOnClickListener(v -> {
//            Emotion emotion= new Emotion(4);
//            emotionBtns(emotion);
//        });
//        binding.imgAhuenno.setOnClickListener(v -> {
//            Emotion emotion= new Emotion(5);
//            emotionBtns(emotion);
//        });
//    }
//    public void emotionBtns(Emotion emotion){
////        addEmotion();
//        if(emotion!=null) Toast.makeText(getActivity(), "aaaa", Toast.LENGTH_SHORT);
//        firestoreEmotion.addEmotion(emotion);
//        int emotionId = emotion.getId();
//        switch (emotionId){
//            case 1:
//                AlertDialogEmotionDiary(reactForEmotions.emotionOne());
//                break;
//            case 2:
//                AlertDialogEmotionDiary(reactForEmotions.emotionTwo());
//                break;
//            case 3:
//                AlertDialogEmotionDiary(reactForEmotions.emotionThree());
//                break;
//            case 4:
//                AlertDialogEmotionDiary(reactForEmotions.emotionFour());
//                break;
//            case 5:
//                AlertDialogEmotionDiary(reactForEmotions.emotionFive());
//                break;
//        }
//    }
//    private void AlertDialogEmotionDiary(AddUserToFirebase.HelpEmotion emotion){
//        builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle(emotion.getHelp())
//                .setMessage("Хочешь оставить заметку?")
//                .setCancelable(true)
//                .setPositiveButton("Да", (dialog, which) -> {
//                    //активити с записями (чел оставляет запись, сохраняется она и эмоция с причинами , если была фокусировка, то ее параметры)
//                })
//                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //активити с записями (сохраняется эмоция+ причины+ фокусировка, если была)
//                        dialog.cancel();
//                    }
//                })
//                .show();
//    }
    public interface OnIdReturnedListener {
        void onIdReturned(String userId);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EmotionDiaryViewModel.class);
        // TODO: Use the ViewModel
    }
    public void setOnHideFragmentContainerListener(OnHideFragmentContainerListener onHideFragmentContainerListener){
        this.onHideFragmentContainerListener= onHideFragmentContainerListener;
    }



}