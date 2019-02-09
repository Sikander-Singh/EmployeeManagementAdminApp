package com.example.mangurkaur.employeeattendancepro.Activties;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangurkaur.employeeattendancepro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {

    Button adminSignIn;
    TextView forget;
    EditText user;
    EditText pass;
    private FirebaseAuth auth;
    private DatabaseReference firebaseObject;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        adminSignIn=findViewById(R.id.login);
        forget=findViewById(R.id.forget);
        user=findViewById(R.id.email);
        pass=findViewById(R.id.pass);
        auth=FirebaseAuth.getInstance();
        progress = new ProgressDialog(LoginActivity.this);
        progress.setMessage("Signing.......");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                    progress.dismiss();

                    if(firebaseAuth.getCurrentUser()!=null){

                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                    }


            }
        };





        adminSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SignIn();

            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(LoginActivity.this,ForgetActivity.class);

                startActivity(intent);

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        auth.addAuthStateListener(authStateListener);
    }
    @Override
    public void onBackPressed(){

      MainActivity main=new MainActivity();
      main.appLogin();
        finish();
    }
    private void SignIn() {

        String emailStr = user.getText().toString();
        String passStr = pass.getText().toString();

        if (TextUtils.isEmpty(emailStr) || TextUtils.isEmpty(passStr)) {


            Toast.makeText(LoginActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();

        } else {

            progress.show();
            auth.signInWithEmailAndPassword(emailStr, passStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Sign in Problem or Internet lost", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                    }
                    pass.setText("");
                    progress.dismiss();
                }
            });

        }


    }
}
