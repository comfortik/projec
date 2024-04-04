package com.example.project.Emotion;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.project.Main.FirestoreGetId;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreEmotion {
    FirebaseFirestore fb;
    FirebaseUser user;
    Context context;
    FirestoreGetId firestoreGetId;
    public FirestoreEmotion(Context context, FirebaseFirestore fb, FirebaseUser user){

        this.fb = fb;
        this.user = user;
        this.context=context;
    }
//    private void getId(String currentUserId, final EmotionDiaryActivity.OnIdReturnedListener listener){
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
    public  void addEmotion(Emotion emotion){
        firestoreGetId = new FirestoreGetId(fb);
        firestoreGetId.getId(user.getUid(), userId -> {
            if (userId != null) {
                fb.collection("Users")
                        .document(userId)
                        .collection("Diary")
                        .add(emotion)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isSuccessful()) Toast.makeText(context, "assss", Toast.LENGTH_SHORT).show();
                            }
                        });

            } else {
                Log.d(TAG, "Пользователь не найден по UID");
            }
        });

    }
}
