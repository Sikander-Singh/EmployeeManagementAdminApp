package com.example.mangurkaur.employeeattendancepro.Activties;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SendEmail extends AsyncTask<String, Integer, Long> {
    protected Long doInBackground(final String... body) {
        try{
            GMailSender sender = new GMailSender(body[3],body[4]);
            sender.sendMail("You are added","Hi "+body[2]+"\n"+"\n"+"You are recently added as employee.This is your employee id "+body[1],
                    body[3],
                    body[0]);
        }catch (Exception e){
            Log.e("Mail Error", e.getMessage());
        }
        return null;
    }

    protected void onProgressUpdate(Integer... progress) {
    }

}

