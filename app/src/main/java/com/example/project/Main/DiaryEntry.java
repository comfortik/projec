package com.example.project.Main;

import com.example.project.Emotion.Emotion;
import com.example.project.Note.Note;

public class DiaryEntry {
    Note note;

    public DiaryEntry(FocusMode focusMode, Emotion emotion) {
        this.focusMode = focusMode;
        this.emotion = emotion;
    }

    FocusMode focusMode;

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

    Emotion emotion;
    //для записи после фокусировки с заметкой
    public DiaryEntry(Note note, FocusMode focusMode, Emotion emotion) {
        this.note = note;
        this.focusMode = focusMode;
        this.emotion = emotion;
    }


    public DiaryEntry() {
    }
}
