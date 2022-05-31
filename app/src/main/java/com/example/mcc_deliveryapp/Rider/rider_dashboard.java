package com.example.mcc_deliveryapp.Rider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mcc_deliveryapp.R;

public class rider_dashboard extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rider_dashboard);

		ImageButton button = (ImageButton) findViewById(R.id.btn_pickup);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openActivity2();
			}
		});
	}
	public void openActivity2(){
		Intent intent = new Intent(this,rider_dashboard1.class);
		startActivity(intent);
	}
}