package com.example.mcc_deliveryapp.Rider;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mcc_deliveryapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class take_order extends AppCompatActivity {

    String name, phonenum, orderID, riderVehicle, senderName, senderLocation, senderContact,
            receiverName, receiverLocation, receiverContact, vehicleType, senderNote, orderPrice;
    TextView senderloc, sendername, sendercontact, receiverloc, receivername, receivercontact,
                order_id, vehicletype, usernote, parcelprice;
    Button btn_takeOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_order);
        requestPermission();

        Intent intent = getIntent();
        name = intent.getStringExtra("username");
        phonenum = intent.getStringExtra("phonenum");
        orderID = intent.getStringExtra("orderID");
        riderVehicle = intent.getStringExtra("vehicle");
        System.out.println(name);

        senderloc = findViewById(R.id.sender_loc);
        sendername = findViewById(R.id.sender_name);
        sendercontact = findViewById(R.id.sender_contact);
        receiverloc = findViewById(R.id.receiver_loc);
        receivername = findViewById(R.id.receiver_name);
        receivercontact = findViewById(R.id.receiver_contact);
        order_id = findViewById(R.id.parcelorder_ID);
        vehicletype = findViewById(R.id.vehicle);
        usernote = findViewById(R.id.note_rider);
        parcelprice = findViewById(R.id.txt_price);
        btn_takeOrder = findViewById(R.id.btn_takeOrder3);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference dr = database.getReference().child("userparcel");
        Query query = dr.orderByChild("OrderID").equalTo(orderID);
        System.out.println(orderID);

        query.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        senderName = dataSnapshot.child("sendername").getValue(String.class);
                        senderLocation = dataSnapshot.child("senderlocation").getValue(String.class);
                        senderContact = dataSnapshot.child("sendercontact").getValue(String.class);
                        receiverName = dataSnapshot.child("receivername").getValue(String.class);
                        receiverLocation = dataSnapshot.child("receiverlocation").getValue(String.class);
                        receiverContact = dataSnapshot.child("receivercontact").getValue(String.class);
                        vehicleType = dataSnapshot.child("vehicletype").getValue(String.class);
                        senderNote = dataSnapshot.child("customernotes").getValue(String.class);
                        orderPrice = dataSnapshot.child("fee").getValue(String.class);
                        sendername.setText(senderName);
                        senderloc.setText(senderLocation);
                        sendercontact.setText(senderContact);
                        receivername.setText(receiverName);
                        receiverloc.setText(receiverLocation);
                        receivercontact.setText(receiverContact);
                        order_id.setText(orderID);
                        vehicletype.setText(vehicleType);
                        usernote.setText(senderNote);
                        parcelprice.setText("₱"+orderPrice);
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

        btn_takeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference dr = database.getReference().child("userparcel");
                Query query = dr.orderByChild("OrderID").equalTo(orderID);

                query.addChildEventListener(
                        new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                dr.child(dataSnapshot.getKey()).child("parcelstatus").setValue("Ongoing"+phonenum);
                                dr.child(dataSnapshot.getKey()).child("ridername").setValue(name);
                                dr.child(dataSnapshot.getKey()).child("ridernum").setValue(phonenum);
                                String userdefnum = dataSnapshot.child("defaultUserNum").getValue().toString();
                                dr.child(dataSnapshot.getKey()).child("userParcelStatus").setValue("Ongoing"+userdefnum);
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
                Intent intent = new Intent(take_order.this, rider_takeorder_map.class);
                intent.putExtra("phonenum", phonenum);
                intent.putExtra("username", name);
                intent.putExtra("vehicle", riderVehicle);
                intent.putExtra("orderID", orderID);
                startActivity(intent);
            }
        });
    }

    public void requestPermission(){
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                fineLocationGranted = result.getOrDefault(
                                        Manifest.permission.ACCESS_FINE_LOCATION, false);
                            }
                            Boolean coarseLocationGranted = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                coarseLocationGranted = result.getOrDefault(
                                        Manifest.permission.ACCESS_COARSE_LOCATION,false);
                            }


                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
//					} else if (backgroundLocationGranted != null && backgroundLocationGranted){

                                // only background location granted
                            } else {
                                onBackPressed();
                            }
                        }
                );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            locationPermissionRequest.launch(new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(take_order.this, rider_dashboard.class);
        intent.putExtra("phonenum", phonenum);
        intent.putExtra("username", name);
        intent.putExtra("vehicle", riderVehicle);
        startActivity(intent);
    }
}