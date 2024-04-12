package com.example.project.Main;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class FocusMode {
    private boolean interval;

    public FocusMode() {
    }


    public void setInterval(boolean interval) {
        this.interval = interval;
    }


    public boolean isInterval() {
        return interval;
    }


    public String getName() {
        return name;
    }

    public long getHour() {
        return hour;
    }

    public long getMinutes() {
        return minutes;
    }

    public long getSec() {
        return sec;
    }

    public long getHourRest() {
        return hourRest;
    }

    public long getMinutesRest() {
        return minutesRest;
    }

    public long getSecRest() {
        return secRest;
    }

    public int getCountWork() {
        return countWork;
    }

    public int getCountRest() {
        return countRest;
    }

    String name;
    long hour;
    long minutes;
    long sec;
    long hourRest;
    long minutesRest;
    long secRest;
    int countWork;
    int countRest;
    //конструктор для неинтервальной фокусировки
    public FocusMode(String name, long hour, long minutes, long sec, boolean interval) {
        this.interval=interval;
        this.name = name;
        this.hour = hour;
        this.minutes = minutes;
        this.sec = sec;
    }

    //конструктор для интервальной фокусировки
    public FocusMode(String name, long hour, long minutes, long sec, long hourRest, long minutesRest, long secRest, int countWork, int countRest, boolean interval) {
        this.interval=true;
        this.name = name;
        this.hour = hour;
        this.minutes = minutes;
        this.sec = sec;
        this.hourRest = hourRest;
        this.minutesRest = minutesRest;
        this.secRest = secRest;
        this.countWork = countWork;
        this.countRest = countRest;
    }
    public String getTime(){
        NumberFormat f = new DecimalFormat("00");
        String time= ("Время фокусировки: "+f.format(hour) + ":" + f.format(minutes) + ":" + f.format(sec));
        return time;
    }
    public String getTimeInterval(){
        NumberFormat f = new DecimalFormat("00");
        String time= ("  Время отдыха: "+f.format(hourRest) + ":" + f.format(minutesRest) + ":" + f.format(secRest));
        return time;
    }

}
