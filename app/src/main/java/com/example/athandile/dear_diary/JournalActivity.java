package com.example.athandile.dear_diary;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.athandile.dear_diary.database.FirestoreCRUD;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.sql.Date;

import butterknife.BindView;

public class JournalActivity extends BaseActivity {

     String id = "";

     @BindView(R.id.edit_title)
     TextView mEditTitle;
     @BindView(R.id.edit_description)
     TextView mEditDescription;
     @BindView(R.id.save_btn)
     Button mSave_btn;

     private Date mnoteDate;

     private FirestoreCRUD db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        Bundle  bundle = getIntent().getExtras();

        mEditTitle = (EditText)findViewById(R.id.edit_title);
        mEditDescription= (EditText)findViewById(R.id.edit_description);
        mSave_btn = (Button)findViewById(R.id.save_btn);
           db = new FirestoreCRUD();

        if(bundle != null){
           id = bundle.getString(getString(R.string.entry_id));
        mSave_btn.setText(getString(R.string.update_button_text));
           mEditTitle.setText(bundle.getString(getString(R.string.entry_title)));
           mEditDescription.setText(bundle.getString(getString(R.string.entry_description)));
           mnoteDate =(Date) bundle.get(getString(R.string.entry_timestamp));
        }

        mSave_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveEntry();
            }
        });


    }


    public void SaveEntry(){
        String title = mEditTitle.getText().toString();
        String description = mEditDescription.getText().toString();

        if(title.length() > 0){
            if(id.length() > 0){
                db.updateEntry(getUid(),title,description,mnoteDate,id).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Snackbar.make(findViewById(R.id.journal_layout),"Entry updated",Snackbar.LENGTH_SHORT)
                                    .show();
                        }

                    }
                });
            }else {
                db.addNewEntry(title,description);
            }
            finish();
        }
    }
}



