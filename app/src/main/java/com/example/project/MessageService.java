package com.example.project;


import androidx.annotation.NonNull;
import com.example.project.Emotion.Emotion;
import com.example.project.Main.DiaryEntry;
import com.example.project.Main.FirestoreGetId;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Objects;

public class MessageService extends WearableListenerService {
    private FirebaseFirestore fb;
    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        String message = Arrays.toString(messageEvent.getData());
        addMessageToFirestore(message);
    }
    public void addMessageToFirestore(String message){
        fb = FirebaseFirestore.getInstance();
        int emotionId = Integer.parseInt(message);
        DiaryEntry diaryEntry= new DiaryEntry(new Emotion(emotionId));
        FirebaseAuth mAuth =FirebaseAuth.getInstance();
        FirestoreGetId firestoreGetId= new FirestoreGetId(fb);
        firestoreGetId.getId(Objects.requireNonNull(mAuth.getCurrentUser()).getUid(), userId -> {
            fb.collection("Users")
                    .document(userId)
                    .collection("Entry")
                    .add(diaryEntry);
        });

    }
}
