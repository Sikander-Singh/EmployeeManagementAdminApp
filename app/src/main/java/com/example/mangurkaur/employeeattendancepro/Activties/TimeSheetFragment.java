package com.example.mangurkaur.employeeattendancepro.Activties;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import noman.weekcalendar.WeekCalendar;
import noman.weekcalendar.listener.OnDateClickListener;

public class TimeSheetFragment extends Fragment {

    private WeekCalendar weekCal;
    private MaterialCalendarView monthCal;
    private String dateStr;
    private TextView hours;
    private ImageView editStartTime;
    private ImageView editEndTime;
    private String empId;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private String startDateTime;
    private String endDateTime;
    private float totalTime;
    private  TextView startTime;
    private  TextView endTime;
    private TimeCardClass timeCardClass;
    private TimeCardClass currentTimeCard;
    private  EmpClass empClass;
    private MenuItem updateCardTime;
    private Date date;
    private SimpleDateFormat dt;
    private  String checkDate;
    private RelativeLayout layoutEmpImage;
    private RelativeLayout layoutStartTime;
    private RelativeLayout layoutEndTime;
    private RelativeLayout layoutShowTime;
    private ImageView imageSignIn;
    private ImageView imageSignOut;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        ((InformationActivity)getActivity()).getSupportActionBar().setTitle("Time Sheet");
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();
        Intent intent=getActivity().getIntent();
        empId=intent.getStringExtra("EmpId");
        date = new Date();
        dt = new SimpleDateFormat("dd-MM-yyyy");
        checkDate = dt.format(date);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_time_sheet, container, false);

        weekCal = view.findViewById(R.id.weekCalendar);
        monthCal=view.findViewById(R.id.monthCalendar);
        monthCal.setVisibility(View.GONE);
        hours=view.findViewById(R.id.showHours);
        editStartTime=view.findViewById(R.id.editStartTime);
        editEndTime=view.findViewById(R.id.editEndTime);
        startTime=view.findViewById(R.id.startTime);
        endTime=view.findViewById(R.id.endTime);
        layoutEmpImage=view.findViewById(R.id.layoutEmpImage);
        layoutStartTime=view.findViewById(R.id.layoutStartTime);
        layoutEndTime=view.findViewById(R.id.layoutEndTime);
        layoutShowTime=view.findViewById(R.id.layoutShowTime);
        imageSignIn=view.findViewById(R.id.imageSignIn);
        imageSignOut=view.findViewById(R.id.imageSignOut);
        editStartTime.setVisibility(View.GONE);
        editEndTime.setVisibility(View.GONE);
        layoutEmpImage.setVisibility(View.GONE);
        loadHours();
        editStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if (currentTimeCard.getEndTime()==null&&currentTimeCard.getDate().equals(checkDate)){

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Message");

                    builder.setMessage("Employee still working").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            builder.setCancelable(false);

                        }
                    });

                    builder.show();


                }
               else {
                   SimpleDateFormat fr = new SimpleDateFormat("hh:mm:ss");
                   Date dateStr = null;
                   try {
                       dateStr = fr.parse(startTime.getText().toString());
                   } catch (ParseException e) {
                       e.printStackTrace();
                   }
                   final int hour = dateStr.getHours();
                   final int minute = dateStr.getMinutes();
                   final int second = dateStr.getSeconds();
                   final MyTimePickerDialog timePickerDialog = new MyTimePickerDialog(getContext(), new MyTimePickerDialog.OnTimeSetListener() {
                       @Override
                       public void onTimeSet(com.ikovac.timepickerwithseconds.TimePicker view, int hourOfDay, int minute, int seconds) {

                           startTime.setText(hourOfDay + ":" + minute + ":" + seconds);
                       }
                   }, hour, minute, second, false);
                   timePickerDialog.show();
               }

            }
        });

        editEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (currentTimeCard.getEndTime()==null&&currentTimeCard.getDate().equals(checkDate)) {



                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Message");

                    builder.setMessage("Employee still working").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            builder.setCancelable(false);

                        }
                    });

                    builder.show();


                }
             else {

                    SimpleDateFormat fr = new SimpleDateFormat("hh:mm:ss");
                    Date dateStr = null;
                    try {

                        dateStr=fr.parse(startTime.getText().toString());


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    final int hour = dateStr.getHours();
                    final int minute = dateStr.getMinutes();
                    final int second = dateStr.getSeconds();
                    final MyTimePickerDialog timePickerDialog = new MyTimePickerDialog(getContext(), new MyTimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(com.ikovac.timepickerwithseconds.TimePicker view, int hourOfDay, int minute, int seconds) {

                            endTime.setText(hourOfDay + ":" + minute + ":" + seconds);
                            updateCardTime.setVisible(true);
                        }
                    }, hour, minute, second, false);
                    timePickerDialog.show();


                }
            }


        });


        monthCal.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                Date calDate=date.getDate();
                SimpleDateFormat simpleDayFormat=new SimpleDateFormat("EEEE");
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
                editStartTime.setVisibility(View.GONE);
                editEndTime.setVisibility(View.GONE);
                updateCardTime.setVisible(false);
                startTime.setText("00:00:00");
                endTime.setText("00:00:00");
                hours.setText("0 Hours");
                setDataInFragment(simpleDayFormat.format(calDate), simpleDateFormat.format(calDate));
                Drawable drawable=getResources().getDrawable(R.drawable.emp);
                imageSignIn.setImageDrawable(drawable);
                imageSignOut.setImageDrawable(drawable);
                imageSignIn.setRotation(360);
                imageSignOut.setRotation(360);
                if(currentTimeCard!=null) {
                    getPhoto(currentTimeCard.getEmpId(), "signIn", simpleDateFormat.format(calDate));
                    getPhoto(currentTimeCard.getEmpId(), "signOut", simpleDateFormat.format(calDate));
                }

            }
        });

        weekCal.setOnDateClickListener(new OnDateClickListener() {

            @Override
            public void onDateClick(DateTime dateTime) {

                DateTime.Property day = dateTime.dayOfWeek();
                String dayStr =day.getAsText();
                editStartTime.setVisibility(View.GONE);
                editEndTime.setVisibility(View.GONE);
                updateCardTime.setVisible(false);
                startTime.setText("00:00:00");
                endTime.setText("00:00:00");
                hours.setText("0 Hours");
                setDataInFragment(dayStr,dateTime.toString("dd-MM-yyyy"));
            }
        });

        return view;

    }

    private void loadHours() {

        CalendarDay day=new CalendarDay();
        Date date=day.getDate();
        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dayforamt=new SimpleDateFormat("EEEE");
        setDataInFragment(dayforamt.format(date),dt.format(date));

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.calendar, menu);
        updateCardTime=menu.findItem(R.id.updateTimeCard);
        updateCardTime.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_weekly) {

            layoutEmpImage.setVisibility(View.GONE);
            layoutStartTime.setVisibility(View.VISIBLE);
            layoutEndTime.setVisibility(View.VISIBLE);
            layoutShowTime.setVisibility(View.VISIBLE);
            weekCal.setVisibility(View.VISIBLE);
            monthCal.setVisibility(View.GONE);

        }
        else if (id == R.id.action_monthly) {

            layoutEmpImage.setVisibility(View.GONE);
            layoutStartTime.setVisibility(View.VISIBLE);
            layoutEndTime.setVisibility(View.VISIBLE);
            layoutShowTime.setVisibility(View.VISIBLE);
            weekCal.setVisibility(View.GONE);
            monthCal.setVisibility(View.VISIBLE);

        }
        else if (id == R.id.updateTimeCard) {

            UpdateCardTime();

        }
        else if (id == R.id.action_image) {

            weekCal.setVisibility(View.GONE);
            layoutStartTime.setVisibility(View.GONE);
            layoutEndTime.setVisibility(View.GONE);
            layoutShowTime.setVisibility(View.GONE);
            monthCal.setVisibility(View.VISIBLE);
            layoutEmpImage.setVisibility(View.VISIBLE);
            monthCal.setSelectedDate(date);
            monthCal.setSelectionColor(Color.parseColor("#46AB46"));
            if(currentTimeCard!=null) {
                getPhoto(currentTimeCard.getEmpId(), "signIn", currentTimeCard.getDate());
                getPhoto(currentTimeCard.getEmpId(), "signOut", currentTimeCard.getDate());
            }

        }

        return super.onOptionsItemSelected(item);
    }

   public void setDataInFragment(String dayStr,String dateString){

        switch (dayStr) {

            case "Monday":

                dateStr = dateString;
                getListOfTimeCards();
                break;
            case "Tuesday": 

                dateStr = dateString;

                getListOfTimeCards();

                break;
            case "Wednesday":

                dateStr = dateString;

                getListOfTimeCards();

                break;
            case "Thursday":
                dateStr = dateString;
                getListOfTimeCards();
                break;
            case "Friday":

                dateStr = dateString;
                getListOfTimeCards();

                break;
            case "Saturday":

                dateStr = dateString;
                getListOfTimeCards();

                break;
            case "Sunday":

                dateStr = dateString;
                getListOfTimeCards();

                break;
            default:
                break;
        }

    }


    public  void getListOfTimeCards( ){


        myRef.child("TimeCard").orderByChild("empId").equalTo(empId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot timedataSnapshot:dataSnapshot.getChildren()){

                   timeCardClass=timedataSnapshot.getValue(TimeCardClass.class);

                    if(timeCardClass.getDate().equals(dateStr)){

                        startTime.setText(timeCardClass.getStartTime());

                        endTime.setText(timeCardClass.getEndTime());

                        CalTotalTime();

                        currentTimeCard=timeCardClass;

                        editStartTime.setVisibility(View.VISIBLE);
                        editEndTime.setVisibility(View.VISIBLE);
                        updateCardTime.setVisible(true);
                        if(currentTimeCard.getEndTime()==null){

                            updateCardTime.setVisible(false);

                        }
                        else{

                            //nothing
                        }


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void CalTotalTime(){

        startDateTime=dateStr+" "+startTime.getText();
        endDateTime=dateStr+" "+endTime.getText();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        Date d1 = null ;
        Date d2 = null;
        try {
            d1 = format.parse(startDateTime);
            d2 = format.parse(endDateTime);
            //in milliseconds
            long diff = d2.getTime() - d1.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            float sec=diffDays*24*60*60+diffHours*60*60+diffMinutes*60+diffSeconds;
            totalTime=sec/60;
            DecimalFormat df = new DecimalFormat("###");

            if(totalTime<1 ){

                hours.setText("0 Hours");

            }
            else if(totalTime==1){

                hours.setText(String.valueOf(df.format(totalTime))+" "+"minutes");

            }
            else if(totalTime>1 && totalTime<60 ){

                hours.setText(String.valueOf(df.format(totalTime))+" "+"minutes");
            }
            else if(totalTime==60 || totalTime>60){

                int totalHours=Integer.parseInt(df.format(totalTime));

                int rem=totalHours % 60;

                hours.setText(String.valueOf(df.format(totalHours/60))+" "+"hours"+" "+rem+" "+"minutes");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void UpdateCardTime(){

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        Date start=null;
        Date end=null;
        try {
            start=format.parse(startTime.getText().toString());
            end=format.parse(endTime.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(start.compareTo(end)>0){

            final AlertDialog.Builder   builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Warning");
            builder.setMessage("End date should be greater than start date").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }});
            builder.show();

        }
        else if(start.compareTo(end)<0 || start.compareTo(end)==0){


            final AlertDialog.Builder  builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Update Hours");

            builder.setMessage("Are you sure want to update hours");

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    myRef.child("Employee").orderByChild("empId").equalTo(currentTimeCard.getEmpId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for(DataSnapshot empsnapshot:dataSnapshot.getChildren()){

                                empClass=empsnapshot.getValue(EmpClass.class);

                                if(empClass.getStatus().equals("Active")&&empClass.getStatusDate().equals(checkDate)){

                                    currentTimeCard.setStartTime(startTime.getText().toString());
                                    currentTimeCard.setEndTime(endTime.getText().toString());
                                    myRef.child("TimeCard").child(currentTimeCard.getTimeCardId()).setValue(currentTimeCard);

                                    Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                                    UpdateHourEmail sendEmail = new UpdateHourEmail();
                                    sendEmail.execute(empClass.getEmail(), empClass.getFirstName(), currentTimeCard.getDate(), currentTimeCard.getStartTime(), currentTimeCard.getEndTime());
                                }
                                else if(empClass.getStatus().equals("InActive")){


                                    currentTimeCard.setStartTime(startTime.getText().toString());
                                    currentTimeCard.setEndTime(endTime.getText().toString());
                                    myRef.child("TimeCard").child(currentTimeCard.getTimeCardId()).setValue(currentTimeCard);

                                    Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                                    UpdateHourEmail sendEmail = new UpdateHourEmail();
                                    sendEmail.execute(empClass.getEmail(), empClass.getFirstName(), currentTimeCard.getDate(),  currentTimeCard.getStartTime(), currentTimeCard.getEndTime());
                                }
                                else{

                                    Toast.makeText(getContext(), "Sorry!! Employee has to sign in first", Toast.LENGTH_SHORT).show();

                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    builder.setCancelable(false);

                }});
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    builder.setCancelable(false);
                } });

            builder.show();

        }

    }

    private void getPhoto(String empId,final String status,String date){


        String imageName=" ";

        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageReference=storage.getReference();

        if(status.equals("signIn")){

            imageName=empId+"signIn"+date;

        }
        else if(status.equals("signOut")){

            imageName=empId+"signOut"+date;
        }
        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.child(imageName).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                if(status.equals("signIn")){

                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageSignIn.setRotation(270);
                    imageSignIn.setImageBitmap(bmp);

                }
                else if(status.equals("signOut")){

                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageSignOut.setRotation(270);
                    imageSignOut.setImageBitmap(bmp);
                }

                //progressBar.setVisibility(View.GONE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

}
