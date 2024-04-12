package com.example.project.Profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.project.Main.DiaryEntry;

import java.util.List;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<List<DiaryEntry>> diaryEntriesLiveData = new MutableLiveData<>();

    public LiveData<List<DiaryEntry>> getDiaryEntries() {
        return diaryEntriesLiveData;
    }

    public void setDiaryEntries(List<DiaryEntry> diaryEntries) {
        diaryEntriesLiveData.setValue(diaryEntries);
    }
}