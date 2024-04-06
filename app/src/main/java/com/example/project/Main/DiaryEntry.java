package com.example.project.Main;

import com.example.project.Emotion.Emotion;
import com.example.project.Note.Note;

public class DiaryEntry {
    Note note;
    FocusMode focusMode;
    Emotion emotion;
    //для записи после фокусировки с заметкой
    public DiaryEntry(Note note, FocusMode focusMode, Emotion emotion) {
        this.note = note;
        this.focusMode = focusMode;
        this.emotion = emotion;
    }


}
