package com.example.mangurkaur.employeeattendancepro.Activties;

import android.os.AsyncTask;
import android.util.Log;

public class UpdateHourEmail extends AsyncTask<String, Integer, Long> {
    protected Long doInBackground(String... body) {
        try{
            GMailSender sender = new GMailSender("noreplydelight@gmail.com", "rootadmin123");
            sender.sendMail("Your hours are updated","Hi "+body[1]+"\n"+"\n"+"You hours are recenlty updated for this date "+body[2]+"\n"+"Start Time: "+body[3]+"\n"+"End Time: "+body[4],
                    "noreplydelight@gmail.com",
                    body[0]);
        }catch (Exception e){
            Log.e("Mail Error", e.getMessage());
        }

        return null;
    }

    protected void onProgressUpdate(Integer... progress) {
        }
}
