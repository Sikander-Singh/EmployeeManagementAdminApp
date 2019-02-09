package com.example.mangurkaur.employeeattendancepro.Activties;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.progresviews.ProgressWheel;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.example.mangurkaur.employeeattendancepro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OverviewFragment extends Fragment implements DatePickerDialog.OnDateSetListener {


    private TextView lastMonth;
    private TextView thisMonth;
    private TextView lastWeek;
    private TextView thisWeek;
    private ProgressWheel wheel;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private SimpleDateFormat simpleDateFormat;
    private Date startDateString;
    private Date endDateString;
    private Calendar cal;
    private String empId;
    private  Float hours;


    public OverviewFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {


        ((InformationActivity)getActivity()).getSupportActionBar().setTitle("Overview");
        setHasOptionsMenu(true);
        firebaseDatabase= FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();
        cal=Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
        Intent intent=getActivity().getIntent();
        empId=intent.getStringExtra("EmpId");
        hours=Float.parseFloat("0");
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

         View view = inflater.inflate(R.layout.fragment_overview, container, false);

         lastMonth=view.findViewById(R.id.lastMonth);
         thisMonth=view.findViewById(R.id.thisMonth);
         lastWeek=view.findViewById(R.id.lastWeek);
         thisWeek=view.findViewById(R.id.thisWeek);
         wheel=view.findViewById(R.id.wheelprogress);
         wheel.setStepCountText("0 Hours");
         wheel.setPercentage(400);
        //this week hours
        hours=Float.parseFloat("0");
        lastMonth.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape));
        thisMonth.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape));
        lastWeek.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape));
        thisWeek.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape_1));
        Calendar calendar = Calendar.getInstance();
        //calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        String[] days = new String[7];
        for (int i = 0; i < 7; i++)
        {
            days[i] = simpleDateFormat.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        wheel.setStepCountText("0 Hours");
        CalculateHours(days[0],days[6]);
       //this week hours





         lastMonth.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 hours=Float.parseFloat("0");
                 lastMonth.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape_1));
                 thisMonth.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape));
                 lastWeek.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape));
                 thisWeek.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape));

                 cal=Calendar.getInstance();
                 cal.add(Calendar.MONTH,-1);
                 cal.set(Calendar.DAY_OF_MONTH,1);
                 startDateString=cal.getTime();
                 cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                 endDateString=cal.getTime();
                 wheel.setStepCountText("0 Hours");
                 CalculateHours(simpleDateFormat.format(startDateString),simpleDateFormat.format(endDateString));

             }
         });


        thisMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hours=Float.parseFloat("0");
                lastMonth.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape));
                thisMonth.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape_1));
                lastWeek.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape));
                thisWeek.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape));
                cal=Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH,1);
                startDateString=cal.getTime();
                cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                endDateString=cal.getTime();
                wheel.setStepCountText("0 Hours");
                CalculateHours(simpleDateFormat.format(startDateString),simpleDateFormat.format(endDateString));

            }
        });

        lastWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hours=Float.parseFloat("0");
                lastMonth.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape));
                thisMonth.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape));
                lastWeek.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape_1));
                thisWeek.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape));
                Date date=new Date();
                cal = Calendar.getInstance();
                cal.setTime(date);
                int i = cal.get(Calendar.DAY_OF_WEEK) - cal.getFirstDayOfWeek();
                cal.add(Calendar.DATE, -i - 7);
                startDateString = cal.getTime();
                cal.add(Calendar.DATE, 6);
                endDateString = cal.getTime();
                wheel.setStepCountText("0 Hours");
                CalculateHours(simpleDateFormat.format(startDateString),simpleDateFormat.format(endDateString));
            }
        });

        thisWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hours=Float.parseFloat("0");
                lastMonth.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape));
                thisMonth.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape));
                lastWeek.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape));
                thisWeek.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape_1));
                Calendar calendar = Calendar.getInstance();
                //calendar.setFirstDayOfWeek(Calendar.MONDAY);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

                String[] days = new String[7];
                for (int i = 0; i < 7; i++)
                {
                    days[i] = simpleDateFormat.format(calendar.getTime());
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                wheel.setStepCountText("0 Hours");
                CalculateHours(days[0],days[6]);
            }
        });



        return view;

    }

    private void CalculateHours(final String startDate, final String endDate) {


        myRef.child("TimeCard").orderByChild("empId").equalTo(empId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Date start=new Date();
                Date end=new Date();
                try {
                    start=simpleDateFormat.parse(startDate);
                    end=simpleDateFormat.parse(endDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                for(DataSnapshot hoursdatasnapcshot:dataSnapshot.getChildren()){

                    TimeCardClass obj = hoursdatasnapcshot.getValue(TimeCardClass.class);
                    Date objDate=new Date();
                    try {
                        simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
                        objDate=simpleDateFormat.parse(obj.getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if((objDate.compareTo(start)>0 && objDate.compareTo(end)<0) || objDate.compareTo(start)==0 || objDate.compareTo(end)==0){

                        CalculateTotalTime(obj.getStartTime(),obj.getEndTime(),obj.getDate());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void CalculateTotalTime(String startTimeString,String endTimeString,String dateString) {

        String startDateTime=dateString+" "+startTimeString;
        String endDateTime=dateString+" "+endTimeString;
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
            hours=hours+sec/60;
            DecimalFormat df = new DecimalFormat("###");
            if(hours<1 ){
                wheel.setStepCountText("0 Hours");

            }
            else if(hours==1){

                wheel.setStepCountText(String.valueOf(df.format(hours))+" "+"minutes");

            }
            else if(hours>1 && hours<60 ){

                wheel.setStepCountText(String.valueOf(df.format(hours))+" "+"minutes");
            }
            else if(hours==60 || hours>60){

                int totalHours=Integer.parseInt(df.format(hours));

                int rem=totalHours % 60;

                wheel.setStepCountText(String.valueOf(df.format(totalHours/60))+" "+"hours"+" "+rem+" "+"minutes");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.custom, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.navigation_custom) {

            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(OverviewFragment.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );

            dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {

        hours=Float.parseFloat("0");
        String startDateStr=dayOfMonth+"-"+(monthOfYear+1)+"-"+year;
        String endDateStr=dayOfMonthEnd+"-"+(monthOfYearEnd+1)+"-"+yearEnd;
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
        Date startDateRange=null;
        Date endDateRange=null;
        try {
            startDateRange=dateFormat.parse(startDateStr);
            endDateRange=dateFormat.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        wheel.setStepCountText("0 Hours");
        lastMonth.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape));
        thisMonth.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape));
        lastWeek.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape));
        thisWeek.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.circle_shape));
        CalculateHours(simpleDateFormat.format(startDateRange),simpleDateFormat.format(endDateRange));

    }
}
