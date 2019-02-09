package com.example.mangurkaur.employeeattendancepro.Activties;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.mangurkaur.employeeattendancepro.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

public class NotificationService extends Service{

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseStorage firebaseStorage;
    private StorageReference islandRef;
    private String timecardId;
    private String EmpName;
    private TimeCardClass object;
    private EmpClass empClass;
    private  String picName;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        islandRef = firebaseStorage.getReference();
        myRef.child("Notification").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    object = dataSnapshot.getValue(TimeCardClass.class);
                    //getting employeee name using timeCardId
                    myRef.child("Employee").orderByChild("empId").equalTo(object.getEmpId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                empClass = snapshot.getValue(EmpClass.class);
                                EmpName = empClass.getFirstName() + " " + empClass.getLastName();


                                if (object.getEndTime() == null) {

                                    picName = object.getEmpId() + "signIn" + object.getDate();
                                    getPhoto(picName, object.getTimeCardId(), "Sign In");
                                    myRef.child("Notification").child(timecardId).removeValue();

                                } else {

                                    picName = object.getEmpId() + "signOut" + object.getDate();
                                    getPhoto(picName, object.getTimeCardId(), "Sign Out");
                                    myRef.child("Notification").child(timecardId).removeValue();
                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    //getting employeee name using timeCardId

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

        private void getPhoto(String picName, String timeId, final String status){

            timecardId = timeId;
            final long ONE_MEGABYTE = 1024 * 1024;
            islandRef.child(picName).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);



                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.admin)
                            .setContentTitle(EmpName + " " + status)
                            .setContentText("Tap to check")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("Tap to check"))
                            .setLargeIcon(bmp)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    Intent notifyIntent = new Intent(getApplicationContext(),ShowEmployee.class);

                    notifyIntent.putExtra("timeCardId",object.getTimeCardId());
                    notifyIntent.putExtra("status",status);
                    notifyIntent.putExtra("empName",EmpName);
                    notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                            getApplicationContext(), 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
                    );

                    mBuilder.setContentIntent(notifyPendingIntent);

                    Random random = new Random();
                    int id = Integer.valueOf(String.format("%04d", random.nextInt(9999)));
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                    notificationManager.notify(id, mBuilder.build());


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
     }
}

