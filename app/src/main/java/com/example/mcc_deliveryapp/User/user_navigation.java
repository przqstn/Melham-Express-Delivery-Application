package com.example.mcc_deliveryapp.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.mcc_deliveryapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class user_navigation extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    user_home_fragment user_home_fragment = new user_home_fragment();
    user_record_fragment user_record_fragment = new user_record_fragment();
    user_profile_fragment user_profile_fragment = new user_profile_fragment();
    private long pressedTime;
    MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_navigation);


//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        bottomNavigationView = findViewById(R.id.bottomNav_user2);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2,user_home_fragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
                menuItem = item;
                switch (item.getItemId()){
                    case R.id.iconhomeuser:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2,user_home_fragment).commit();
                        return true;
                }
                switch (item.getItemId()){
                    case R.id.iconrecorduser:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2,user_record_fragment).commit();
                        return true;
                }
                switch (item.getItemId()){
                    case R.id.iconprofileuser:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2,user_profile_fragment).commit();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            if (menuItem.getItemId() == R.id.iconhomeuser) {
                super.onBackPressed();
                finishAffinity();
            }
        }else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
        if(menuItem.getItemId()==R.id.iconrecorduser
                || menuItem.getItemId()==R.id.iconprofileuser){
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }
}