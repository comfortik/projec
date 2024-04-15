package com.example.project.Main;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.databinding.FragmentNewTypeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class NewTypeFragment extends Fragment {
    String newName;
    private NewTypeViewModel viewModel;
    private FragmentNewTypeBinding binding;
    public static NewTypeFragment newInstance() {
        return new NewTypeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNewTypeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.strelka.setOnClickListener(v -> {
            FirebaseFirestore fb = FirebaseFirestore.getInstance();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirestoreGetId firestoreGetId = new FirestoreGetId(fb);
            firestoreGetId.getId(mAuth.getCurrentUser().getUid(), userId -> {
                fb.collection("Users")
                        .document(userId)
                        .collection("Types")
                        .whereEqualTo("name", binding.etFragmentCreateType.getText().toString())
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                CharSequence s= "asdasdas";
                                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                            } else if (binding.etFragmentCreateType.getText().toString().isEmpty()) {
                                Toast.makeText(getContext(), "Введите текст", Toast.LENGTH_SHORT).show();
                            } else{
                                newName= binding.etFragmentCreateType.getText().toString();
                                ChooseFragment chooseFragment = ChooseFragment.newInstance(newName);
                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment, chooseFragment)
                                        .addToBackStack(null)
                                        .commit();
                            }

                        });
            });

        });


        binding.etFragmentCreateType.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                binding.strelka.performClick();
                return true;
            }
            return false;
        });

        return view;
    }



}