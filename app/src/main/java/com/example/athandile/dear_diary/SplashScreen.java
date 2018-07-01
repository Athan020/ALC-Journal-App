package com.example.athandile.dear_diary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            if(getCurrentUser()!=null){
                startActivity(new Intent(this,MainActivity.class));
                finish();
            }else {
                startActivity(new Intent(this,SignInActivity.class));
                finish();
            }
    }
}
