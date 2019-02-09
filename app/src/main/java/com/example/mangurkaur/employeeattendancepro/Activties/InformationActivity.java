package com.example.mangurkaur.employeeattendancepro.Activties;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.mangurkaur.employeeattendancepro.R;

public class InformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.overviewTab:
                        selectedFragment = new OverviewFragment();
                        transaction.replace(R.id.content,selectedFragment);
                        transaction.commit();
                        break;
                    case R.id.timeTab:
                        selectedFragment = new TimeSheetFragment();
                        transaction.replace(R.id.content,selectedFragment);
                        transaction.commit();
                        break;
                    case R.id.profileTab:
                        selectedFragment = new ProfileFragment();
                        transaction.replace(R.id.content,selectedFragment);
                        transaction.commit();
                        break;

                }

                return true;
            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new OverviewFragment());
        transaction.commit();

    }


}
