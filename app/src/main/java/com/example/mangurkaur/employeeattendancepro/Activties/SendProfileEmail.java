package com.example.mangurkaur.employeeattendancepro.Activties;

import android.os.AsyncTask;
import android.util.Log;

public class SendProfileEmail extends AsyncTask<String, Integer, Long> {
    protected Long doInBackground(String... body) {
        try{
            GMailSender sender = new GMailSender(body[5], body[6]);
            sender.sendMail("Information updated","Hi "+body[2]+"\n"+"You are profile information updated by your employer.This is your new updated information "+
                            "\n"+"Email:"+body[1]+"\n"+"Name:"+body[2]+" "+body[3]+"\n"+"Phone number:"+body[4],
                    "noreplydelight@gmail.com",body[0]);
        }catch (Exception e){
            Log.e("Mail Error", e.getMessage());
        }


        return null;
    }

    protected void onProgressUpdate(Integer... progress) {
    }

}
