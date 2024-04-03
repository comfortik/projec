package com.example.project;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AddUserToFirebase {
    FirebaseFirestore fb;
    FirebaseAuth mAuth;
    Context context;



    static String id;
    public AddUserToFirebase(Context context, FirebaseFirestore fb, FirebaseAuth mAuth){
        this.context=context;
        this.fb=fb;
        this.mAuth=mAuth;
    }
    public static String getId() {
        return id;
    }
    public void anonimouseSignUp() {
        mAuth.getInstance();
        fb=FirebaseFirestore.getInstance();
        mAuth.signInAnonymously()
                .addOnCompleteListener((Activity) context, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInAnonymously:success");
                        Toast.makeText(context, "Authentication success.", Toast.LENGTH_SHORT).show();
                        userExist();
                    } else {
                        Log.w(TAG, "signInAnonymously:failure", task.getException());
                        Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void userExist() {

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            fb.collection("Users")
                    .whereEqualTo("uid", user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                Toast.makeText(context, "Файлик имеется", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(context, "Нет файлика, создаем новый!", Toast.LENGTH_SHORT).show();
                                addUser();
                            }
                        }
                    });
        } else {
            Toast.makeText(context, "Проблема с FirebaseUser", Toast.LENGTH_SHORT).show();
        }
    }
    private void addUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid().toString();

        Map<String, Object> userData = new HashMap<>();
        userData.put("uid", uid);
        fb.collection("Users")
                .add(userData)
                .addOnSuccessListener(documentReference -> {
                    id = documentReference.getId();
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    Toast.makeText(context, "DocumentSnapshot added with ID: " + documentReference.getId(),
                            Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding document", e);
                    Toast.makeText(context, "Error adding document",
                            Toast.LENGTH_SHORT).show();
                });
    }

    public static class HelpEmotion {
        public String getHelp() {
            return help;
        }

        public int getHz() {
            return hz;
        }

        public boolean isYes() {
            return yes;
        }

        private String help;
        private int hz;
        private boolean yes;

        public HelpEmotion(String help, int hz, boolean yes) {
            this.help = help;
            this.hz = hz;
            this.yes = yes;
        }
    }
}
