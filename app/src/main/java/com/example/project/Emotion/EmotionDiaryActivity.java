package com.example.project.Emotion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Main.AddUserToFirebase;
import com.example.project.databinding.ActivityEmotionDiaryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class EmotionDiaryActivity extends AppCompatActivity {
    ActivityEmotionDiaryBinding binding;

    FirebaseFirestore fb;
    FirebaseAuth mAuth;
    FirebaseUser user;
    AlertDialog.Builder builder;
    ReactForEmotions reactForEmotions;
    FirestoreEmotion firestoreEmotion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityEmotionDiaryBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());

        fb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        reactForEmotions = new ReactForEmotions();

        firestoreEmotion = new FirestoreEmotion(this, fb, user);


        binding.imgPloho.setOnClickListener(v -> {
            Emotion emotion= new Emotion(1);
            emotionBtns(emotion);
        });
        binding.imgTakoe.setOnClickListener(v -> {
            Emotion emotion= new Emotion(2);
            emotionBtns(emotion);
        });
        binding.imgNorm.setOnClickListener(v -> {
            Emotion emotion= new Emotion(3);
            emotionBtns(emotion);
        });
        binding.imgWow.setOnClickListener(v -> {
            Emotion emotion= new Emotion(4);
            emotionBtns(emotion);
        });
        binding.imgAhuenno.setOnClickListener(v -> {
            Emotion emotion= new Emotion(5);
            emotionBtns(emotion);
        });
    }
//    private  void addEmotion(Emotion emotion){
//        getId(user.getUid(), userId -> {
//            if (userId != null) {
//                binding.tv.setText(userId);
//                fb.collection("Users")
//                        .document(userId)
//                        .collection("Diary")
//                        .add(emotion)
//                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<DocumentReference> task) {
//                                        if(task.isSuccessful())Toast.makeText(EmotionDiaryActivity.this, "assss", Toast.LENGTH_SHORT).show();
//                                        else binding.tv.setText("nenennen");
//                                    }
//                                });
//
//            } else {
//                Log.d(TAG, "Пользователь не найден по UID");
//            }
//        });
//
//    }
    public void emotionBtns(Emotion emotion){
//        addEmotion();
        if(emotion!=null) Toast.makeText(this, "aaaa", Toast.LENGTH_SHORT);
        firestoreEmotion.addEmotion(emotion);
        int emotionId = emotion.getId();
        switch (emotionId){
            case 1:
                AlertDialogEmotionDiary(reactForEmotions.emotionOne());
                break;
            case 2:
                AlertDialogEmotionDiary(reactForEmotions.emotionTwo());
                break;
            case 3:
                AlertDialogEmotionDiary(reactForEmotions.emotionThree());
                break;
            case 4:
                AlertDialogEmotionDiary(reactForEmotions.emotionFour());
                break;
            case 5:
                AlertDialogEmotionDiary(reactForEmotions.emotionFive());
                break;
        }
    }
    private void AlertDialogEmotionDiary(AddUserToFirebase.HelpEmotion emotion){
        builder = new AlertDialog.Builder(this);
        builder.setTitle(emotion.getHelp())
                .setMessage("Хочешь оставить заметку?")
                .setCancelable(true)
                .setPositiveButton("Да", (dialog, which) -> {
                    //активити с записями (чел оставляет запись, сохраняется она и эмоция с причинами , если была фокусировка, то ее параметры)
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //активити с записями (сохраняется эмоция+ причины+ фокусировка, если была)
                        dialog.cancel();
                    }
                })
                .show();
    }
//    public void getId(String currentUserId, final OnIdReturnedListener listener){
//        fb.collection("Users")
//                .whereEqualTo("uid", currentUserId)
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        if(!queryDocumentSnapshots.isEmpty()){
//                            String id = queryDocumentSnapshots.getDocuments().get(0).getId();
//                            listener.onIdReturned(id);
//                        } else {
//                            listener.onIdReturned(null);
//                        }
//                    }
//                });
//    }

    public interface OnIdReturnedListener {
        void onIdReturned(String userId);
    }






}