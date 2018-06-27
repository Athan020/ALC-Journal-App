package com.example.athandile.dear_diary.database;



import com.example.athandile.dear_diary.models.JournalEntry;
import com.google.android.gms.tasks.Task;

import java.util.List;

public interface journalDao {

    List<JournalEntry> getAllEntries(String uid);

    void addNewEntry(String title,String  description);

    JournalEntry getEntry(String id);

    Task<Void> updateEntry(JournalEntry journalEntry, String id);

    Task<Void> deleteEntry(String id);
}
