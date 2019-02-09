package com.example.mangurkaur.employeeattendancepro.Activties;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mangurkaur.employeeattendancepro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {
    private ImageView empsign;
    private ImageView signout;
    private ImageView admin;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        appLogin();

        empsign = (ImageView) findViewById(R.id.in);

        empsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InOutActivity.class);
                intent.putExtra("key","signIn");
                startActivity(intent);
            }
        });

        signout = (ImageView)findViewById(R.id.out);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InOutActivity.class);
                intent.putExtra("key","signOut");
                startActivity(intent);
            }
        });

        admin = (ImageView) findViewById(R.id.adminin);

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
    public void appLogin() {


        auth=FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword("noreplydelight@gmail.com", "rootadmin123").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Server Problem", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
