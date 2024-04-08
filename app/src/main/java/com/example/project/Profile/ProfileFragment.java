package com.example.project.Profile;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.example.project.Emotion.EmotionDiaryFragment;
import com.example.project.Main.DiaryEntry;
import com.example.project.Main.FirestoreGetId;
import com.example.project.Main.MainActivity;
import com.example.project.Main.Type;
import com.example.project.OnHideFragmentContainerListener;
import com.example.project.R;
import com.example.project.ReplaceFragment;
import com.example.project.Settings.SettingsFragment;
import com.example.project.Sounds.SoundFragment;

import com.example.project.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private OnHideFragmentContainerListener hideFragmentContainerListener;
    FragmentProfileBinding binding;
    List<DiaryEntry> diaryEntryList= new ArrayList<>();

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
        binding.bottomNavigationView.setSelectedItemId(R.id.bottom_profile);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        ReplaceFragment replaceFragment = new ReplaceFragment();


        firestoreGetId.getId(mAuth.getCurrentUser().getUid(), userId -> {
            fb.collection("Users")
                    .document(userId)
                    .collection("Entry")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if(documentSnapshot!=null){
                            diaryEntryList = queryDocumentSnapshots.toObjects(DiaryEntry.class);

                        }
                    }
                    ProfileAdapter adapter = new ProfileAdapter();
                    ItemClickListener itemClickListener= diaryEntry -> adapter.openFragment(diaryEntry);
                    adapter.setItemClickListener(itemClickListener);
                    recycler.setAdapter(adapter);
                    adapter.setItems(diaryEntryList);
                }
            });
        });


        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.bottom_settings) {
                    replaceFragment.replaceFragment(fragmentManager,new SettingsFragment());
                } else if (menuItem.getItemId() == R.id.bottom_emotionDiary) {
                    replaceFragment.replaceFragment(fragmentManager,new EmotionDiaryFragment());
                } else if (menuItem.getItemId() == R.id.bottom_timer) {
                    ((OnHideFragmentContainerListener)getActivity()).onButtonTimerClick();
                    getActivity().getSupportFragmentManager().beginTransaction().remove(ProfileFragment.this).commit();
                } else if (menuItem.getItemId() == R.id.bottom_sound) {
                    replaceFragment.replaceFragment(fragmentManager,new SoundFragment());
                } else if (menuItem.getItemId() == R.id.bottom_profile) {
                    replaceFragment.replaceFragment(fragmentManager, new ProfileFragment());
                }


                return false;
            }
        });
        return view;
    }

    public void setOnHideFragmentContainerListener(OnHideFragmentContainerListener listener) {
        this.hideFragmentContainerListener = listener;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}