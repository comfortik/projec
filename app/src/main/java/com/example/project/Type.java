package com.example.project;

public class Type {
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
