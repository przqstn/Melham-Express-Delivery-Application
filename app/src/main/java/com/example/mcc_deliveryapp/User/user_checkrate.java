package com.example.mcc_deliveryapp.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mcc_deliveryapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.SharedPreferences;
import android.widget.TextView;

import java.util.HashMap;
import java.util.UUID;

public class user_checkrate extends AppCompatActivity {
	FirebaseDatabase db =FirebaseDatabase.getInstance();
	DatabaseReference root = db.getReference().child("userparcel");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_checkrate);

		ImageButton btnMotorcycle = findViewById(R.id.btnMotorcycle);
		ImageButton btnSedan = findViewById(R.id.btnSedan);
		ImageButton btnTruck = findViewById(R.id.btnTruck);

		//
		EditText editText = (EditText)findViewById(R.id.editTextPickUp);
		EditText editText2 = (EditText)findViewById(R.id.editTextDestination);

		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		//getting the save shared preference of sender
		String senderloc =sharedPref.getString("key 1","");
		String sendercontact =sharedPref.getString("key 2","");
		String sendername =sharedPref.getString("key 3","");
		//getting the save shared preference of receiver
		String receiverloc =sharedPref.getString("key 4","");
		String receivercontact =sharedPref.getString("key 5","");
		String receivername =sharedPref.getString("key 6","");
		String sendernotes =sharedPref.getString("key 7","");
		String distance =sharedPref.getString("key 8","");
		String parcelstatus = "Pending";
		String ridername = "Searching";
		String ridernum = "Searching";

		editText.setText(senderloc);
		editText2.setText(receiverloc);

		btnMotorcycle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
						user_checkrate.this,R.style.BottomSheetDialogTheme
				);
				View bottomSheetView = LayoutInflater.from(getApplicationContext())
						.inflate(
								R.layout.motorcycle_dialog,
								(LinearLayout)findViewById(R.id.motorcycle_sheet_dialog)
						);
				// Specify the layout you are using.
//				setContentView(R.layout.motorcycle_dialog);
//
//				// Load and use views afterwards
				TextView tv1 = bottomSheetView.findViewById(R.id.txtFeeMotorcycle);


				String convertFloat =String.valueOf(calculateFare(distance, 0));

				tv1.setText(convertFloat);
				System.out.println(convertFloat);

				bottomSheetView.findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						pushData(sendernotes, senderloc, sendercontact, sendername, receiverloc,
								receivercontact, receivername ,"Motorcycle", convertFloat,
								parcelstatus, ridername, ridernum);
						Intent intent = new Intent(user_checkrate.this,user_findingrider.class);
						startActivity(intent);
					}
				});

				// temporarily disabled dialog box
				bottomSheetDialog.setContentView(bottomSheetView);
				bottomSheetDialog.show();
			}
		});

		btnSedan.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("SetTextI18n")
			@Override
			public void onClick(View view) {
				final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
						user_checkrate.this,R.style.BottomSheetDialogTheme
				);
				View bottomSheetView = LayoutInflater.from(getApplicationContext())
						.inflate(
								R.layout.sedan_dialog,
								(LinearLayout)findViewById(R.id.sedan_sheet_dialog)
						);

				bottomSheetView.findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						pushData(sendernotes, senderloc, sendercontact, sendername, receiverloc,
								receivercontact, receivername ,"Sedan", "",
								parcelstatus, ridername, ridernum);
						Intent intent = new Intent(user_checkrate.this,user_findingrider.class);
						startActivity(intent);
					}
				});
				bottomSheetDialog.setContentView(bottomSheetView);
				bottomSheetDialog.show();
			}
		});

		btnTruck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
						user_checkrate.this,R.style.BottomSheetDialogTheme
				);
				View bottomSheetView = LayoutInflater.from(getApplicationContext())
						.inflate(
								R.layout.truck_dialog,
								(LinearLayout)findViewById(R.id.truck_sheet_dialog)
						);
				bottomSheetView.findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						pushData(sendernotes, senderloc, sendercontact, sendername, receiverloc,
								receivercontact, receivername ,"Truck", "",
								parcelstatus, ridername, ridernum);
						Intent intent = new Intent(user_checkrate.this,user_findingrider.class);
						startActivity(intent);

					}
				});
				bottomSheetDialog.setContentView(bottomSheetView);
				bottomSheetDialog.show();
			}
		});
	}

	public void pushData(String sendernotes, String senderLoc, String senderCon,
						 String senderName, String receiverLoc, String receiverCon,
						 String receiverName, String vehicle, String fee, String parcelstatus,
						 String ridername, String ridernum) {

		HashMap<String, String> usermap = new HashMap<>();
		usermap.put("customernotes", sendernotes);
		usermap.put("senderlocation", senderLoc);
		usermap.put("sendercontact", senderCon);
		usermap.put("sendername", senderName);
		usermap.put("receiverlocation", receiverLoc);
		usermap.put("receivercontact", receiverCon);
		usermap.put("receivername", receiverName);
		usermap.put("vehicletype", vehicle);
		usermap.put("fee", fee);
		usermap.put("parcelstatus", parcelstatus);
		usermap.put("ridername", ridername);
		usermap.put("ridernum", ridernum);
		usermap.put("OrderID", orderID());

		// to get the unique key generated by firebase
		DatabaseReference newroot = root.push();
		String parcelId = newroot.getKey();
		newroot.setValue(usermap);
//		System.out.println(parcelId);
	}

	public static float calculateFare(String distance, int type){
		float removekm = Float.parseFloat(distance.replaceAll("[^\\d.]", ""));
		float finalFare = 0;
		float greatFive;

		if (type == 0) {
			float base = 49;
			if (removekm > 5){
				greatFive = removekm - 5;
				finalFare = (greatFive * 5) + (5 * 6) + base;
			}
			else {
				finalFare = 49 + (6 * removekm);
			}
		}
		return finalFare;
	}
	public static String orderID(){
		//generate random UUIDs
		UUID idOne = UUID.randomUUID();
		return String.valueOf(idOne);
	}
}