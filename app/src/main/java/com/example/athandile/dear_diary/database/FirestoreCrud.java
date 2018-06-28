package com.example.athandile.dear_diary.database;

import android.content.Context;
import android.view.View;

import com.example.athandile.dear_diary.R;
import com.example.athandile.dear_diary.models.JournalEntry;
import com.example.athandile.dear_diary.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

public class FirestoreCrud implements  journalDao {

    private FirebaseFirestore  firestore;
    private List<JournalEntry> entries;
    private FirebaseAuth mAuth;
    private Context mcontext;



    private View mLayout;
    public FirestoreCrud() {
        this.firestore = FirebaseFirestore.getInstance();
        this.mAuth =  FirebaseAuth.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        this.firestore.setFirestoreSettings(settings);
    }

    @Override
    public List<JournalEntry> getAllEntries(String uid) {
      firestore.collection("Entries")
              .whereEqualTo("uid",uid)
              .addSnapshotListener(new EventListener<QuerySnapshot>() {
                  @Override
                  public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                  }



              });
        return entries;
    }

    @Override
    public void addNewEntry(String title,String  description) {
       String uid = mAuth.getCurrentUser().getUid();
        Date timeStamp = new Date();
        JournalEntry entry = new JournalEntry(uid,title,description,timeStamp);
        firestore.collection("entries")
                .add(entry);
    }

    @Override
    public JournalEntry getEntry(String id) {
        return null;
    }

    @Override
    public Task<Void> updateEntry(String uid, String title, String description,Date timestamp, String id) {
        JournalEntry journalEntry= new JournalEntry(uid,title,description,timestamp);
       return firestore.collection("entries")
                .document(id)
                .set(journalEntry);
    }

    @Override
    public Task<Void> deleteEntry(String id) {

       return firestore.collection("entries")
                .document(id)
                .delete();

    }

    @Override
    public boolean addNewUser(User user) {
      return  firestore.collection("users")
                .add(user).isSuccessful();
    }
}
