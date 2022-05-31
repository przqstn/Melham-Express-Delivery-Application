package com.example.mcc_deliveryapp.Rider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mcc_deliveryapp.R;

public class rider_dashboard1 extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rider_dashboard1);
		Button button = (Button) findViewById(R.id.btn_pckup);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openActivity3();
			}
		});
	}
	public void openActivity3(){
		Intent intent = new Intent(this,rider_dashboard2.class);
		startActivity(intent);
	}
}