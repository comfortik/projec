package com.example.project.Main;

public class FocusMode {
    public boolean isInterval() {
        return Interval;
    }

    boolean Interval;


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
    public FocusMode(String name, long hour, long minutes, long sec) {
        this.name = name;
        this.hour = hour;
        this.minutes = minutes;
        this.sec = sec;
    }

    //конструктор для интервальной фокусировки
    public FocusMode(String name, long hour, long minutes, long sec, long hourRest, long minutesRest, long secRest, int countWork, int countRest) {
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
}
