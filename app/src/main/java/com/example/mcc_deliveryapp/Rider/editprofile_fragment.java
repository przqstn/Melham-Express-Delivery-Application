package com.example.mcc_deliveryapp.Rider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mcc_deliveryapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class editprofile_fragment extends AppCompatActivity {

    private Button btnCancel;
    private TextView viewphoneNum, viewname, viewvehicleType, viewplateNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_editprofile_fragment);

        btnCancel = findViewById(R.id.btn_cancelChanges);

        Intent intent = getIntent();
        String phoneNum = intent.getStringExtra("riderphone");
        viewphoneNum=findViewById(R.id.riderNumber);
        viewphoneNum.setText(phoneNum);

        String name = intent.getStringExtra("name");
        viewname=findViewById(R.id.txt_name);
        viewname.setText(name);

        String vehicleType = intent.getStringExtra("vehicletype");
        viewvehicleType=findViewById(R.id.riderVehicle);
        viewvehicleType.setText(vehicleType);

        String plateNum = intent.getStringExtra("platenumber");
        viewplateNum=findViewById(R.id.riderPlate);
        viewplateNum.setText(plateNum);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });
    }
}
