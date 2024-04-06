package com.example.project.Main;

public class FocusMode {


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
