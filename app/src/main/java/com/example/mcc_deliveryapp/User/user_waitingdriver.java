package com.example.mcc_deliveryapp.User;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mcc_deliveryapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class user_waitingdriver extends AppCompatActivity {
	ImageButton imageButton;
	TextView chat1;
	Dialog dialog;
	Button button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_user_waitingdriver);


		ImageButton imageButton = findViewById(R.id.imageButton);
		TextView chat1 = findViewById(R.id.chat1);
		Button dotMenu = findViewById(R.id.dotMenu);
		LinearLayout driverDialog;

		View view = getLayoutInflater().inflate(R.layout.driver_bottom_sheet,null);
		dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
		dialog.setContentView(view);

		driverDialog = view.findViewById(R.id.driverDialog);

		imageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.show();
			}
		});

		driverDialog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
				Toast.makeText(user_waitingdriver.this, "Test", Toast.LENGTH_SHORT).show();
			}
		});

		dotMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
						user_waitingdriver.this,R.style.BottomSheetDialogTheme
				);
				View bottomSheetView = LayoutInflater.from(getApplicationContext())
						.inflate(
								R.layout.layout_bottom_sheet,
								(LinearLayout)findViewById(R.id.bottomSheetContainer)
						);
				bottomSheetView.findViewById(R.id.txtViewCancel).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Toast.makeText(user_waitingdriver.this, "Order Cancelled test", Toast.LENGTH_SHORT).show();
						bottomSheetDialog.dismiss();
					}
				});
				bottomSheetDialog.setContentView(bottomSheetView);
				bottomSheetDialog.show();
			}
		});

	}
}