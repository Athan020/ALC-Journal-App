package com.example.athandile.dear_diary.ViewModels;

import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;

import com.example.athandile.dear_diary.models.JournalEntry;

import java.util.List;

public class MainViewModel extends ViewModel{

    private String mUserName;
    private List<JournalEntry> mEntries;

    public MainViewModel(String mUserName, List<JournalEntry> mEntries) {
        this.mUserName = mUserName;
        this.mEntries = mEntries;
    }

}
