package com.example.athandile.dear_diary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.example.athandile.dear_diary.adapter.EntriesAdapter;
import com.example.athandile.dear_diary.database.FirestoreCRUD;
import com.example.athandile.dear_diary.fragments.DetailViewFragment;
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

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class MainActivity extends BaseActivity implements EntriesAdapter.OnEntryClickHandler {


    @BindView(R.id.fab)FloatingActionButton fab;

    @BindView(R.id.recyclerViewEntries)
    RecyclerView mRecyclerView;



    private FirebaseAuth mAuth;
    private FirestoreCRUD db;
    private ListenerRegistration mFirestoreListener;
    private EntriesAdapter mAdapter;
    private FirebaseFirestore firestoreDb;
    private ArrayList<JournalEntry> entries;
    private DetailViewFragment detail;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getCurrentUser().getDisplayName()!= null){
            getSupportActionBar().setTitle("Hi " + getCurrentUser().getDisplayName());
        }
        db = new FirestoreCRUD();
        detail = new DetailViewFragment();
        fab =(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),JournalActivity.class));
            }
        });

        ft = getSupportFragmentManager().beginTransaction();

        populateEntriesList();
        firestoreDb = FirebaseFirestore.getInstance();
        mFirestoreListener = firestoreDb.collection("entries")
                .whereEqualTo("uid",getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if( e != null) {
                            Log.e("LISTEN FAILED", "Listen failed!", e);
                            return;
                        }

                        entries = new ArrayList<JournalEntry>();

                        for (DocumentSnapshot doc:queryDocumentSnapshots) {
                            JournalEntry entry = doc.toObject(JournalEntry.class);
                            entry.setId(doc.getId());
                            Log.d("Document",entry.getDescription());

                            entries.add(entry);

                        }
                        if(entries.isEmpty()){
                            mRecyclerView.setVisibility(View.INVISIBLE);
                        }
                        mAdapter.notifyDataSetChanged();

                        mRecyclerView.setAdapter(mAdapter);
                    }
                });

    }


    @Override
    protected void onStart() {
        super.onStart();
       mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirestoreListener.remove();
    }



    private void populateEntriesList(){



        //Define Query
        Query q = FirebaseFirestore.getInstance()
                .collection("entries")
                .whereEqualTo("uid",getUid());

        //Build Options For adapter
        FirestoreRecyclerOptions<JournalEntry> response = new FirestoreRecyclerOptions.Builder<JournalEntry>()
                .setQuery(q,JournalEntry.class)
                .build();
        mAdapter = new EntriesAdapter(response,this);

        mAdapter.notifyDataSetChanged();

        mRecyclerView =(RecyclerView)findViewById(R.id.recyclerViewEntries);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                //Get Position on The Reccler View
                int position  =  viewHolder.getAdapterPosition();

               final String JournalId = entries.get(position).getId();
                db.deleteEntry(JournalId)
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

    @Override
    public void onEntryClick(int position) {
        JournalEntry entry = entries.get(position);

        Intent viewDataIntent = new Intent(this,JournalActivity.class);

        viewDataIntent.putExtra(getString(R.string.entry_id),entry.getId());
        viewDataIntent.putExtra(getString(R.string.entry_title),entry.getHeading());
        viewDataIntent.putExtra(getString(R.string.entry_description),entry.getDescription());

        startActivity(viewDataIntent);
    }
}
