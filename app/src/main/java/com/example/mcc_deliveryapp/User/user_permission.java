package com.example.mcc_deliveryapp.User;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mcc_deliveryapp.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class user_permission extends AppCompatActivity {

Button btn_permission;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_permission);

		btn_permission = findViewById(R.id.btn_user_permission);
		Intent intent = getIntent();
		String name = intent.getStringExtra("username");
		String phonenum = intent.getStringExtra("phonenum");
		if(ContextCompat.checkSelfPermission(user_permission.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
		{
			Intent home = new Intent(user_permission.this,user_navigation.class);
			home.putExtra("phonenum", phonenum);
			home.putExtra("username", name);
			System.out.println("Permission " + name + phonenum);

			startActivity(home);
			finish();
			return;
		}

		btn_permission.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Dexter.withContext(user_permission.this)
						.withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
						.withListener(new PermissionListener() {
							@Override
							public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
								Intent home = new Intent(user_permission.this,user_navigation.class);
								home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
								home.putExtra("phonenum", phonenum);
								home.putExtra("username", name);
								startActivity(home);
								finish();
							}

							@Override
							public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
								 if(permissionDeniedResponse.isPermanentlyDenied()){
									 AlertDialog.Builder builder = new AlertDialog.Builder(user_permission.this);
									 builder.setTitle("Permission Denied")
											 .setMessage("Permission to access Device location is permanently denied. go to settings in order allow the permission.")
											 .setNegativeButton("Cancel" ,null)
											 .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
												 @Override
												 public void onClick(DialogInterface dialogInterface, int i) {
													 Intent intent = new Intent();
													 intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
													 intent.setData(Uri.fromParts("package",getPackageName(),null));
												 }
											 })
											 .show();
								 }else{
									 Toast.makeText(user_permission.this,"Permission Denied",Toast.LENGTH_SHORT).show();
								 }
							}

							@Override
							public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
								permissionToken.continuePermissionRequest();
							}
						})
						.check();
			}
		});



	}
}