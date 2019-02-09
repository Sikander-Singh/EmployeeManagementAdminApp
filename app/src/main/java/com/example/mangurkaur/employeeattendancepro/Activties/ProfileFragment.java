package com.example.mangurkaur.employeeattendancepro.Activties;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.example.mangurkaur.employeeattendancepro.R;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ProfileFragment extends Fragment {
    private EditText FName;
    private EditText LName;
    private EditText Ph;
    private EditText Email;
    private TextView EmployeeId;
    private RadioButton radioM;
    private RadioButton radioF;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private EmailClass emailClass;
    EmpClass empClass;
    String empId;
    @Override
    public void onCreate(Bundle savedInstanceState) {


        ((InformationActivity)getActivity()).getSupportActionBar().setTitle("Employee Profile");
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();
        FName = view.findViewById(R.id.fname);
        LName = view.findViewById(R.id.lname);
        Ph =    view.findViewById(R.id.contact);
        Email = view.findViewById(R.id.email);
        EmployeeId = view.findViewById(R.id.employeeid);
        radioM=view.findViewById(R.id.male);
        radioF=view.findViewById(R.id.female);
        Intent intent=getActivity().getIntent();
        radioM.setEnabled(true);
        empId=intent.getStringExtra("EmpId");

        myRef.child("Employee").orderByChild("empId").equalTo(empId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot empdataSnapshot:dataSnapshot.getChildren()){


                    empClass=empdataSnapshot.getValue(EmpClass.class);
                    FName.setText(empClass.getFirstName());
                    LName.setText(empClass.getLastName());
                    if(empClass.getGender().equals("Male")){

                        radioM.setChecked(true);
                    }
                    else if(empClass.getGender().equals("Female")){

                        radioF.setChecked(true);
                    }
                    Ph.setText(empClass.getPhone());
                    Email.setText(empClass.getEmail());
                    EmployeeId.setText(empClass.getEmpId());

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       /* Donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(FName.getText().toString().isEmpty()||LName.getText().toString().isEmpty()||Ph.getText().toString().isEmpty()||Email.getText().toString().isEmpty()){


                    Toast.makeText(getContext(),"Please enter all fields",Toast.LENGTH_SHORT).show();


                }
                else {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Update");

                    builder.setMessage("Are you sure want to update employee").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            myRef.child("Employee").child(empClass.getDataId()).child("firstName").setValue(FName.getText().toString());
                            myRef.child("Employee").child(empClass.getDataId()).child("lastName").setValue(LName.getText().toString());
                            myRef.child("Employee").child(empClass.getDataId()).child("email").setValue(Email.getText().toString());
                            myRef.child("Employee").child(empClass.getDataId()).child("phone").setValue(Ph.getText().toString());
                            if (radioM.isChecked()) {

                                myRef.child("Employee").child(empClass.getDataId()).child("gender").setValue("Male");

                            } else if (radioF.isChecked()) {

                                myRef.child("Employee").child(empClass.getDataId()).child("gender").setValue("Female");
                            }

                            Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                            SendProfileEmail sendEmail = new SendProfileEmail();
                            sendEmail.execute(empClass.getEmail(), Email.getText().toString(), FName.getText().toString(), LName.getText().toString(), Ph.getText().toString());
                            builder.setCancelable(false);

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            builder.setCancelable(false);
                        }
                    });

                    builder.show();
                }
            }
        });*/


      /*  delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final AlertDialog.Builder  builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete");

                builder.setMessage("Are you sure want to delete employee").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        myRef.child("Employee").child(empClass.getDataId()).removeValue();
                        getActivity().finish();
                        Toast.makeText(getContext(),"Deleted",Toast.LENGTH_SHORT).show();
                        builder.setCancelable(false);

                    }});
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        builder.setCancelable(false);
                    } });

                builder.show();

            }
        });*/



        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.delete, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.donebtn) {

            if(FName.getText().toString().isEmpty()||LName.getText().toString().isEmpty()||Ph.getText().toString().isEmpty()||Email.getText().toString().isEmpty()){

                Toast.makeText(getContext(),"Please enter all fields",Toast.LENGTH_SHORT).show();

            }
            else {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Update");

                builder.setMessage("Are you sure want to update employee").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        myRef.child("Employee").child(empClass.getDataId()).child("firstName").setValue(FName.getText().toString());
                        myRef.child("Employee").child(empClass.getDataId()).child("lastName").setValue(LName.getText().toString());
                        myRef.child("Employee").child(empClass.getDataId()).child("email").setValue(Email.getText().toString());
                        myRef.child("Employee").child(empClass.getDataId()).child("phone").setValue(Ph.getText().toString());
                        if (radioM.isChecked()) {

                            myRef.child("Employee").child(empClass.getDataId()).child("gender").setValue("Male");

                        } else if (radioF.isChecked()) {

                            myRef.child("Employee").child(empClass.getDataId()).child("gender").setValue("Female");
                        }

                        myRef.child("Email").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot:dataSnapshot.getChildren()){

                                    emailClass=snapshot.getValue(EmailClass.class);
                                    Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                                    SendProfileEmail sendEmail = new SendProfileEmail();
                                    sendEmail.execute(empClass.getEmail(), Email.getText().toString(), FName.getText().toString(), LName.getText().toString(), Ph.getText().toString(),emailClass.getEmail(),emailClass.getPass());
                                    builder.setCancelable(false);

                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        builder.setCancelable(false);
                    }
                });

                builder.show();
            }

        }
        if (item.getItemId() == R.id.delete) {

            final AlertDialog.Builder  builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Delete");

            builder.setMessage("Are you sure want to delete employee").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    myRef.child("Employee").child(empClass.getDataId()).removeValue();
                    getActivity().finish();
                    Toast.makeText(getContext(),"Deleted",Toast.LENGTH_SHORT).show();
                    builder.setCancelable(false);

                }});
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    builder.setCancelable(false);
                } });

            builder.show();

        }
        return super.onOptionsItemSelected(item);
    }

}
