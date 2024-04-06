package com.example.project.Main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.project.Emotion.EmotionDiaryFragment;
import com.example.project.Emotion.EmotionUtils;
import com.example.project.Emotion.FirestoreEmotion;
import com.example.project.Emotion.OnAddUserToFirestore;
import com.example.project.Emotion.OnCloseDialogEmotionListener;
import com.example.project.Emotion.ReactForEmotions;
import com.example.project.OnHideFragmentContainerListener;
import com.example.project.Profile.ProfileFragment;
import com.example.project.R;
import com.example.project.Settings.SettingsFragment;
import com.example.project.Sounds.SoundFragment;
import com.example.project.databinding.ActivityEmotionDiaryBinding;
import com.example.project.databinding.ActivityMainBinding;
import com.example.project.databinding.FragmentEmotionDiaryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity implements post, OnHideFragmentContainerListener {
    long milliesec;
    long savemilliesec;
    boolean isRunning, cancelButton, porabotal;
    CountDownTimer countDownTimer;
    AlertDialog.Builder builder;
    public Type currentType;
    private List<Type> types;
    int countRest;
    int countWork ;
    Spisok spisok;
    CustomAdapter adapter;
    private ActivityMainBinding binding;
    FirebaseFirestore fb;
    FirebaseAuth mAuth;
    FirestoreGetId firestoreGetId;
    AddUserToFirebase add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());
        fb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
         add= new AddUserToFirebase(this, fb, mAuth);
        add.anonimouseSignUp();
        firestoreGetId = new FirestoreGetId(fb);
        add.setOnAddUserToFirestore(new OnAddUserToFirestore() {
            @Override
            public void addUser() {
                getTypes();
            }
        });
//        getTypes();


        binding.bottomNavigationView.setSelectedItemId(R.id.bottom_timer);
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.bottom_settings) {
                    SettingsFragment settingsFragment = new SettingsFragment();
                    settingsFragment.setOnHideFragmentContainerListener(new OnHideFragmentContainerListener() {
                        @Override
                        public void onButtonTimerClick() {
                            binding.bottomNavigationView.setVisibility(View.GONE);
                        }
                    });
                    replaceFragment(new SettingsFragment());
                } else if (menuItem.getItemId() == R.id.bottom_emotionDiary) {
                    EmotionDiaryFragment emotionDiaryFragment = new EmotionDiaryFragment();
                    emotionDiaryFragment.setOnHideFragmentContainerListener(new OnHideFragmentContainerListener() {
                        @Override
                        public void onButtonTimerClick() {
                            binding.fragmentView.setVisibility(View.GONE);
                        }
                    });
                    replaceFragment(new EmotionDiaryFragment());
                } else if (menuItem.getItemId() == R.id.bottom_sound) {
                    SoundFragment soundFragment= new SoundFragment();
                    soundFragment.setOnHideFragmentContainerListener(new OnHideFragmentContainerListener() {
                        @Override
                        public void onButtonTimerClick() {
                            binding.fragmentView.setVisibility(View.GONE);
                        }
                    });
                    replaceFragment(new SoundFragment());
                } else if (menuItem.getItemId() == R.id.bottom_profile) {
                    ProfileFragment profileFragment = new ProfileFragment();
                    profileFragment.setOnHideFragmentContainerListener(new OnHideFragmentContainerListener() {
                        @Override
                        public void onButtonTimerClick() {
                            binding.fragmentView.setVisibility(View.GONE);
                        }
                    });
                    replaceFragment(profileFragment);
                }
                return false;
            }
        });


        binding.btnTimer.setOnClickListener(v -> {
            countWork= 0;
            countRest=0;
            porabotal= true;
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            binding.tvs.setText("Работа");
            binding.btnTimer.setVisibility(View.GONE);
            binding.ll.setVisibility(View.VISIBLE);
            Timer(milliesec);
        });
        binding.filliedExposed.setOnItemClickListener((parent, view, position, id) -> {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            binding.tvTimer.setText("00:00:00");
            countWork=0;
            countRest=0;
            binding.btnTimer.setVisibility(View.VISIBLE);
            binding.ll.setVisibility(View.GONE);
            if (position == binding.filliedExposed.getAdapter().getCount() - 1) {
                createNewType();
            }else{
                Toast.makeText(MainActivity.this, binding.filliedExposed.getText().toString(),Toast.LENGTH_SHORT).show();
                String selectedTypeName = parent.getItemAtPosition(position).toString();
                loadTypeFromFirestore(selectedTypeName);

            }
        });



        binding.btnTimerCancel.setOnClickListener(v -> {
            cancelButton=true;
            if(!currentType.isInterval()){
                countDownTimer.cancel();
                AlertDilaog();
            }
            else{
                countDownTimer.cancel();
                binding.tvs.setText((countRest+" "+countWork).toString());
                AlertDilaog();
            }
        });
        binding.btnTimerPause.setOnClickListener(v -> {
            if(isRunning){
                countDownTimer.cancel();
                binding.btnTimerPause.setText("Play");
                isRunning=false;
            } else if (!isRunning) {
                Timer(savemilliesec);
                binding.btnTimerPause.setText("Pause");
                isRunning= true;
            }
        });
    }
    private void replaceFragment(Fragment fragment){
        binding.fragmentView.setVisibility(View.VISIBLE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentView,fragment )
                .commit();
    }
    private void loadTypeFromFirestore(String selectedType) {
        firestoreGetId.getId(mAuth.getCurrentUser().getUid(), userId1 -> {
            fb.collection("Users")
                    .document(userId1)
                    .collection("Types")
                    .whereEqualTo("name", selectedType) // Укажите значение, с которым сравнивать поле "name"
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                // Обработайте найденный документ
                                Type type = documentSnapshot.toObject(Type.class);
                                milliesec=type.getTimeWork();
                                currentType=type;
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Обработка ошибки получения данных из Firestore
                        }
                    });
        });

    }





    public void Timer(long milliesec){
        countDownTimer= new CountDownTimer(milliesec,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                isRunning = true;
                NumberFormat f = new DecimalFormat("00");
                long totalSeconds = millisUntilFinished / 1000;
                long hour = totalSeconds / 3600;
                long min = (totalSeconds % 3600) / 60;
                long sec = totalSeconds % 60;
                savemilliesec= millisUntilFinished;
                binding.tvTimer.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }
            @Override
            public void onFinish() {
                if(currentType.isInterval() && porabotal){
                    Timer(currentType.getTimeRest());
                    porabotal=false;
                    countRest++;
                    binding.tvs.setText("Отдых");
                } else if (currentType.isInterval()&&!porabotal){
                    Timer(currentType.getTimeWork());
                    porabotal=true;
                    countWork++;
                    binding.tvs.setText("Работа");
                } else{
                    binding.tvTimer.setText("00:00:00");
                    finishTimer();
                }
            }
        }.start();
    }
    public void createNewType(){
        binding.fragment.setVisibility(View.VISIBLE);
        NewTypeFragment newTypeFragment = new NewTypeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, newTypeFragment);
        ft.commit();
    }

    public void postpost(String name, long timeWork){
        binding.fragment.setVisibility(View.GONE);
        addTypes(name,timeWork);
        getTypes();
        binding.tvs.setText(name);
    }
    public void getTypes(){
        spisok = new Spisok(this , fb, mAuth.getCurrentUser());
        spisok.createSpisok(new DataLoadListener() {
            @Override
            public void onDataLoaded(CustomAdapter adapter) {

                if (adapter != null) {
                    binding.filliedExposed.setAdapter(adapter);
                } else {
                    // обработка ошибки загрузки данных
                }
            }
        });
    }
    public void addTypes(String name, long timeWork, long timeRest){
        firestoreGetId.getId(mAuth.getCurrentUser().getUid(), userId1 -> {
            if(userId1!=null){
                fb.collection("Users")
                        .document(userId1)
                        .collection("Types")
                        .add(new Type(name, timeWork,timeRest))
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isSuccessful()) Toast.makeText(MainActivity.this, "assss", Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });


    }
    public void addTypes(String name, long timeWork){
        firestoreGetId.getId(mAuth.getCurrentUser().getUid(), userId1 -> {
            fb.collection("Users")
                    .document(userId1)
                    .collection("Types")
                    .add(new Type(name, timeWork))
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()) Toast.makeText(MainActivity.this, "assss", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

    }

    public void postpost(String name, long timeWork, long timeRest){
        binding.fragment.setVisibility(View.GONE);
        addTypes(name, timeWork, timeRest);
        getTypes();
        binding.tvs.setText(name);
    }
    public void AlertDilaog(){
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Подтверждение")
                .setMessage("Вы точно хотите остановить таймер?")
                .setCancelable(true)
                .setPositiveButton("Да", (dialog, which) -> {
                    countDownTimer.cancel();
                    binding.tvTimer.setText("00:00:00");
                    binding.btnTimer.setVisibility(View.VISIBLE);
                    binding.ll.setVisibility(View.GONE);
                    finishTimer();
                })
                .setNegativeButton("Нет ", (dialog, which) -> {
                    Timer(savemilliesec);
                    dialog.cancel();
                })
                .show();
    }
    public void finishTimer(){
        if(!currentType.isInterval()){
            builder = new AlertDialog.Builder(this);
            long finishmillies= (milliesec-savemilliesec)/1000;
            long hour= finishmillies/3600;
            long minutes = (finishmillies-hour*3600)/60;
            long sec= (finishmillies-hour*3600-minutes*60);
            FocusMode focusMode = new FocusMode(currentType.getName(), hour, minutes, sec );
            alertDialogEmotion(focusMode);
            Toast.makeText(this, focusMode.getName(), Toast.LENGTH_SHORT).show();
//            if(hour<=0&&minutes<=0){
//                builder.setMessage("Конец фокусировки "+"\n"+"Время фокусировки: "+String.valueOf(sec)+" секунд");
//            }
//            else if(hour<=0&&minutes>=0){
//                builder.setMessage("Конец фокусировки "+"\n"+"Время фокусировки: "+ String.valueOf(minutes)+" минут, "+String.valueOf(sec)+" секунд");
//            }
//            else if(hour>0&&minutes<=0){
//                builder.setMessage("Конец фокусировки "+"\n"+"Время фокусировки: "+String.valueOf(hour)+" часов, "+String.valueOf(sec)+" секунд");
//            }
//            else{
//                builder.setMessage("Конец фокусировки "+"\n"+"Время фокусировки: "+String.valueOf(hour)+" часов, "+ String.valueOf(minutes)+" минут, "+String.valueOf(sec)+" секунд");
//            }
//            builder.setTitle("Конец")
//                    .setPositiveButton("ok", (dialog, which) -> dialog.cancel())
//                    .show();
        }
        else {
            long workTime=0;
            long restTime=0;
            if(porabotal){
                workTime = countWork*currentType.getTimeWork()/1000+(currentType.getTimeWork()-savemilliesec)/1000;
                restTime = countRest*currentType.getTimeRest()/1000;
            }
            else if(!porabotal){
                workTime = countWork*currentType.getTimeWork()/1000;
                restTime = countRest*currentType.getTimeRest()/1000+(currentType.getTimeWork()-savemilliesec)/1000;
            }
            long hours= workTime/3600;
            long minutes = (workTime-hours*3600)/60;
            long sec = (workTime-hours*3600-minutes*60);
            long hoursRest= restTime/3600;
            long minutesRest = (restTime-hours*3600)/60;
            long secRest = (restTime-hours*3600-minutes*60);
            FocusMode focusMode = new FocusMode(currentType.getName(), hours, minutes, sec, hoursRest, minutesRest, secRest, countWork, countRest);
            builder.setTitle("Количество интервалов отдыха: " +countRest+" Количество интервалов работы: " +countWork);
//                    .setMessage("Время в работе: "+  " : "+ hours+" : " + minutes+" : "+ sec+"\n"+"Время в отдыхе: "+  " : "+ hoursRest+" : " + minutesRest+" : "+ secRest)
//                    .setPositiveButton("ok", (dialog, which) -> dialog.cancel())
//                    .show();
        }

    }

    private void alertDialogEmotion(FocusMode focusMode){
        AlertDialog dialog;
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        FragmentEmotionDiaryBinding binding1 = FragmentEmotionDiaryBinding.inflate(getLayoutInflater(), null, false);
        View view = binding1.getRoot();
        ReactForEmotions reactForEmotions = new ReactForEmotions();
        FirestoreEmotion firestoreEmotion = new FirestoreEmotion(this, fb, mAuth.getCurrentUser());
        EmotionUtils emotionUtils = new EmotionUtils(firestoreEmotion, reactForEmotions);
        emotionUtils.setListeners(this, binding1, focusMode);
        dialog = builder1.setView(view).show();

        emotionUtils.setOnCloseDialogEmotionListener(new OnCloseDialogEmotionListener() {

            @Override
            public void onHideDialog() {
                dialog.cancel();
                firestoreGetId.getId(mAuth.getCurrentUser().getUid(), userId -> {
                    fb.collection("Users")
                            .document(userId)
                            .collection("Entry")
                            .add(focusMode)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if(task.isSuccessful())Toast.makeText(MainActivity.this, "Запись в дневник добавлена", Toast.LENGTH_SHORT);
                                    else Toast.makeText(MainActivity.this, "Не получилось добавить запись в дневник", Toast.LENGTH_SHORT);
                                }
                            });
                });
            }
        });
    }


    @Override
    public void onButtonTimerClick() {
        binding.fragmentView.setVisibility(View.GONE);
    }




//    public void anonimouseSignUp() {
//        mAuth.signInAnonymously()
//                .addOnCompleteListener(MainActivity.this, task -> {
//                    if (task.isSuccessful()) {
//                        Log.d(TAG, "signInAnonymously:success");
//                        Toast.makeText(MainActivity.this, "Authentication success.", Toast.LENGTH_SHORT).show();
//                        userExist();
//                    } else {
//                        Log.w(TAG, "signInAnonymously:failure", task.getException());
//                        Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//
//
//    public void addUser() {
//        FirebaseUser user = mAuth.getCurrentUser();
//        String uid = user.getUid().toString();
//
//        Map<String, Object> userData = new HashMap<>();
//        userData.put("uid", uid);
//        fb.collection("Users")
//                .add(userData)
//                .addOnSuccessListener(documentReference -> {
//                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                    Toast.makeText(MainActivity.this, "DocumentSnapshot added with ID: " + documentReference.getId(),
//                            Toast.LENGTH_SHORT).show();
//                })
//                .addOnFailureListener(e -> {
//                    Log.w(TAG, "Error adding document", e);
//                    Toast.makeText(MainActivity.this, "Error adding document",
//                            Toast.LENGTH_SHORT).show();
//                });
//    }
//
//    public void userExist() {
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null) {
//            fb.collection("Users")
//                    .whereEqualTo("uid", user.getUid())
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
//                                Toast.makeText(MainActivity.this, "Файлик имеется", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(MainActivity.this, "Нет файлика, создаем новый!", Toast.LENGTH_SHORT).show();
//                                addUser();
//                            }
//                        }
//                    });
//        } else {
//            Toast.makeText(MainActivity.this, "Проблема с FirebaseUser", Toast.LENGTH_SHORT).show();
//        }
//    }

}



