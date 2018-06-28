package com.example.athandile.dear_diary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.athandile.dear_diary.adapter.EntriesAdapter;
import com.example.athandile.dear_diary.database.FirestoreCrud;
import com.example.athandile.dear_diary.models.JournalEntry;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class MainActivity extends BaseActivity implements EntriesAdapter.OnEntryClickHandler {


    @BindView(R.id.fab)
     FloatingActionButton fab;
    @BindViews(R.id.recyclerViewEntries)
    RecyclerView mRecyclerView;

    private FirebaseAuth mAuth;
    private FirestoreCrud db;
    private ListenerRegistration mFirestoreListener;
    private EntriesAdapter mAdapter;
    private FirebaseFirestore firestoreDb;
    private ArrayList<JournalEntry> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new FirestoreCrud(this);

        mFirestoreListener = firestoreDb.collection(getString(R.string.entries_collection))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if( e == null) {
                            return;
                        }

                        entries = new ArrayList<JournalEntry>();

                        for (DocumentSnapshot doc:queryDocumentSnapshots) {
                            JournalEntry entry = doc.toObject(JournalEntry.class);
                            entry.setId(doc.getId());

                            entries.add(entry);

                        }
                    }
                });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.startListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirestoreListener.remove();
    }

    private void popuplateEntriesList(){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //Define Query
        Query q = FirebaseFirestore.getInstance()
                .collection(getString(R.string.entries_collection))
                .whereEqualTo("uid",getUid());

        //Build Options For adapter
        FirestoreRecyclerOptions<JournalEntry> response = new FirestoreRecyclerOptions.Builder<JournalEntry>()
                .setQuery(q,JournalEntry.class)
                .build();
        mAdapter = new EntriesAdapter(response,this);

        mAdapter.notifyDataSetChanged();

        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                //Get Position on The Reccler View
                int position  =  viewHolder.getAdapterPosition();
                //
               String JournalId = entries.get(position).getId();

                db .deleteEntry(JournalId)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Snackbar.make(findViewById(R.id.main_layout),"Entry Deleted",Snackbar.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(findViewById(R.id.main_layout),"Error Deleting Entry",Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    private void setUpViewModel(){


    }

    @Override
    public void onEntryClick(int position) {
        JournalEntry entry = entries.get(position);
        Intent  updateNoteIntent = new Intent(this,JournalActivity.class);
        updateNoteIntent.putExtra(getString(R.string.entry_id),entry.getId());
        updateNoteIntent.putExtra(getString(R.string.entry_title),entry.getHeading());
        updateNoteIntent.putExtra(getString(R.string.entry_description),entry.getDescription());
        startActivity(updateNoteIntent);
    }
}
