package com.example.mcc_deliveryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		Handler handler = new Handler();
		Intent intent = new Intent(this,MainUserActivity.class);
		Runnable r = new Runnable() {
			@Override
			public void run() {

				startActivity(intent);
			}
		};

		handler.postDelayed(r,3000);


	}
}