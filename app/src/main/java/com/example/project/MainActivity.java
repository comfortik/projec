package com.example.project;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements post {
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

    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());
        fb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firestoreGetId = new FirestoreGetId(fb);
        binding.btn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EmotionDiaryActivity.class);
            startActivity(intent);
        });

        firestoreGetId = new FirestoreGetId(fb);
        firestoreGetId.getId(mAuth.getCurrentUser().getUid(), userId -> {
            if (userId != null) {
                this.userId =userId;
            } else {
                Log.d(TAG, "Пользователь не найден по UID");
            }
        });

        spisok = new Spisok(this , fb, mAuth.getCurrentUser());
        getTypes();
        AddUserToFirebase add= new AddUserToFirebase(this, fb, mAuth);
        add.anonimouseSignUp();


//        types = new ArrayList<>();
//        types.add(new Type("Sleep", 6000, 5000));
//        types.add(new Type("Study", 50000,5000));


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
    }
    public void postpost(String name, long timeWork){
        binding.fragment.setVisibility(View.GONE);
        addTypes(name,timeWork);
        getTypes();
        binding.tvs.setText(name);
    }
    public void getTypes(){
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
        fb.collection("Users")
                .document(userId)
                .collection("Types")
                .add(new Type(name, timeWork,timeRest))
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()) Toast.makeText(MainActivity.this, "assss", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    public void addTypes(String name, long timeWork){
        fb.collection("Users")
                .document(userId)
                .collection("Types")
                .add(new Type(name, timeWork))
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()) Toast.makeText(MainActivity.this, "assss", Toast.LENGTH_SHORT).show();
                    }
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
            long finishmillies= (milliesec-savemilliesec)/1000;
            long hour= finishmillies/3600;
            long minutes = (finishmillies-hour*3600)/60;
            long sec= (finishmillies-hour*3600-minutes*60);
            if(hour<=0&&minutes<=0){
                builder.setMessage("Конец фокусировки "+"\n"+"Время фокусировки: "+String.valueOf(sec)+" секунд");
            }
            else if(hour<=0&&minutes>0){
                builder.setMessage("Конец фокусировки "+"\n"+"Время фокусировки: "+ String.valueOf(minutes)+" минут, "+String.valueOf(sec)+" секунд");
            }
            else if(hour>0&&minutes<=0){
                builder.setMessage("Конец фокусировки "+"\n"+"Время фокусировки: "+String.valueOf(hour)+" часов, "+String.valueOf(sec)+" секунд");
            }
            else{
                builder.setMessage("Конец фокусировки "+"\n"+"Время фокусировки: "+String.valueOf(hour)+" часов, "+ String.valueOf(minutes)+" минут, "+String.valueOf(sec)+" секунд");
            }
            builder.setTitle("Конец")
                    .setPositiveButton("ok", (dialog, which) -> dialog.cancel())
                    .show();
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
            builder.setTitle("Количество интервалов отдыха: " +countRest+" Количество интервалов работы: " +countWork)
                    .setMessage("Время в работе: "+  " : "+ hours+" : " + minutes+" : "+ sec+"\n"+"Время в отдыхе: "+  " : "+ hoursRest+" : " + minutesRest+" : "+ secRest)
                    .setPositiveButton("ok", (dialog, which) -> dialog.cancel())
                    .show();
        }

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



