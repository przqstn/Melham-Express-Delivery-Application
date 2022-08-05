package com.example.mcc_deliveryapp.Rider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mcc_deliveryapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class rider_dashboard extends AppCompatActivity {
	FirebaseDatabase firebaseDatabase;
	DatabaseReference databaseReference;

	BottomNavigationView bottomNavigationView;

	pickup_fragment pickupFragment = new pickup_fragment();
	profile_fragment profile_fragment = new profile_fragment();
	record_fragment record_fragment = new record_fragment();
	courierHomeFragment courierHomeFragment = new courierHomeFragment();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rider_dashboard);


		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		bottomNavigationView = findViewById(R.id.bottomNav_rider);
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, courierHomeFragment).commit();
		bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem item) {
				switch (item.getItemId()) {
					case R.id.iconhome:
						getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, courierHomeFragment).commit();
						return true;
					case R.id.iconpickup:
						getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, pickupFragment).commit();
						return true;
					case R.id.iconrecord:
						getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, record_fragment).commit();
						return true;
					case R.id.iconprofile:
						getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profile_fragment).commit();
						return true;
				}

				return false;
			}
		});

	}
}