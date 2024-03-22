package com.example.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements post {
    int time, minutes, hours,sec;
    public MaterialButton btnTimer, btnCancel, btnPause ;
    TextView tvTimer;
    long milliesec;
    long savemilliesec;
    boolean isRunning, cancelButton, porabotal;
    TimePicker timePicker;
    CountDownTimer countDownTimer;
    LinearLayout ll;
    AlertDialog.Builder builder;
    Type currentType;
    FragmentContainerView fragmentContainerView;
    ArrayList<Type> types;
    AutoCompleteTextView autoCompleteTextView;
    String newName;
    int countRest;
    int countWork ;
    long intervalTimeWork, intervalTimeRest;
    String [] typeName;
    boolean first = true;
    TextView tvs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTimer = findViewById(R.id.btnTimer);
        btnCancel= findViewById(R.id.btnTimerCancel);
        btnPause= findViewById(R.id.btnTimerPause);
        tvTimer= findViewById(R.id.tvTimer);
        fragmentContainerView= findViewById(R.id.fragment);
        autoCompleteTextView= findViewById(R.id.fillied_exposed);
        ll= findViewById(R.id.ll);
        tvs= findViewById(R.id.tvs);
        types = new ArrayList<>();
        types.add(new Type("Sleep", 6000, 5000));
        types.add(new Type("Study", 50000,5000));
        createSpisok();
        btnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countWork= 0;
                countRest=0;
                porabotal= true;
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                tvs.setText("Работа");
                btnTimer.setVisibility(View.GONE);
                ll.setVisibility(View.VISIBLE);
                Timer(milliesec);
            }
        });
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == types.size()){
                    createNewType();
                }else{
                    Toast.makeText(MainActivity.this, autoCompleteTextView.getText().toString(),Toast.LENGTH_SHORT).show();
                    currentType= types.get(position);
                    milliesec= currentType.getTimeWork();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelButton=true;
                if(!currentType.isInterval()){
                    countDownTimer.cancel();
                    AlertDilaog();
                }
                else{
                    countDownTimer.cancel();
                    tvs.setText((countRest+" "+countWork).toString());
                    AlertDilaog();
                }
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRunning){
                    countDownTimer.cancel();
                    btnPause.setText("Play");
                    isRunning=false;
                } else if (!isRunning) {
                    Timer(savemilliesec);
                    btnPause.setText("Pause");
                    isRunning= true;
                }
            }
        });
    }
    public void createNewType(){
        fragmentContainerView.setVisibility(View.VISIBLE);
    }
    public void createSpisok(){
        String [] typeName = new String[types.size()+1];
        String [] typeInterval =new String[types.size()+1];
        for(int i=0; i<types.size();i++){
            Type type = types.get(i);
            typeName[i] = type.getName();
            if(type.isInterval()) typeInterval[i]="I";
            else typeInterval[i]="N";
        }
        typeName[types.size()]= "Создать свой режим";
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
//                R.id.mainText,
                typeName
        );
//        ArrayAdapter<String> typeInt = new ArrayAdapter<>(
//                this,
//                R.layout.drop_down_item,
//                R.id.sideSymbol,
//                typeInterval);
        autoCompleteTextView.setAdapter(adapter);
//        autoCompleteTextView.setAdapter(typeInt);
    }
    public void addspisok(Type newType){
        types.add(newType);
        createSpisok();
    }
    public void postpost(String name, long timeWork){
        fragmentContainerView.setVisibility(View.GONE);
        addspisok(new Type(name, timeWork));
        TextView tv = findViewById(R.id.tvs);
        tv.setText(name);
    }
    public void postpost(String name, long timeWork, long timeRest){
        fragmentContainerView.setVisibility(View.GONE);
        addspisok(new Type(name, timeWork, timeRest));
        TextView tv = findViewById(R.id.tvs);
        tv.setText(name);
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
                tvTimer.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }
            @Override
            public void onFinish() {
                if(currentType.isInterval() && porabotal){
                    Timer(currentType.getTimeRest());
                    porabotal=false;
                    countRest++;
                    tvs.setText("Отдых");
                } else if (currentType.isInterval()&&!porabotal){
                    Timer(currentType.getTimeWork());
                    porabotal=true;
                    countWork++;
                    tvs.setText("Работа");
                } else{
                    tvTimer.setText("00:00:00");
                    finishTimer();
                }



            }
        }.start();
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
                        tvTimer.setText("00:00:00");
                        btnTimer.setVisibility(View.VISIBLE);
                        ll.setVisibility(View.GONE);
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
class Type {
    String name;
    long timeWork, timeRest;
    boolean interval;
    public Type(String name, long timeWork, long timeRest){
        this.name = name;
        this.timeWork=timeWork;
        this.timeRest= timeRest;
        interval= true;
    }

    public boolean isInterval() {
        return interval;
    }

    public void setInterval(boolean interval) {
        this.interval = interval;
    }

    public Type(String name, long timeWork){
        this.name = name;
        this.timeWork= timeWork;
        interval = false;
    }
    public long getTimeWork() {
        return timeWork;
    }

    public void setTimeWork(long timeWork) {
        this.timeWork = timeWork;
    }

    public long getTimeRest() {
        return timeRest;
    }

    public void setTimeRest(long timeRest) {
        this.timeRest = timeRest;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
