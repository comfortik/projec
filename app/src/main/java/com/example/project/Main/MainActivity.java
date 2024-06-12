package com.example.project.Main;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.project.Emotion.EmotionDiaryFragment;
import com.example.project.Emotion.EmotionUtils;
import com.example.project.Emotion.ReactForEmotions;
import com.example.project.MessageService;
import com.example.project.OnHideFragmentContainerListener;
import com.example.project.Profile.ProfileFragment;
import com.example.project.R;
import com.example.project.Sounds.SoundFragment;
import com.example.project.Stata.StataFragment;
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
    int countWork ;
    Spisok spisok;
    private ActivityMainBinding binding;
    FirebaseFirestore fb;
    FirebaseAuth mAuth;
    FirestoreGetId firestoreGetId;
    AddUserToFirebase add;
    int countRestartTimer;
    Context context = MainActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
        View customLoadingView = getLayoutInflater().inflate(R.layout.splash_screen, null);

        setContentView(customLoadingView);
        fb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        startService(new Intent(this, MessageService.class));
        spisok = new Spisok(this , fb, mAuth.getCurrentUser());
        add= new AddUserToFirebase(this, fb, mAuth);
        add.anonimouseSignUp();
        firestoreGetId = new FirestoreGetId(fb);
        add.setOnAddUserToFirestore(this::getTypes);
        binding.filliedExposed.setDropDownBackgroundResource(R.drawable.png);
        final int NOTIFICATION_PERMISSION_CODE = 123;
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
            }
        }
        binding.bottomNavigationView.setSelectedItemId(R.id.bottom_timer);
        binding.bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.bottom_emotionDiary) {
                replaceFragment(new EmotionDiaryFragment());
            } else if (menuItem.getItemId() == R.id.bottom_sound) {
                replaceFragment(new SoundFragment());
            } else if (menuItem.getItemId() == R.id.bottom_profile) {
                replaceFragment(new ProfileFragment());
            }else if(menuItem.getItemId()==R.id.bottom_stata){
                replaceFragment(new StataFragment(fb, mAuth));
            }
            else if (menuItem.getItemId() == R.id.bottom_timer) {
                binding.mainlayout.setVisibility(View.VISIBLE);
            }


            return true;
        });




        binding.btnTimer.setOnClickListener(v -> {
            countWork= 0;
            countRest=0;
            countRestartTimer=0;
            porabotal= true;
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            if (currentType != null) {
                binding.tvs.setText("Работа");
                binding.btnTimer.setVisibility(View.GONE);
                binding.ll.setVisibility(View.VISIBLE);
                if(milliesec==0){Toast.makeText(context, "Выбранный тип фокусировки 00", Toast.LENGTH_SHORT).show();
                    binding.btnTimer.setVisibility(View.VISIBLE);
                    binding.ll.setVisibility(View.GONE);}
                else{Timer(milliesec);}
            }
            else{
                Toast.makeText(context, "Повтори", Toast.LENGTH_SHORT).show();
            }


        });
        binding.filliedExposed.setOnItemClickListener((parent, view, position, id) -> {
            if (countDownTimer != null) {
                countDownTimer.cancel();
                finishTimer();
            }

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
        binding.btnTimerCancel.setOnClickListener(v -> AlertDilaog());


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
                .whereEqualTo("name", selectedType)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Type type = documentSnapshot.toObject(Type.class);
                        milliesec=type.getTimeWork();
                        currentType=type;
                        NumberFormat f = new DecimalFormat("00");
                        long totalSeconds = milliesec / 1000;
                        long hour = totalSeconds / 3600;
                        long min = (totalSeconds % 3600) / 60;
                        long sec = totalSeconds % 60;
                        if(getIntent().getLongExtra("save",0)==0){
                            binding.tvTimer.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                        }

                    }
                })
                .addOnFailureListener(e -> {
                }));
    }
    public void soundNotification(){
        MediaPlayer mp ;
        mp= MediaPlayer.create(this, R.raw.pap);
        mp.start();
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
                    Toast.makeText(context, "Null type", Toast.LENGTH_SHORT).show();
                }
                else if(currentType.isInterval() && porabotal){
                    try{
                        Timer(currentType.getTimeRest());
                    }catch(NullPointerException e){
                        Toast.makeText(MainActivity.this, "Время отдыха 00", Toast.LENGTH_SHORT).show();
                    }
                    soundNotification();
                    porabotal=false;
                    countWork++;
                    binding.tvs.setText("Отдых");
                } else if (currentType.isInterval()&&!porabotal){
                    try{
                        Timer(currentType.getTimeWork());
                    }catch(NullPointerException e){
                        Toast.makeText(MainActivity.this, "Время работы 00", Toast.LENGTH_SHORT).show();
                    }
                    soundNotification();
                    porabotal=true;
                    countRest++;
                    binding.tvs.setText("Работа");
                } else{
                    binding.tvTimer.setText("00:00:00");
                    soundNotification();
                    binding.filliedExposed.setText(" ");
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
        binding.filliedExposed.setText(null);
        binding.fragment.setVisibility(View.GONE);
        addTypes(name,timeWork);
        getTypes();
    }
    public void getTypes(){
        spisok.createSpisok(adapter -> {
            if (adapter != null) {
                if(getIntent().getLongExtra("save",0)!=0){
                    savemilliesec= getIntent().getLongExtra("save", 0);
                    if(savemilliesec>0){
                        Timer(savemilliesec);
                        binding.btnTimer.setVisibility(View.GONE);
                        binding.ll.setVisibility(View.VISIBLE);
                        loadTypeFromFirestore(getIntent().getStringExtra("name"));
                        if(getIntent().getBooleanExtra("interval", false)){
                            countWork =getIntent().getIntExtra("work", 0);
                            countRest = getIntent().getIntExtra("rest",0);
                        }
                        porabotal = getIntent().getBooleanExtra("boo", false);
                    }

                }
                setContentView(binding.getRoot());
                binding.filliedExposed.setAdapter(adapter);

            }
        });
    }

    public void addTypes(String name, long timeWork, long timeRest){
        firestoreGetId.getId(Objects.requireNonNull(mAuth.getCurrentUser()).getUid(), userId1 -> {
            if(userId1!=null){
                fb.collection("Users")
                        .document(userId1)
                        .collection("Types")
                        .add(new Type(name, timeWork,timeRest))
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()) Toast.makeText(MainActivity.this, "Добавлен новый тип", Toast.LENGTH_SHORT).show();

                        });
            }
        });
    }
    public void addTypes(String name, long timeWork){
        firestoreGetId.getId(Objects.requireNonNull(mAuth.getCurrentUser()).getUid(), userId1 -> fb.collection("Users")
                .document(userId1)
                .collection("Types")
                .add(new Type(name, timeWork))
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) Toast.makeText(MainActivity.this, "Добавлен новый тип", Toast.LENGTH_SHORT).show();
                }));

    }
    public void postpost(String name, long timeWork, long timeRest){
        binding.fragment.setVisibility(View.GONE);
        binding.filliedExposed.setText(null);
        addTypes(name, timeWork, timeRest);
        getTypes();
    }
    public void AlertDilaog(){
        builder = new AlertDialog.Builder(this, R.style.AlertDialogText);
        countDownTimer.cancel();
        builder.setTitle("Подтверждение")
                .setMessage("Вы точно хотите остановить таймер?")
                .setCancelable(true)
                .setPositiveButton("Да", (dialog, which) -> {
                    cancelButton=true;
                    binding.tvTimer.setText("00:00:00");
                    binding.filliedExposed.setText(null);
                    finishTimer();
                })
                .setNegativeButton("Нет ", (dialog, which) -> {
                    Timer(savemilliesec);
                    dialog.cancel();
                });
        GradientDrawable gradientDrawable= new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(Color.parseColor("#90A4AE"));
        gradientDrawable.setCornerRadius(10);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(gradientDrawable);
        alertDialog.show();

    }
    public FocusMode getHoursMinutesSeconds(long timeWork, long timeRest){
        if(timeRest==-1){
            timeWork+=1;
            long hour= timeWork/3600;
            long minutes = (timeWork-hour*3600)/60;
            long sec= (timeWork-hour*3600-minutes*60);
            FocusMode focusMode = new FocusMode(currentType.getName(), hour, minutes, sec, currentType.isInterval());
            currentType=null;
            return focusMode;
        }
        else{
            long hours= timeWork/3600;
            long minutes = (timeWork-hours*3600)/60;
            long sec = (timeWork-hours*3600-minutes*60);
            long hoursRest= timeRest/3600;
            long minutesRest = (timeRest-hoursRest*3600)/60;
            long secRest = (timeRest-hoursRest*3600-minutesRest*60);
            FocusMode focusMode = new FocusMode(currentType.getName(), hours, minutes, sec,hoursRest,minutesRest,secRest,countWork,countRest, currentType.isInterval() );
            currentType=null;
            return focusMode;
        }

    }

    public void finishTimer(){
        binding.btnTimer.setVisibility(View.VISIBLE);
        binding.ll.setVisibility(View.GONE);
        if(!currentType.isInterval()){
            builder = new AlertDialog.Builder(this);
            long finishmillies= (milliesec-savemilliesec)/1000;
            countDownTimer=null;
            savemilliesec=0;
            porabotal=true;
            alertDialogEmotion(getHoursMinutesSeconds(finishmillies, -1));
        }
        else {
            long workTime=0;
            long restTime=0;
//            if (countWork>0)++workTime;
//            if(countRest>0) ++restTime;
            if(porabotal){
                workTime += countWork*currentType.getTimeWork()/1000+(currentType.getTimeWork()-savemilliesec)/1000;
                restTime += countRest*currentType.getTimeRest()/1000;
            }
            else if(!porabotal){
                workTime += countWork*currentType.getTimeWork()/1000;
                restTime += countRest*currentType.getTimeRest()/1000+(currentType.getTimeRest()-savemilliesec)/1000;
            }
            countDownTimer=null;
            savemilliesec=0;
            porabotal=true;
            alertDialogEmotion(getHoursMinutesSeconds(workTime, restTime));
        }

    }
    private void alertDialogEmotion(FocusMode focusMode) {
        AlertDialog dialog;
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

        ReactForEmotions reactForEmotions = new ReactForEmotions();
        EmotionUtils emotionUtils = new EmotionUtils(reactForEmotions);
        FragmentEmotionDiaryBinding binding1 = FragmentEmotionDiaryBinding.inflate(getLayoutInflater(), null, false);

        View view = binding1.getRoot();

        emotionUtils.setListeners(this, binding1);
        builder1.setCancelable(false);
        dialog = builder1.setView(view).show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        binding1.main.setBackgroundResource(R.drawable.alert_profile);



        emotionUtils.setOnCloseDialogEmotionListener(emotion -> {
            emotionUtils.alertEmotion(fb, mAuth, focusMode, emotion);
            dialog.cancel();
        });
        emotionUtils.setOnNote(emotion -> {
            dialog.cancel();
            emotionUtils.alertNote(MainActivity.this, getLayoutInflater(), fb, mAuth, focusMode, emotion);
        });
        binding1.scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding1.flexbox.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ViewGroup.LayoutParams layoutParams = binding1.scrollView.getLayoutParams();
                layoutParams.height = 500;
                binding1.scrollView.setVisibility(View.GONE);
                binding1.scrollView.setLayoutParams(layoutParams);
            }
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
        soundFragment.releaseMedia();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(countDownTimer!=null) {
            NotificationHelper.sendNotification(this, savemilliesec, porabotal, currentType.getName(), countWork, countRest, currentType.isInterval());
            countDownTimer.cancel();
            countRestartTimer++;
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
    private void checkAndShowAlert() {
        SharedPreferences prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        boolean showAlert = prefs.getBoolean("show_alert", false);

        if (showAlert) {
            new AlertDialog.Builder(this)
                    .setTitle("Напоминание")
                    .setMessage("Пожалуйста, проверьте свое самочувствие.")
                    .setPositiveButton("OK", (dialog, which) -> {
                        prefs.edit().putBoolean("show_alert", false).apply();
                    })
                    .setCancelable(false)
                    .show();
        }
    }
}
class NotificationHelper {

    private static final String CHANNEL_ID = "1";
    private static final String CHANNEL_NAME = "My Channel";
    public static void sendNotification(Context context, long savemilliesec, boolean porabotal, String typeName, int timeWork, int timeRest, boolean isinterval) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentText("Фокусировка не закончена")
                .setContentTitle("Вернись")
                .setSmallIcon(R.drawable.img)
                .setAutoCancel(true)
                .setPriority(NotificationManager.IMPORTANCE_HIGH);
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("save",savemilliesec);
        intent.putExtra("boo", porabotal);
        intent.putExtra("name", typeName);
        intent.putExtra("work", timeWork);
        intent.putExtra("rest", timeRest);
        intent.putExtra("interval", isinterval);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        for(int i=0; i<10; i++){
            notificationManager.notify(i, builder.build());
        }


    }

}






