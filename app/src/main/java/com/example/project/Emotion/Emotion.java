package com.example.project.Emotion;

public class Emotion {
    boolean posleFocus;

    public int getId() {
        return id;
    }

    int id;
    public Emotion(int id){
        this.id = id;
        posleFocus=false;
    }
    public Emotion(int id,  boolean posleFocus){
         this. id =id;
         this.posleFocus=posleFocus;
    }

}
