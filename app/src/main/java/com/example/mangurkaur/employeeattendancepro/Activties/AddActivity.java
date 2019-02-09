package com.example.mangurkaur.employeeattendancepro.Activties;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;


import com.example.mangurkaur.employeeattendancepro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class AddActivity extends AppCompatActivity {

    private  EditText FirstName;
    private  EditText LastName;
    private  EditText Phone;
    private  EditText Email;
    private  RadioButton GenderM;
    private  RadioButton  GenderF;
    private  Button add;
    private  EmpClass empClass;
    private  EmailClass emailClass;
    private  FirebaseDatabase database;
    private  String key;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setTitle("Add Employee");

        FirstName=findViewById(R.id.frstname);
        LastName=findViewById(R.id.lastname);
        Phone=findViewById(R.id.cntct);
        Email=findViewById(R.id.emaill);
        GenderM=findViewById(R.id.radioM);
        GenderF=findViewById(R.id.radioF);
        add=findViewById(R.id.addEmp);
        GenderM.setChecked(true);

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(FirstName.getText().toString().isEmpty()||LastName.getText().toString().isEmpty()||Phone.getText().toString().isEmpty()||Email.getText().toString().isEmpty()){

                    Toast.makeText(AddActivity.this,"Please end all fields",Toast.LENGTH_SHORT).show();

                }
                else{

                    addEmployee();
                }

            }
        });



    }

    private void addEmployee() {

        setValueInEmpObject();
        myRef.child("Employee").child(key).setValue(empClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(!task.isSuccessful()){
                    Toast.makeText(AddActivity.this,"Server Problem",Toast.LENGTH_SHORT).show();
                    finish();

                }
                else{
                    final AlertDialog.Builder  builder = new AlertDialog.Builder(AddActivity.this);
                    builder.setTitle("Message");
                    builder.setMessage("Employee Added").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {


                            myRef.child("Email").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){

                                        emailClass=snapshot.getValue(EmailClass.class);
                                        SendEmail sendEmail=new SendEmail();
                                        sendEmail.execute(empClass.getEmail(),empClass.getEmpId(),empClass.getFirstName(),emailClass.getEmail(),emailClass.getPass());
                                        builder.setCancelable(false);
                                        finish();

                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }});
                    AlertDialog show = builder.show();

                }

            }
        });


    }

    private void setValueInEmpObject() {

        empClass=new EmpClass();
        empClass.setFirstName(FirstName.getText().toString());
        empClass.setLastName(LastName.getText().toString());
        empClass.setEmail(Email.getText().toString());
        empClass.setPhone(Phone.getText().toString());
        if(GenderM.isChecked()){

            empClass.setGender("Male");
        }
        else if(GenderF.isChecked()){

            empClass.setGender("Female");
        }


        empClass.setEmpId(String.valueOf(generateId()));
        empClass.setStatus("InActive");
        key=myRef.push().getKey();
        empClass.setDataId(key);


    }

    private int generateId() {

        Random rand=new Random();

        int num=Integer.valueOf(String.format("%04d",rand.nextInt(9999)));



        return num;
    }
}
