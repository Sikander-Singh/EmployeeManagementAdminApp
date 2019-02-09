package com.example.mangurkaur.employeeattendancepro.Activties;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mangurkaur.employeeattendancepro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetActivity extends AppCompatActivity {

    private EditText email;
    private Button req;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        getSupportActionBar().setTitle("Reset Password");

        email = (EditText)findViewById(R.id.mail);
        req = (Button)findViewById(R.id.don);

        auth = FirebaseAuth.getInstance();

        req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emaill = email.getText().toString().trim();

                if(TextUtils.isEmpty(emaill)){


                    Toast.makeText(getApplicationContext(),"Enter your email", Toast.LENGTH_SHORT).show();

                    return;
                }

                auth.sendPasswordResetEmail(emaill)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                              if(task.isSuccessful()){
                                  Toast.makeText(getApplicationContext(),"Check email to reset your password",Toast.LENGTH_SHORT).show();

                              }else{

                                  Toast.makeText(getApplicationContext(),"Fail to send reset password email",Toast.LENGTH_SHORT).show();
                              }
                            }
                        });

            }
        });
    }
}
