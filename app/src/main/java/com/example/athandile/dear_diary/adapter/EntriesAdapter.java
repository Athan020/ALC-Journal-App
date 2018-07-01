package com.example.athandile.dear_diary.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.athandile.dear_diary.R;
import com.example.athandile.dear_diary.models.JournalEntry;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.example.athandile.dear_diary.adapter.EntriesAdapter.JournalHolder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EntriesAdapter extends FirestoreRecyclerAdapter<JournalEntry,JournalHolder> {

    private List<JournalEntry>  entryList;
    private OnEntryClickHandler mHandler;
    public interface OnEntryClickHandler{
        void onEntryClick(int position);
    }


    public EntriesAdapter(@NonNull FirestoreRecyclerOptions<JournalEntry> options,OnEntryClickHandler handler) {
        super(options);
        mHandler = handler;
    }

    @Override
    protected void onBindViewHolder(@NonNull JournalHolder holder, int position, @NonNull JournalEntry model) {
        holder.bind(model);
    }


    @Override
    public JournalEntry getItem(int position) {
        return super.getItem(position);
    }


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

    class JournalHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        @BindView(R.id.description_tv)
        TextView mdescriptiontView;

        @BindView(R.id.date_tv)
        TextView mdateView;


        @BindView(R.id.title_tv)
        TextView  mtitleView;


        private  final SimpleDateFormat FORMAT  = new SimpleDateFormat(
                "MM/dd/yyyy", Locale.getDefault());

        public JournalHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
        public String trimDescription(String description){
            String content =  description;
            if(content.length()> 50){
                content = content.substring(0,50) + "...";
            }
            return content;
        }

        public void bind(JournalEntry entry){

            mdescriptiontView.setText(entry.getDescription());
            mtitleView.setText(entry.getHeading());

            if(entry.getTimestamp() != null){
                mdateView.setText(FORMAT.format(entry.getTimestamp()));
            }

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mHandler.onEntryClick(position);

        }
    }
}
