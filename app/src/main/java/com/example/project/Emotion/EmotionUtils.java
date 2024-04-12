package com.example.project.Emotion;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.project.Main.AddUserToFirebase;
import com.example.project.Main.DiaryEntry;
import com.example.project.Main.FirestoreGetId;
import com.example.project.Main.FocusMode;
import com.example.project.Note.Note;
import com.example.project.OnNote;
import com.example.project.databinding.AlertNoteBinding;
import com.example.project.databinding.FragmentEmotionDiaryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class EmotionUtils {
     FirestoreEmotion firestoreEmotion;
     ReactForEmotions reactForEmotions;
     OnNote onNote;
    DiaryEntry diaryEntry;
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
            emotionBtns(context, emotion);
        });
        binding.imgTakoe.setOnClickListener(v -> {
            Emotion emotion= new Emotion(2);
            emotionBtns(context, emotion);
        });
        binding.imgNorm.setOnClickListener(v -> {
            Emotion emotion= new Emotion(3);
            emotionBtns(context, emotion);
        });
        binding.imgWow.setOnClickListener(v -> {
            Emotion emotion= new Emotion(4);
            emotionBtns(context, emotion);
        });
        binding.imgAhuenno.setOnClickListener(v -> {
            Emotion emotion= new Emotion(5);
            emotionBtns(context, emotion);
        });
    }

    public  void emotionBtns(Context context, Emotion emotion ) {
        int emotionId = emotion.getId();
        switch (emotionId){
            case 1:
                AlertDialogEmotionDiary(context, reactForEmotions.emotionOne(), emotion);
                break;
            case 2:
                AlertDialogEmotionDiary(context, reactForEmotions.emotionTwo(), emotion);
                break;
            case 3:
                AlertDialogEmotionDiary(context, reactForEmotions.emotionThree(), emotion);
                break;
            case 4:
                AlertDialogEmotionDiary(context, reactForEmotions.emotionFour(), emotion);
                break;
            case 5:
                AlertDialogEmotionDiary(context, reactForEmotions.emotionFive(), emotion);
                break;
        }
    }

    private void AlertDialogEmotionDiary(Context context, AddUserToFirebase.HelpEmotion helpEmotion, Emotion emotion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(helpEmotion.getHelp())
                .setMessage("Хочешь оставить заметку?")
                .setCancelable(true)
                .setCancelable(false)
                .setPositiveButton("Да", (dialog, which) -> {
                    onNote.onNote(emotion);
                    dialog.cancel();
                })
                .setNegativeButton("Нет", (dialog, which) -> {
                    onCloseDialogEmotionListener.onHideDialog(emotion);
                    dialog.cancel();
                })
                .show();

    }
    public void alertEmotion(FirebaseFirestore fb, FirebaseAuth mAuth, FocusMode focusMode, Emotion emotion){
        FirestoreGetId  firestoreGetId = new FirestoreGetId(fb);
        if(focusMode!=null){
            diaryEntry = new DiaryEntry( focusMode,  emotion);
            Log.e("AAAAAAA", String.valueOf(diaryEntry.getFocusMode().getSecRest()));
        }
        else{
            diaryEntry = new DiaryEntry(emotion);
        }
        firestoreGetId.getId(Objects.requireNonNull(mAuth.getCurrentUser()).getUid(), userId -> fb.collection("Users")
                .document(userId)
                .collection("Entry")
                .add(diaryEntry));


    }

    public void alertNote(Context context, LayoutInflater layoutInflater, FirebaseFirestore fb, FirebaseAuth mAuth, FocusMode focusMode, Emotion emotion){
        FirestoreGetId firestoreGetId = new FirestoreGetId(fb);
        AlertDialog dialog;
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setCancelable(false);
        AlertNoteBinding binding2 = AlertNoteBinding.inflate(layoutInflater, null, false);
        View view = binding2.getRoot();
        dialog=builder1.setView(view).show();
        binding2.btnOk.setOnClickListener(v -> {
            if(binding2.etNote.getText().length()==0){
                Toast.makeText(context, "Введите заметку", Toast.LENGTH_SHORT).show();
            } else if (binding2.etNote.getText().toString().isEmpty()) {
                Toast.makeText(context, "Введите заметку", Toast.LENGTH_SHORT).show();
            }
            else{
                Note note = new Note(binding2.etNote.getText().toString());
                if(focusMode!=null) {
                    diaryEntry = new DiaryEntry(note, focusMode, emotion);
                }
                else{
                    diaryEntry = new DiaryEntry(note, emotion);
                }
                firestoreGetId.getId(Objects.requireNonNull(mAuth.getCurrentUser()).getUid(), userId -> fb.collection("Users")
                        .document(userId)
                        .collection("Entry")
                        .add(diaryEntry)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful())Toast.makeText(context, "Запись добавлена", Toast.LENGTH_SHORT).show();
                            else Toast.makeText(context, "Не удалось добавить запись", Toast.LENGTH_SHORT).show();
                        }));
                dialog.cancel();
            }

        });



    }
    public void setOnNote(OnNote onNote){
        this.onNote=onNote;
    }


}
