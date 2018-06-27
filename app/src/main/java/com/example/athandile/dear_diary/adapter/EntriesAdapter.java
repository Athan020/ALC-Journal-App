package com.example.athandile.dear_diary.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.athandile.dear_diary.R;
import com.example.athandile.dear_diary.models.JournalEntry;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

public class EntriesAdapter extends FirestoreRecyclerAdapter<JournalEntry,JournalHolder>  {

    private List<JournalEntry>  entryList;
    private Context context;


    public EntriesAdapter(@NonNull FirestoreRecyclerOptions<JournalEntry> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull JournalHolder holder, int position, @NonNull JournalEntry model) {
        holder.bind(model);

    }


    @NonNull
    @Override
    public JournalEntry getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public JournalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.journal_entry_layout,parent,false);

        JournalHolder holder = new JournalHolder(view);
        return holder;

    }



    @Override
    public void onError(@NonNull FirebaseFirestoreException e) {
        super.onError(e);
        Log.e("error", e.getMessage());
    }
}
