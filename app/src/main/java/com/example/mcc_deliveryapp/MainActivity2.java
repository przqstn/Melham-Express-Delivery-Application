package com.example.mcc_deliveryapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mcc_deliveryapp.Rider.riderLogin;
import com.example.mcc_deliveryapp.User.MainUserActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity2 extends AppCompatActivity {
	Button btnriderform,btnuserform;
	ProgressDialog progressDialog;
	Timer timer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);

		btnriderform = findViewById(R.id.btn_riderform);
		btnuserform = findViewById(R.id.btn_userform);

		btnriderform.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent2 = new Intent(MainActivity2.this, riderLogin.class);
				startActivity(intent2);
				progressDialog = new ProgressDialog(MainActivity2.this);
				progressDialog.show();
				progressDialog.setContentView(R.layout.progresslayout);
				progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
				timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						progressDialog.dismiss();
					}
				},5000);
				Log.d("check", "hello rider!");
			}
		});

		btnuserform.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity2.this, MainUserActivity.class);
				startActivity(intent);
				progressDialog = new ProgressDialog(MainActivity2.this);
				progressDialog.show();
				progressDialog.setContentView(R.layout.progresslayout);
				progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
				timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						progressDialog.dismiss();
					}
				},5000);
				Log.d("check", "hello user!");
			}
		});
	}
}