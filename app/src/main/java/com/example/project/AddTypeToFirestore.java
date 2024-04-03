package com.example.project;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddTypeToFirestore {
    Context context;
    List<Type> types;
    FirebaseFirestore fb;
    FirebaseUser user;

    FirestoreGetId firestoreGetId;
    public AddTypeToFirestore(FirebaseFirestore fb, FirebaseUser user){
        this.fb = fb;
        this.user = user;
    }
    public void getTypes() {
        firestoreGetId.getId(user.getUid(), userId -> {
            if (userId != null) {
                fb.collection("Users")
                        .document(userId)
                        .collection("Types")
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            List<Type> types = documentSnapshot.toObjects(Type.class);

                        });
            }
        });
    }
}
