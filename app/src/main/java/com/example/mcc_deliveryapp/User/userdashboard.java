package com.example.mcc_deliveryapp.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mcc_deliveryapp.R;

public class userdashboard extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		setContentView(R.layout.activity_userdashboard);
		Button btnGetStart = findViewById(R.id.btnGetStart);
		btnGetStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(userdashboard.this,user_checkrate.class);
				startActivity(intent);
				// to transfer
//				final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
//						userdashboard.this,R.style.BottomSheetDialogTheme
//				);
//				View bottomSheetView = LayoutInflater.from(getApplicationContext())
//						.inflate(
//								R.layout.user_bottom_sheet,
//								(LinearLayout)findViewById(R.id.bottomSheetContainer)
//						);
//				bottomSheetView.findViewById(R.id.txtViewCancel).setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View view) {
//						Toast.makeText(userdashboard.this, "Order Cancelled", Toast.LENGTH_SHORT).show();
//						bottomSheetDialog.dismiss();
//					}
//				});
//				bottomSheetDialog.setContentView(bottomSheetView);
//				bottomSheetDialog.show();
			}
		});
	}
}