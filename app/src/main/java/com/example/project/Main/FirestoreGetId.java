package com.example.project.Main;

import com.example.project.Emotion.EmotionDiaryFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FirestoreGetId {
    FirebaseFirestore fb;
    public FirestoreGetId(FirebaseFirestore fb){
        this.fb=fb;
    }

    public void getId(String currentUserId, final EmotionDiaryFragment.OnIdReturnedListener listener){
        fb.collection("Users")
                .whereEqualTo("uid", currentUserId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        String id = queryDocumentSnapshots.getDocuments().get(0).getId();
                        listener.onIdReturned(id);
                    } else {
                        listener.onIdReturned(null);
                    }
                });
    }



}
