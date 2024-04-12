package com.example.project.Main;

import com.example.project.Emotion.Emotion;
import com.example.project.Note.Note;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.security.auth.callback.Callback;

public class DiaryEntry {
    Note note;

    FocusMode focusMode;
    Emotion emotion;
    int id;
    Date date;

    public int getIdDataBase() {
        return idDataBase;
    }

    public void setIdDataBase(int idDataBase) {
        this.idDataBase = idDataBase;
    }
    Random random = new Random();

    int idDataBase;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



    public DiaryEntry(FocusMode focusMode, Emotion emotion) {
        id =1;

        date = new Date();
        this.focusMode = focusMode;
        this.emotion = emotion;
    }
    public DiaryEntry(Note note, FocusMode focusMode, Emotion emotion) {
        id=2;
        date = new Date();
        this.note = note;
        this.focusMode = focusMode;
        this.emotion = emotion;
    }
    public DiaryEntry (Emotion emotion){
        id=3;
        date = new Date();
        this.emotion = emotion;
    }
    public DiaryEntry(Note note,  Emotion emotion) {
        id=4;
        date = new Date();
        this.note = note;
        this.emotion = emotion;
    }


    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public FocusMode getFocusMode() {
        return focusMode;
    }

    public void setFocusMode(FocusMode focusMode) {
        this.focusMode = focusMode;
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }


    //для записи после фокусировки с заметкой





    public DiaryEntry() {
    }
}
