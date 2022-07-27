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



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rider_dashboard);


		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		bottomNavigationView = findViewById(R.id.bottomNav_rider);
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,pickupFragment).commit();
		bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected( MenuItem item) {
				switch (item.getItemId()){
					case R.id.iconpickup:
						getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,pickupFragment).commit();
						return true;
					case R.id.iconrecord:
						getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,record_fragment).commit();
						return true;
					case R.id.iconprofile:
						getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,profile_fragment).commit();
						return true;
				}

				return false;
			}
		});


//		ImageButton button = (ImageButton) findViewById(R.id.btn_pickup);
//		button.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				openActivity2();
//			}
//		});

//		firebaseDatabase = FirebaseDatabase.getInstance();
//
//		// below line is used to get
//		// reference for our database.
//		databaseReference = firebaseDatabase.getReference("user parcel infos");
//
//		getdata();
	}
	public void openActivity2(){
		Intent intent = new Intent(this,rider_dashboard1.class);
		startActivity(intent);
	}

	// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.

//	public void getdata(){
//		databaseReference.addValueEventListener(new ValueEventListener() {
//			@Override
//			public void onDataChange(@NonNull DataSnapshot snapshot) {
//				// this method is call to get the realtime
//				// updates in the data.
//				// this method is called when the data is
//				// changed in our Firebase console.
//				// below line is for getting the data from
//				// snapshot of our database.
//				String value = snapshot.getValue(String.class);
//
//				// after getting the value we are setting
//				// our value to our text view in below line.
//				.setText(value);
//			}
//
//			@Override
//			public void onCancelled(@NonNull DatabaseError error) {
//				// calling on cancelled method when we receive
//				// any error or we are not able to get the data.
//				Toast.makeText(rider_dashboard.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
//			}
//		});
//	}
//	}
}