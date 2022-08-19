package com.example.mcc_deliveryapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mcc_deliveryapp.Rider.riderLogin;
import com.example.mcc_deliveryapp.User.MainUserActivity;
import com.example.mcc_deliveryapp.User.user_checkrate;
import com.example.mcc_deliveryapp.User.user_ongoing_order_details2;
import com.example.mcc_deliveryapp.User.user_order_summary;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity2 extends AppCompatActivity {
	Button btnriderform,btnuserform,btntrack;
	ProgressDialog progressDialog;
	Timer timer;
	EditText orderID;
	String userDefaultNumber, ridernum;
	private long pressedTime;
	private DatabaseReference getRiderRef;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);

		btnriderform = findViewById(R.id.btn_riderform);
		btnuserform = findViewById(R.id.btn_userform);
		btntrack = findViewById(R.id.btn_trackorder);

		// This callback will only be called when MyFragment is at least Started.
		OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
			@Override
			public void handleOnBackPressed() {
				if (pressedTime + 2000 > System.currentTimeMillis()) {
					finishAffinity();
				} else {
					Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
				}
				pressedTime = System.currentTimeMillis();
			}
		};
		this.getOnBackPressedDispatcher().addCallback(this, callback);

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
			}
		});

		btntrack.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("SetTextI18n")
			@Override
			public void onClick(View view) {
				final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
						MainActivity2.this,R.style.BottomSheetDialogTheme
				);
				View bottomSheetView = LayoutInflater.from(getApplicationContext())
						.inflate(
								R.layout.trackorderbutton_dialog,
								findViewById(R.id.trackorder_button)
						);

				orderID = (EditText) bottomSheetView.findViewById(R.id.OrderID);

				bottomSheetView.findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						final FirebaseDatabase database = FirebaseDatabase.getInstance();
						final DatabaseReference dr = database.getReference().child("userparcel");
						Query query = dr.orderByChild("OrderID").equalTo(orderID.getText().toString());

						System.out.println(orderID);

						query.addChildEventListener(
								new ChildEventListener() {
									@Override
									public void onChildAdded(DataSnapshot dataSnapshot, String s) {
										userDefaultNumber = dataSnapshot.child("defaultUserNum").getValue(String.class);
										ridernum = dataSnapshot.child("ridernum").getValue(String.class);
									}

									@Override
									public void onChildRemoved(DataSnapshot dataSnapshot) {

									}

									@Override
									public void onChildMoved(DataSnapshot dataSnapshot, String s) {
									}

									@Override
									public void onCancelled(@NonNull DatabaseError error) {

									}

									@Override
									public void onChildChanged(DataSnapshot dataSnapshot, String s) {
									}
								});

						if (ridernum != null) {
							Log.e("UserNUm", userDefaultNumber);
							Log.e("RiderNUm", ridernum);

							Intent intent = new Intent(MainActivity2.this, user_ongoing_order_details2.class);
							intent.putExtra("phonenum", userDefaultNumber);
							intent.putExtra("orderID", orderID.getText().toString());
							intent.putExtra("ridernum", ridernum);
							startActivity(intent);
						}
					}
				});
				bottomSheetDialog.setContentView(bottomSheetView);
				bottomSheetDialog.show();
			}
		});
	}


}