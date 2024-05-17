package com.example.project.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Main.DiaryEntry;
import com.example.project.Main.FirestoreGetId;
import com.example.project.OnHideFragmentContainerListener;
import com.example.project.R;
import com.example.project.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private OnHideFragmentContainerListener hideFragmentContainerListener;
    FragmentProfileBinding binding;
    List<DiaryEntry> diaryEntryList= new ArrayList<>();
    ProfileAdapter adapter;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth  = FirebaseAuth.getInstance();
        FirestoreGetId firestoreGetId = new FirestoreGetId(fb);
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        RecyclerView recycler = view.findViewById(R.id.recyclerView);
        firestoreGetId.getId(mAuth.getCurrentUser().getUid(), userId -> {
            fb.collection("Users")
                    .document(userId)
                    .collection("Entry")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if(documentSnapshot!=null){
                                binding.textView.setVisibility(View.GONE);
                                diaryEntryList = queryDocumentSnapshots.toObjects(DiaryEntry.class);
                                Collections.sort(diaryEntryList, Comparator.comparing(DiaryEntry::getDate).reversed());
                            }
                        }
                        adapter = new ProfileAdapter();
                        ItemClickListener itemClickListener= diaryEntry -> adapter.openFragment(getContext(), diaryEntry, getLayoutInflater());
                        adapter.setItemClickListener(itemClickListener);
                        recycler.setAdapter(adapter);
                        adapter.setItems(diaryEntryList);
                    });
        });
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0,ItemTouchHelper.START);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Date date = diaryEntryList.get(position).getDate();
                firestoreGetId.getId(mAuth.getCurrentUser().getUid(), userId -> {
                    fb.collection("Users")
                            .document(userId)
                            .collection("Entry")
                            .whereEqualTo("date", date)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        document.getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                adapter.deleteItem(position);
                                            }
                                        });

                                    }

                                }
                            });
                });


            }
        });

        helper.attachToRecyclerView(binding.recyclerView);

        return view;
    }

    public void setOnHideFragmentContainerListener(OnHideFragmentContainerListener listener) {
        this.hideFragmentContainerListener = listener;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        mViewModel.getDiaryEntries().observe(getViewLifecycleOwner(), diaryEntries -> {
            if (diaryEntries != null && !diaryEntries.isEmpty()) {
                adapter.setItems(diaryEntries);
                adapter.notifyDataSetChanged();
            }
        });
    }


}