package com.example.mangurkaur.employeeattendancepro.Activties;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mangurkaur.employeeattendancepro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {

    private EditText newpass;
    FirebaseAuth auth;
    private Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings");

        newpass = (EditText) findViewById(R.id.newpw);
        update = (Button) findViewById(R.id.saveee);
        auth = FirebaseAuth.getInstance();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {

                    user.updatePassword(newpass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Password is changed", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Password does not changed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


    }


    }
