package com.example.mcc_deliveryapp.Rider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.mcc_deliveryapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class rider_cancel_order extends AppCompatActivity {
    Button confirmCancellation, goBack;
    EditText reasonOthers;
    String stateRadio = null;
    private String phonenum, orderID, name, riderVehicle,  defaultUserNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_cancel_order);
        reasonOthers = findViewById(R.id.reasonOthers);
        confirmCancellation = findViewById(R.id.confirmCancellation);
        goBack = findViewById(R.id.goBack);
        Intent intent = getIntent();
        name = intent.getStringExtra("username");
        phonenum = intent.getStringExtra("phonenum");
        orderID = intent.getStringExtra("orderID");
        riderVehicle = intent.getStringExtra("vehicle");
        defaultUserNum = intent.getStringExtra("defaultUserNum");

       confirmCancellation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stateRadio != null && !stateRadio.equals("otherOpt")) {
                    dataPush();

                    Intent intent = new Intent(rider_cancel_order.this, rider_dashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("orderID", orderID);
                    intent.putExtra("phonenum", phonenum);
                    intent.putExtra("username", name);
                    intent.putExtra("vehicle", riderVehicle);
                    intent.putExtra("defaultUserNum", defaultUserNum);
                    startActivity(intent);
                } else if (stateRadio != null){
                    stateRadio = reasonOthers.getText().toString();
                    dataPush();
                    Intent intent = new Intent(rider_cancel_order.this, rider_dashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("orderID", orderID);
                    intent.putExtra("phonenum", phonenum);
                    intent.putExtra("username", name);
                    intent.putExtra("vehicle", riderVehicle);
                    intent.putExtra("defaultUserNum", defaultUserNum);
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "Please select or enter the reason of cancellation.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rider_cancel_order.this, rider_ongoing_order.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("orderID", orderID);
                intent.putExtra("phonenum", phonenum);
                intent.putExtra("username", name);
                intent.putExtra("vehicle", riderVehicle);
                intent.putExtra("defaultUserNum", defaultUserNum);
                startActivity(intent);
            }
        });
    }

    public void dataPush() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference dr = database.getReference().child("userparcel");
        Query query = dr.orderByChild("OrderID").equalTo(orderID);

        query.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        dr.child(dataSnapshot.getKey()).child("parcelstatus").setValue("Cancelled" + phonenum);
                        String userdefnum = dataSnapshot.child("defaultUserNum").getValue().toString();
                        dr.child(dataSnapshot.getKey()).child("userParcelStatus").setValue("Cancelled" + userdefnum);
                        dr.child(dataSnapshot.getKey()).child("cancellationReason").setValue(stateRadio);
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
    }

    @SuppressLint("NonConstantResourceId")
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.systemError:
                if (checked) {
                    stateRadio = "System Error";
                    reasonOthers.setEnabled(false);
                }
                break;
            case R.id.changeOfMind:
                if (checked) {
                    stateRadio = "Customer requested due to change of mind";
                    reasonOthers.setEnabled(false);
                }
                break;
            case R.id.duplicateOrder:
                if (checked) {
                    stateRadio = "Customer requested due to duplicate order";
                    reasonOthers.setEnabled(false);
                }
                break;
            case R.id.incorrectAddress:
                if (checked) {
                    stateRadio = "Customer shipping address is incorrect/incomplete";
                    reasonOthers.setEnabled(false);
                }
                break;
            case R.id.customerUnreachable:
                if (checked) {
                    stateRadio = "Customer unreachable";
                    reasonOthers.setEnabled(false);
                }
                break;
            case R.id.otherOpt:
                if (checked) {
                    stateRadio = "otherOpt";
                    reasonOthers.setEnabled(true);
                }
                break;
        }
    }
}