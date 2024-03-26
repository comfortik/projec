package com.example.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import com.example.project.databinding.ActivityMainBinding;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements post {
    int time, minutes, hours,sec;
    long milliesec;
    long savemilliesec;
    boolean isRunning, cancelButton, porabotal;
    CountDownTimer countDownTimer;
    AlertDialog.Builder builder;
    Type currentType;
    ArrayList<Type> types;
    int countRest;
    int countWork ;
    boolean first = true;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());
        types = new ArrayList<>();
        types.add(new Type("Sleep", 6000, 5000));
        types.add(new Type("Study", 50000,5000));
        binding.filliedExposed.setAdapter(Spisok.createSpisok(this, types));
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        binding.btnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
        binding.filliedExposed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                binding.tvTimer.setText("00:00:00");
                countWork=0;
                countRest=0;
                binding.btnTimer.setVisibility(View.VISIBLE);
                binding.ll.setVisibility(View.GONE);
                if(position == types.size()){
                    createNewType();
                }else{

                    Toast.makeText(MainActivity.this, binding.filliedExposed.getText().toString(),Toast.LENGTH_SHORT).show();
                    currentType= types.get(position);
                    milliesec= currentType.getTimeWork();
                }
            }
        });
        binding.btnTimerCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
        binding.btnTimerPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRunning){
                    countDownTimer.cancel();
                    binding.btnTimerPause.setText("Play");
                    isRunning=false;
                } else if (!isRunning) {
                    Timer(savemilliesec);
                    binding.btnTimerPause.setText("Pause");
                    isRunning= true;
                }
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
        Spisok.addType(types, new Type(name, timeWork));
        binding.filliedExposed.setAdapter(Spisok.createSpisok(this, types));
        binding.tvs.setText(name);
    }
    public void postpost(String name, long timeWork, long timeRest){
        binding.fragment.setVisibility(View.GONE);
        Spisok.addType(types, new Type(name, timeWork));
        binding.filliedExposed.setAdapter(Spisok.createSpisok(this, types));
        binding.tvs.setText(name);
    }
    public void AlertDilaog(){
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Подтверждение")
                .setMessage("Вы точно хотите остановить таймер?")
                .setCancelable(true)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        countDownTimer.cancel();
                        binding.tvTimer.setText("00:00:00");
                        binding.btnTimer.setVisibility(View.VISIBLE);
                        binding.ll.setVisibility(View.GONE);
                        finishTimer();
                    }
                })
                .setNegativeButton("Нет ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Timer(savemilliesec);
                        dialog.cancel();
                    }
                })
                .show();
    }
    public void intervalTimer(){

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
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
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
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
        }



    }
}


