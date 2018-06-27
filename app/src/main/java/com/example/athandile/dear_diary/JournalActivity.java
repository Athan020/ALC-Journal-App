package com.example.athandile.dear_diary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class JournalActivity extends AppCompatActivity {

     String id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        Bundle  bundle = getIntent().getExtras();

        if(bundle != null){
           id =
        }
    }
}
