package com.example.project.Main;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AddTypeToFirestore {
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
