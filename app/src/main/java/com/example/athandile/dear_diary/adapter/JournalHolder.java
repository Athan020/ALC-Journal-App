package com.example.athandile.dear_diary.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.athandile.dear_diary.R;
import com.example.athandile.dear_diary.models.JournalEntry;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public  class JournalHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.description_tv)
    TextView mdescriptiontView;

    @BindView(R.id.date_tv)
    TextView mdateView;


    @BindView(R.id.title_tv)
    TextView  mtitleView;


    private static final SimpleDateFormat FORMAT  = new SimpleDateFormat(
            "MM/dd/yyyy", Locale.getDefault());

    public JournalHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);

    }

    public void bind(JournalEntry entry){

        mdescriptiontView.setText(entry.getDescription());
        mtitleView.setText(entry.getHeading());

        if(entry.getTimestamp() != null){
            mdateView.setText(FORMAT.format(entry.getTimestamp()));
        }

    }
}
