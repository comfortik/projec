package com.example.project.Emotion;

import java.util.ArrayList;

public class Emotion {
    public boolean isPosleFocus() {
        return posleFocus;
    }

    public void setPosleFocus(boolean posleFocus) {
        this.posleFocus = posleFocus;
    }

    boolean posleFocus;

    public ArrayList<String> getEmotionWords() {
        return emotionWords;
    }

    public void setEmotionWords(ArrayList<String> emotionWords) {
        this.emotionWords = emotionWords;
    }

    ArrayList<String> emotionWords = new ArrayList<>();
    public Emotion() {
    }

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

    public Emotion(boolean posleFocus, ArrayList<String> emotionWords, int id) {
        this.posleFocus = posleFocus;
        this.emotionWords = emotionWords;
        this.id = id;
    }
}
