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

public  class JournalHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

    @BindView(R.id.description_tv)
    TextView mdescriptiontView;

    @BindView(R.id.date_tv)
    TextView mdateView;


    @BindView(R.id.title_tv)
    TextView  mtitleView;


    private static final SimpleDateFormat FORMAT  = new SimpleDateFormat(
            "MM/dd/yyyy", Locale.getDefault());

    public String trimDescription(String description){
        String content =  description;
        if(content.length()> 50){
            content = content.substring(0,50) + "...";
        }
        return content;
    }
    public JournalHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);

    }

    public void bind(JournalEntry entry){

        mdescriptiontView.setText(trimDescription(entry.getDescription()));
        mtitleView.setText(entry.getHeading());

        if(entry.getTimestamp() != null){
            mdateView.setText(FORMAT.format(entry.getTimestamp()));
        }
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();

    }
}
