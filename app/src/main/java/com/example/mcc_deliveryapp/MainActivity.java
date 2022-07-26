package com.example.mcc_deliveryapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Handler handler = new Handler();
		Intent intent = new Intent(this, MainActivity2.class);
		Runnable r = new Runnable() {
			@Override
			public void run() {

				startActivity(intent);
			}
		};
		FirebaseApp.initializeApp(/*context=*/ this);
		FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
		firebaseAppCheck.installAppCheckProviderFactory(
				SafetyNetAppCheckProviderFactory.getInstance());

		handler.postDelayed(r,3000);


	}
}