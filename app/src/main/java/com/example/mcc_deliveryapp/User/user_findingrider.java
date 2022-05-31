package com.example.mcc_deliveryapp.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mcc_deliveryapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class user_findingrider extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_user_findingrider);
		Button dotMenu = findViewById(R.id.dotMenu);
		ImageView imageView1 = findViewById(R.id.imageView1);


		dotMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
						user_findingrider.this,R.style.BottomSheetDialogTheme
				);
				View bottomSheetView = LayoutInflater.from(getApplicationContext())
						.inflate(
								R.layout.layout_bottom_sheet,
								(LinearLayout)findViewById(R.id.bottomSheetContainer)
						);
				bottomSheetView.findViewById(R.id.txtViewCancel).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Toast.makeText(user_findingrider.this, "Order Cancelled test", Toast.LENGTH_SHORT).show();
						bottomSheetDialog.dismiss();
					}
				});
				bottomSheetDialog.setContentView(bottomSheetView);
				bottomSheetDialog.show();
			}
		});

		imageView1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(user_findingrider.this,user_driverfound.class);
				startActivity(intent);
			}
		});
	}
}