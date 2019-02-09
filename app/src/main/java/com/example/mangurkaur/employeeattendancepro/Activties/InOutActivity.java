package com.example.mangurkaur.employeeattendancepro.Activties;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mangurkaur.employeeattendancepro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;



public class InOutActivity extends AppCompatActivity {

    private Button ok;
    private ImageView connfirm;
    private EditText empId;
    private ImageView signInImage;
    private ImageView signOutImage;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth auth;
    private Date date;
    private SimpleDateFormat dt;
    private  SimpleDateFormat dt1;
    private  String dateStr;
    private  String TimeStr;
    private  String str;
    private boolean status=false;
    EmpClass empClass;
    TimeCardClass timeCardClass=new TimeCardClass();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_out);
        ok = (Button)findViewById(R.id.done);
        connfirm=(ImageView) findViewById(R.id.confirm);
        empId=(EditText)findViewById(R.id.empId);
        connfirm.setVisibility(View.GONE);
        signInImage=findViewById(R.id.signin);
        signOutImage=findViewById(R.id.signout);
        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();

        Intent intent=getIntent();
        str = intent.getStringExtra("key");

        connfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(str.equals("signIn")){


            signInImage.setVisibility(View.VISIBLE);
            signOutImage.setVisibility(View.GONE);


        }
        else if(str.equals("signOut")){

            signInImage.setVisibility(View.GONE);
            signOutImage.setVisibility(View.VISIBLE);

        }
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                date = new Date();
                dt = new SimpleDateFormat("dd-MM-yyyy");
                dt1 = new SimpleDateFormat("HH:mm:ss");
                dateStr = dt.format(date);
                TimeStr = dt1.format(date);

                if(empId.getText().toString().isEmpty()){


                }
                else{


                    // User Cannot Sign in and out multiple times

                    CheckSignInOutConfirmation(); //Check Sign in and out confirmation
                    //InvalidId();

                }



            }
        });


    }


    private void InvalidId() {

        if(status==true){

            final AlertDialog.Builder  builder = new AlertDialog.Builder(InOutActivity.this);
            builder.setTitle("Message");

            builder.setMessage("Invalid Id").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    builder.setCancelable(false);
                }});
            AlertDialog show = builder.show();

        }




    }



    private void getEmployeeData() {

        myRef.child("Employee").orderByChild("empId").equalTo(empId.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot empdatasnapshot : dataSnapshot.getChildren()) {

                    empClass = empdatasnapshot.getValue(EmpClass.class);
                    //Sign In


                    if (str.equals("signIn")) {
                        if (empId.getText().toString().equals(empClass.getEmpId() )) {


                            if(empClass.getStatusDate()==null){

                                SendSignEmail sendSignEmail=new SendSignEmail();
                                sendSignEmail.execute(empClass.getEmail(),empClass.getFirstName(),dateStr,TimeStr);
                                SignIn();
                            }
                            else {

                                if (empClass.getStatusDate().equals(dateStr)) {
                                    Toast.makeText(InOutActivity.this,"You can not sign in second time in a day",Toast.LENGTH_SHORT).show();

                                }
                                else {

                                    SendSignEmail sendSignEmail=new SendSignEmail();
                                    sendSignEmail.execute(empClass.getEmail(),dateStr,TimeStr);
                                    SignIn();

                                }
                            }
                        }

                    }
                    // Sign In

                    //Sign Out
                    if (str.equals("signOut")) {

                        if (empId.getText().toString().equals(empClass.getEmpId())&&empClass.getStatus().equals("Active")&&empClass.getStatusDate().equals(dateStr)) {


                            getEmployeeTimeCard();


                            SignOut();
                        }
                    }

                    //Sign Out

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(InOutActivity.this,"Server Problem",Toast.LENGTH_SHORT).show();
                finish();

            }
        });





    }



    private void getEmployeeTimeCard() {

        myRef.child("TimeCard").orderByChild("timeCardId").equalTo(empClass.getTimeCardId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot timedatasnapshot : dataSnapshot.getChildren()) {

                    timeCardClass=timedatasnapshot.getValue(TimeCardClass.class);
                    //Update employee time card after signout

                    SimpleDateFormat dt1 = new SimpleDateFormat("HH:mm:ss");
                    Date date = new Date();
                    String TimeStr = dt1.format(date);
                    timeCardClass.setEndTime(TimeStr);
                    myRef.child("TimeCard").child(empClass.getTimeCardId()).setValue(timeCardClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(InOutActivity.this,"Server Problem",Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }
                    });

                    //Update employee time card after signout

                    //Update employee status Active to InActive after signout
                    empClass.setStatusDate(timeCardClass.getDate());
                    empClass.setStatus("InActive");


                    myRef.child("Employee").child(empClass.getDataId()).setValue(empClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                connfirm.setVisibility(View.VISIBLE);
                                empId.setVisibility(View.GONE);
                                ok.setVisibility(View.GONE);
                                SendSignOutEmail sendSignOutEmail=new SendSignOutEmail();
                                sendSignOutEmail.execute(empClass.getEmail(),empClass.getFirstName(),timeCardClass.getDate(),timeCardClass.getStartTime(),timeCardClass.getEndTime());


                            }
                            else{

                                Toast.makeText(InOutActivity.this,"Server Problem",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });

                    myRef.child("Employee").child(empClass.getDataId()).child("timeCardId").removeValue();
                    //Update employee status Active to InActive after signout



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    private void CheckSignInOutConfirmation() {


        getEmployeeData();



    }
    private void SignIn() {

       //Write sign in employee time card after signin

        String key = myRef.push().getKey();
        timeCardClass.setTimeCardId(key);
        timeCardClass.setEmpId(empClass.getEmpId());
        timeCardClass.setDate(dateStr);
        timeCardClass.setStartTime(TimeStr);
        myRef.child("TimeCard").child(key).setValue(timeCardClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){

                    Toast.makeText(InOutActivity.this,"Server Problem",Toast.LENGTH_SHORT).show();

                }

            }
        });

        //Write sign in employee time card after signin


        //Update Employee status InActive to Active after signin
        empClass.setTimeCardId(key);
        empClass.setStatusDate(timeCardClass.getDate());
        empClass.setStatus("Active");
        myRef.child("Employee").child(empClass.getDataId()).setValue(empClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    connfirm.setVisibility(View.VISIBLE);
                    empId.setVisibility(View.GONE);
                    ok.setVisibility(View.GONE);


                }
                else{

                    Toast.makeText(InOutActivity.this,"Server Problem",Toast.LENGTH_SHORT).show();

                }
            }
        });

        //Update Employee status InActive to Active after signin

    }
    private void SignOut() {


        getEmployeeTimeCard();


    }

    private void CheckForMulitpleSignInOut(String id, String dateStr) {




    }


}
