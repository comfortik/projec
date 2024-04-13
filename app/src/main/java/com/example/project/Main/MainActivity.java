package com.example.project.Main;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Emotion.EmotionDiaryFragment;
import com.example.project.Emotion.EmotionUtils;
import com.example.project.Emotion.FirestoreEmotion;
import com.example.project.Emotion.ReactForEmotions;
import com.example.project.OnHideFragmentContainerListener;
import com.example.project.Profile.ProfileFragment;
import com.example.project.R;
import com.example.project.Settings.SettingsFragment;
import com.example.project.Sounds.SoundFragment;
import com.example.project.databinding.ActivityMainBinding;
import com.example.project.databinding.FragmentEmotionDiaryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements post, OnHideFragmentContainerListener {
    long milliesec;
    long savemilliesec;
    boolean isRunning, cancelButton, porabotal;
    CountDownTimer countDownTimer;
    AlertDialog.Builder builder;
    public Type currentType;
    int countRest;
    long restartRestMillies;
    int countWork ;
    Spisok spisok;
    private ActivityMainBinding binding;
    FirebaseFirestore fb;
    FirebaseAuth mAuth;
    FirestoreGetId firestoreGetId;
    AddUserToFirebase add;
    long restartTimerMillies=0;
    int countRestartTimer;
    Context context = MainActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
//        setContentView(binding.getRoot());
        View customLoadingView = getLayoutInflater().inflate(R.layout.splash_screen, null);
        setContentView(customLoadingView);

        fb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        add= new AddUserToFirebase(this, fb, mAuth);
        add.anonimouseSignUp();
        firestoreGetId = new FirestoreGetId(fb);
        add.setOnAddUserToFirestore(this::getTypes);

        final int NOTIFICATION_PERMISSION_CODE = 123;
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
            }
        }
        binding.bottomNavigationView.setSelectedItemId(R.id.bottom_timer);
        binding.bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.bottom_emotionDiary) {
                EmotionDiaryFragment emotionDiaryFragment = new EmotionDiaryFragment();
//                emotionDiaryFragment.setOnHideFragmentContainerListener(() -> binding.fragmentView.setVisibility(View.GONE));
                replaceFragment(new EmotionDiaryFragment());
            } else if (menuItem.getItemId() == R.id.bottom_sound) {
                SoundFragment soundFragment= new SoundFragment();
//                soundFragment.setOnHideFragmentContainerListener(() -> binding.fragmentView.setVisibility(View.GONE));
                replaceFragment(new SoundFragment());
            } else if (menuItem.getItemId() == R.id.bottom_profile) {
                ProfileFragment profileFragment = new ProfileFragment();
//                profileFragment.setOnHideFragmentContainerListener(() -> binding.fragmentView.setVisibility(View.GONE));
                replaceFragment(profileFragment);
            }
            else if (menuItem.getItemId() == R.id.bottom_timer) {
                binding.mainlayout.setVisibility(View.VISIBLE);
            }

            return true;
        });




        binding.btnTimer.setOnClickListener(v -> {
            countWork= 0;
            countRest=0;
            restartTimerMillies=0;
            countRestartTimer=0;
            porabotal= true;
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            binding.tvs.setText("Работа");
            binding.btnTimer.setVisibility(View.GONE);
            binding.ll.setVisibility(View.VISIBLE);
            if(milliesec==0){Toast.makeText(context, "Выбранный тип фокусировки 00", Toast.LENGTH_SHORT).show();
            binding.btnTimer.setVisibility(View.VISIBLE);
            binding.ll.setVisibility(View.GONE);}
            else{Timer(milliesec);}

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
                countDownTimer.cancel();
                binding.tvTimer.setText("00:00:00");
                finishTimer();
        });


    }
    private void replaceFragment(Fragment fragment){
        binding.fragmentView.setVisibility(View.VISIBLE);
        binding.mainlayout.setVisibility(View.GONE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentView,fragment )
                .commit();
    }
    private void loadTypeFromFirestore(String selectedType) {
        firestoreGetId.getId(Objects.requireNonNull(mAuth.getCurrentUser()).getUid(), userId1 -> fb.collection("Users")
                .document(userId1)
                .collection("Types")
                .whereEqualTo("name", selectedType) // Укажите значение, с которым сравнивать поле "name"
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Type type = documentSnapshot.toObject(Type.class);
                        milliesec=type.getTimeWork();
                        currentType=type;
                    }
                })
                .addOnFailureListener(e -> {
                    // Обработка ошибки получения данных из Firestore
                }));

    }





    public void Timer(long milliesec){
        savemilliesec=0;
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
                if(currentType==null){
                    Toast.makeText(context, "null type", Toast.LENGTH_SHORT).show();
                }
                else if(currentType.isInterval() && porabotal){
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
        spisok.createSpisok(adapter -> {
            if (adapter != null) {
                setContentView(binding.getRoot());
                binding.filliedExposed.setAdapter(adapter);
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
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()) Toast.makeText(MainActivity.this, "assss", Toast.LENGTH_SHORT).show();

                        });
            }
        });
    }
    public void addTypes(String name, long timeWork){
        firestoreGetId.getId(mAuth.getCurrentUser().getUid(), userId1 -> fb.collection("Users")
                .document(userId1)
                .collection("Types")
                .add(new Type(name, timeWork))
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) Toast.makeText(MainActivity.this, "assss", Toast.LENGTH_SHORT).show();
                }));

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
                .setMessage("Вы хотите удалить ?")
                .setCancelable(true)
                .setPositiveButton("Да", (dialog, which) -> {
                    dialog.cancel();
                })
                .setNegativeButton("Нет ", (dialog, which) -> {
                    dialog.cancel();
                })
                .show();
    }
//    public void AlertDilaog(){
//        builder = new AlertDialog.Builder(this);
//        builder.setTitle("Подтверждение")
//                .setMessage("Вы точно хотите остановить таймер?")
//                .setCancelable(true)
//                .setPositiveButton("Да", (dialog, which) -> {
//                    countDownTimer.cancel();
//                    binding.tvTimer.setText("00:00:00");
//                    binding.btnTimer.setVisibility(View.VISIBLE);
//                    binding.ll.setVisibility(View.GONE);
//                    finishTimer();
//                })
//                .setNegativeButton("Нет ", (dialog, which) -> {
//                    Timer(savemilliesec);
//                    dialog.cancel();
//                })
//                .show();
//    }

    public void finishTimer(){
        binding.btnTimer.setVisibility(View.VISIBLE);
        binding.ll.setVisibility(View.GONE);
        if(!currentType.isInterval()){
            builder = new AlertDialog.Builder(this);
            long finishmillies= (milliesec-savemilliesec)/1000;
            if(countRestartTimer>0){
                finishmillies+=(restartTimerMillies/1000);
            }
            long hour= finishmillies/3600;
            long minutes = (finishmillies-hour*3600)/60;
            long sec= (finishmillies-hour*3600-minutes*60);
            FocusMode focusMode = new FocusMode(currentType.getName(), hour, minutes, sec, currentType.isInterval());
            alertDialogEmotion(focusMode);
        }
        else {
            long workTime=0;
            long restTime=0;
            if(countRestartTimer>0){
                if(restartTimerMillies>0)
                    workTime+=(restartTimerMillies/1000);
                else if (restartRestMillies>0) {
                    restTime+=(restartRestMillies/1000);
                }
            }

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
            FocusMode focusMode = new FocusMode(currentType.getName(), hours, minutes, sec,hoursRest,minutesRest,secRest,countWork,countRest, currentType.isInterval() );
            alertDialogEmotion(focusMode);
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
        emotionUtils.setListeners(this, binding1);
        builder1.setCancelable(false);
        dialog = builder1.setView(view).show();

        emotionUtils.setOnCloseDialogEmotionListener(emotion -> {
            emotionUtils.alertEmotion(fb, mAuth, focusMode, emotion);
            dialog.cancel();
        });
        emotionUtils.setOnNote(emotion -> {
            dialog.cancel();
            emotionUtils.alertNote(MainActivity.this, getLayoutInflater(), fb, mAuth, focusMode, emotion);
        });

    }



    @Override
    public void onButtonTimerClick() {
        binding.fragmentView.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SoundFragment soundFragment = new SoundFragment();
        soundFragment.stopMedia();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(countDownTimer!=null) {
            NotificationHelper.sendNotification(this);
            countDownTimer.cancel();
            countRestartTimer++;
            if(currentType.isInterval()&&porabotal){
                restartRestMillies += currentType.getTimeRest()-savemilliesec;
            }
            else if(currentType.isInterval()&& !porabotal) {
                restartTimerMillies+= currentType.getTimeWork()-savemilliesec;
            }
            else{
                restartTimerMillies+= currentType.getTimeWork()-savemilliesec;
            }
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        if(savemilliesec>0){
            countDownTimer.cancel();
            Timer(savemilliesec);
        }

    }
}
class NotificationHelper {

    private static final String CHANNEL_ID = "1";
    private static final String CHANNEL_NAME = "My Channel";
    public static void sendNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentText("DAAAAAAAAA")
                .setContentTitle("poluchilos")
                .setSmallIcon(R.drawable.img)
                .setAutoCancel(true)
                .setPriority(NotificationManager.IMPORTANCE_HIGH);
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.putExtra("save", savemilliesec);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE);
//        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(1, builder.build());

    }
}






