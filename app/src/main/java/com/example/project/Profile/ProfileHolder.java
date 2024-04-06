package com.example.project.Profile;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Main.AddUserToFirebase;
import com.example.project.Main.FirestoreGetId;
import com.example.project.R;
import com.example.project.TypeItemClickListener;
import com.example.project.databinding.ItemDiaryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileHolder extends RecyclerView.ViewHolder {
    ItemDiaryBinding binding;
    TypeItemClickListener typeItemClickListener;
    FirestoreGetId firestoreGetId;
    FirebaseFirestore fb;
    FirebaseAuth mAuth;

    public ProfileHolder(ItemDiaryBinding binding, TypeItemClickListener typeItemClickListener) {
        super(binding.getRoot());
        this.binding = binding;
        this.typeItemClickListener = typeItemClickListener;
    }
    public void setBinding(){



    }
}
