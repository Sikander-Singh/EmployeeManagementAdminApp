package com.example.mangurkaur.employeeattendancepro.Activties;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangurkaur.employeeattendancepro.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

public class ShowEmployee extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseStorage firebaseStorage;
    private StorageReference islandRef;
    private ProgressBar progressBar;
    private String status;
    private TextView empDate;
    private  String name;
    private TextView empName;
    private TextView empTime;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_employee);

        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        islandRef = firebaseStorage.getReference();
        empName=findViewById(R.id.name);
        empTime=findViewById(R.id.time);
        empDate = findViewById(R.id.employeedate);
        imageView=findViewById(R.id.showImage);
        progressBar=findViewById(R.id.progressBar);
        Intent intent=getIntent();
        String id=intent.getStringExtra("timeCardId");
        status=intent.getStringExtra("status");
        name=intent.getStringExtra("empName");


        myRef.child("TimeCard").orderByChild("timeCardId").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){

                    TimeCardClass timeCardClass=snapshot.getValue(TimeCardClass.class);
                    if(status.equals("Sign In")){

                        String picName=timeCardClass.getEmpId()+"signIn"+timeCardClass.getDate();
                        getPhoto(picName,name,timeCardClass.getStartTime(),timeCardClass.getDate());

                    }
                    else if(status.equals("Sign Out")) {

                        String picName = timeCardClass.getEmpId() + "signOut" + timeCardClass.getDate();
                        getPhoto(picName, name,timeCardClass.getEndTime(),timeCardClass.getDate());

                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getPhoto(String picName, final String name, final String time, final String date){
        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.child(picName).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setRotation(270);
                imageView.setImageBitmap(bmp);
                empName.setText(name);
                empTime.setText(time);
                empDate.setText(date);
                progressBar.setVisibility(View.GONE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

}
