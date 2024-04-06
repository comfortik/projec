package com.example.project.Emotion;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.example.project.Main.AddUserToFirebase;
import com.example.project.Main.MainActivity;
import com.example.project.databinding.ActivityEmotionDiaryBinding;
import com.example.project.databinding.FragmentEmotionDiaryBinding;

public class EmotionUtils {
     FirestoreEmotion firestoreEmotion;
     ReactForEmotions reactForEmotions;
    private OnCloseDialogEmotionListener onCloseDialogEmotionListener;

    public void setOnCloseDialogEmotionListener(OnCloseDialogEmotionListener listener) {
        this.onCloseDialogEmotionListener = listener;
    }

    public EmotionUtils(FirestoreEmotion firestoreEmotion, ReactForEmotions reactForEmotions) {
        this.firestoreEmotion = firestoreEmotion;
        this.reactForEmotions = reactForEmotions;
    }


    public  void setListeners(Context context, FragmentEmotionDiaryBinding binding) {
        binding.imgPloho.setOnClickListener(v -> {
            Emotion emotion = new Emotion(1);
            emotionBtns(context, binding, emotion);
        });
        binding.imgTakoe.setOnClickListener(v -> {
            Emotion emotion= new Emotion(2);
            emotionBtns(context, binding, emotion);
        });
        binding.imgNorm.setOnClickListener(v -> {
            Emotion emotion= new Emotion(3);
            emotionBtns(context, binding, emotion);
        });
        binding.imgWow.setOnClickListener(v -> {
            Emotion emotion= new Emotion(4);
            emotionBtns(context, binding, emotion);
        });
        binding.imgAhuenno.setOnClickListener(v -> {
            Emotion emotion= new Emotion(5);
            emotionBtns(context, binding, emotion);
        });
    }

    public  void emotionBtns(Context context, FragmentEmotionDiaryBinding binding, Emotion emotion) {

        firestoreEmotion.addEmotion(emotion);
        int emotionId = emotion.getId();
        switch (emotionId){
            case 1:
                AlertDialogEmotionDiary(context, reactForEmotions.emotionOne());
                break;
            case 2:
                AlertDialogEmotionDiary(context, reactForEmotions.emotionTwo());
                break;
            case 3:
                AlertDialogEmotionDiary(context, reactForEmotions.emotionThree());
                break;
            case 4:
                AlertDialogEmotionDiary(context, reactForEmotions.emotionFour());
                break;
            case 5:
                AlertDialogEmotionDiary(context, reactForEmotions.emotionFive());
                break;
        }
    }

    public  void AlertDialogEmotionDiary(Context context, AddUserToFirebase.HelpEmotion emotion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(emotion.getHelp())
                .setMessage("Хочешь оставить заметку?")
                .setCancelable(true)
                .setPositiveButton("Да", (dialog, which) -> {

                })
                .setNegativeButton("Нет", (dialog, which) -> {
                    onCloseDialogEmotionListener.onHideDialog();
                    dialog.cancel();
                })
                .show();

    }


}
