package com.example.mangurkaur.employeeattendancepro.Activties;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mangurkaur.employeeattendancepro.R;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmailChange extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private EditText emailChange;
    private EditText emailPassChange;
    private Button updateEmail;
    private  EmailClass emailClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_change);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        emailChange=(EditText)findViewById(R.id.emailChange);
        emailPassChange=(EditText)findViewById(R.id.emailPassChange);
        updateEmail=findViewById(R.id.updateEmail);
        updateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailClass=new EmailClass();
                emailClass.setEmail(emailChange.getText().toString());
                emailClass.setPass(emailPassChange.getText().toString());
                databaseReference.child("Email").removeValue();
                databaseReference.child("Email").child(databaseReference.push().getKey()).setValue(emailClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(!task.isSuccessful()){

                            Toast.makeText(getApplicationContext(),"Server problem",Toast.LENGTH_SHORT).show();

                        }
                        else{

                            Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    }
                });
            }
        });



    }
}
