package com.example.mangurkaur.employeeattendancepro.Activties;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangurkaur.employeeattendancepro.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<EmpClass> list = new ArrayList<EmpClass>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private DatabaseReference connectedRef;
    private EmpAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        startService(new Intent(AdminActivity.this,NotificationService.class));
        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference();
        connectedRef=firebaseDatabase.getReference(".info/connected");
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewList);
        adapter = new EmpAdapter(list,getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        ImageView fab = (ImageView) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminActivity.this,AddActivity.class);
                startActivity(intent);

            }
        });
        myRef.child("Employee").addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

             list.clear();
             for(DataSnapshot empdataSnapshot:dataSnapshot.getChildren()){

                 EmpClass empClass=empdataSnapshot.getValue(EmpClass.class);
                 list.add(empClass);

             }

             adapter.notifyDataSetChanged();
         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {

         }
     });

        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                boolean connected=dataSnapshot.getValue(Boolean.class);

                if(connected){

                    Toast.makeText(getApplicationContext(),"Internet is connected",Toast.LENGTH_SHORT).show();
                }
                else{

                    Toast.makeText(getApplicationContext(),"Internet connection is lost",Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.admin, menu);

        getSupportActionBar().setTitle("Employee List");

        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_changePass) {
            Intent intent=new Intent(AdminActivity.this,SettingsActivity.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.action_changeEmail) {
            Intent intent=new Intent(AdminActivity.this,EmailChange.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            finish();

        }
        return super.onOptionsItemSelected(item);
    }


}
