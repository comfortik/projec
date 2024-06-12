package com.example.project.Profile;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Main.DiaryEntry;
import com.example.project.Main.FirestoreGetId;
import com.example.project.OnHideFragmentContainerListener;
import com.example.project.R;
import com.example.project.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private OnHideFragmentContainerListener hideFragmentContainerListener;
    FragmentProfileBinding binding;

    List<DiaryEntry> diaryEntryList= new ArrayList<>();
    ProfileAdapter adapter;
    private String url="http://172.20.10.2:5000";
    private String POST="POST";

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

        binding.report.setOnClickListener(new View.OnClickListener() {
            Instant instant = Instant.now();
            ZoneId zoneId = ZoneId.systemDefault();
            Date currentDate = Date.from(instant.atZone(zoneId).toInstant());
            @Override
            public void onClick(View v) {
                firestoreGetId.getId(mAuth.getCurrentUser().getUid(), userId ->{
                    fb.collection("Users")
                            .document(userId)
                            .collection("Entry")
                            .whereGreaterThanOrEqualTo("date", getStartOfWeek())
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                int totalEmotions = 0;
                                StringBuilder allNotes = new StringBuilder();
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    DiaryEntry diaryEntry = documentSnapshot.toObject(DiaryEntry.class);
                                    totalEmotions += diaryEntry.getEmotion().getId();
                                    if (diaryEntry.getNote()!= null) {
                                        allNotes.append(diaryEntry.getNote().getNote()).append("\n");
                                    }
                                }
                                double averageEmotion = queryDocumentSnapshots.size() > 0 ? (double) totalEmotions / queryDocumentSnapshots.size() : 0;
                                allNotes.append(String.valueOf(averageEmotion));
                                sendRequest(getContext(), POST, "getname", "number", allNotes.toString());
                            });


                } );


            }
        });

        return view;
    }

    public void setOnHideFragmentContainerListener(OnHideFragmentContainerListener listener) {
        this.hideFragmentContainerListener = listener;
    }

    private Date getStartOfWeek(){
        Calendar startOfWeekCalendar = Calendar.getInstance();
        startOfWeekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        startOfWeekCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startOfWeekCalendar.set(Calendar.MINUTE, 0);
        startOfWeekCalendar.set(Calendar.SECOND, 0);
        startOfWeekCalendar.set(Calendar.MILLISECOND, 0);
        return startOfWeekCalendar.getTime();
    }
    private void sendRequest(Context context, String type, String method, String paramname, String param) {
        String fullURL = url + "/" + method;
        Request request;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build();

        if (type.equals(POST)) {
            RequestBody formBody = new FormBody.Builder()
                    .add(paramname, param)
                    .build();
            request = new Request.Builder()
                    .url(fullURL)
                    .post(formBody)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(fullURL)
                    .build();
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseData = response.body().string();
                AlertDialogReaction(context, responseData);
            }
        });
    }
    private void AlertDialogReaction(Context context, String response) {
        ((AppCompatActivity) context).runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogText);
            builder.setTitle("")
                    .setMessage(response)
                    .setPositiveButton("Oк", (dialog, which) -> {
                        dialog.cancel();
                    });
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setShape(GradientDrawable.RECTANGLE);
            gradientDrawable.setColor(Color.parseColor("#90A4AE"));
            gradientDrawable.setCornerRadius(10);
            AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(gradientDrawable);
            alertDialog.show();
        });
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