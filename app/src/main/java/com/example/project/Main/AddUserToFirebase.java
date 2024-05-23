package com.example.project.Main;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.example.project.Emotion.OnAddUserToFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddUserToFirebase {
    FirebaseFirestore fb;
    FirebaseAuth mAuth;
    Context context;
    OnAddUserToFirestore onAddUserToFirestore;



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
                        userExist();
                    } else {
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
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            onAddUserToFirestore.addUser();
                        } else {

                            addUser();

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

                    onAddUserToFirestore.addUser();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error adding document",
                            Toast.LENGTH_SHORT).show();
                });


    }

    public void setOnAddUserToFirestore(OnAddUserToFirestore onAddUserToFirestore) {
        this.onAddUserToFirestore = onAddUserToFirestore;
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
