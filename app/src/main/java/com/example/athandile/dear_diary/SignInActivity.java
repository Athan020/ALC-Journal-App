package com.example.athandile.dear_diary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.athandile.dear_diary.database.FirestoreCrud;
import com.example.athandile.dear_diary.models.User;
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
    private GoogleSignInClient mGoogleSignInClient;
    private FirestoreCrud db;
    private static final int RC_SIGN_IN = 672;
    @BindView(R.id.sign_in_button)
    SignInButton mGoogleSignIn;

    TextView  mEmail;

    TextView mPassword;

    Button mSignup_btn;
    Button mSignIn_btn;


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

        mGoogleSignIn.setSize(SignInButton.SIZE_WIDE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(getCurrentUser() != null){
            mGoogleSignIn.setEnabled(false);
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

                    Snackbar.make(findViewById(R.id.sign_in_layout),"Seems To be A problem with google",Snackbar.LENGTH_SHORT)
                            .show();

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

    private void EmailAndPasswordAuth(){
        showProgressDialog();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        mFirebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                    }
                });
    }
    private void EmailAndPasswordSignUp(){
        showProgressDialog();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();


        mFirebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        hideProgressDialog();

                        if(task.isSuccessful()){
                            User user = new User(getUid(),getCurrentUser().getDisplayName(),getCurrentUser().getEmail());
                            db.addNewUser(user);
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }
                        else {
                            Snackbar.make(findViewById(R.id.sign_in_layout),"Oops Something went wrong with the Regestration",Snackbar.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }


}
