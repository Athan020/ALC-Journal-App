package com.example.athandile.dear_diary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.athandile.dear_diary.database.FirestoreCrud;

import butterknife.BindViews;

public class JournalActivity extends BaseActivity {

     String id = "";

     TextView mEditTitle;
     TextView mEditDescription;
     Button mSave_btn;

     private FirestoreCrud db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        Bundle  bundle = getIntent().getExtras();

        if(bundle != null){
           id = bundle.get(getString(R.string.entry_id)).toString();

           mEditTitle.setText(bundle.get(getString(R.string.entry_title)).toString());
           mEditDescription.setText(bundle.get(getString(R.string.entry_description)).toString());
        }
    }

    public void SaveEntry(){
        String title = mEditTitle.getText().toString();
        String description = mEditDescription.getText().toString();
        if(title.length() > 0){
            if(id.length() > 0){
                db.updateEntry(id);
            }else {
                db.addNewEntry(title,description);
            }
        }
    }
}



