package com.example.athandile.dear_diary.database;



import com.example.athandile.dear_diary.models.JournalEntry;
import com.example.athandile.dear_diary.models.User;
import com.google.android.gms.tasks.Task;

import java.util.Date;
import java.util.List;

public interface journalDao {

    List<JournalEntry> getAllEntries(String uid);

    void addNewEntry(String title,String  description);

    JournalEntry getEntry(String id);

    Task<Void> updateEntry(String uid, String title, String description, Date timestamp, String id);

    Task<Void> deleteEntry(String id);

    boolean addNewUser(User user);
}
