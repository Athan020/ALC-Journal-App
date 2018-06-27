package com.example.athandile.dear_diary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends BaseActivity{

    //Sign in Dependecies
    private FirebaseAuth mFirebaseAuth;
    private ProgressDialog mProgressDialog;
    private static final int RC_SIGN_IN = 672;
    @BindView(R.id.sign_in_button)
    SignInButton mSignInBtn;

    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        mSignInBtn.setSize(SignInButton.SIZE_WIDE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(getCurrentUser() != null){
            mSignInBtn.setEnabled(false);
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent,RC_SIGN_IN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case RC_SIGN_IN:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    GoogleAuth(account);
                } catch (ApiException e) {

                   Log.w("Fail Sign IN", "Google sign in failed", e);

                    //updateUI(null);

                };
                break;
        }
    }

    @OnClick(R.id.sign_in_button)
    public void signIn(View v){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    private void GoogleAuth(GoogleSignInAccount acc){

        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(acc.getIdToken(),null);

        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            Snackbar.make(findViewById(R.id.sign_in_layout),"Authentication Success",Snackbar.LENGTH_SHORT).show();
                            hideProgressDialog();
                          Intent dashboardIntent = new Intent(getApplicationContext(),MainActivity.class);
                          startActivity(dashboardIntent);
                        }else{
                        Snackbar.make(findViewById(R.id.sign_in_layout),"Authentication Failed",Snackbar.LENGTH_SHORT).show();
                        }

                    }

                });

    }





//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        if(id == R.id.sign_in_button){
//
//        }
//    }
}
