package com.example.mcc_deliveryapp.Rider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;

public class rider_ongoing_order extends AppCompatActivity {

    String name, phonenum, orderID, riderVehicle, senderName, senderLocation, senderContact,
            receiverName, receiverLocation, receiverContact, vehicleType, senderNote, orderPrice,
            defaultUserNum, customerName, orderPlaced;
    TextView senderloc, sendername, sendercontact, receiverloc, receivername, receivercontact,
            order_id, vehicletype, usernote, parcelprice, customer_name, order_placed;
    Button btn_CompleteOrder, btn_cancelOrderRider, btn_MessageCustomer, btn_CallCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_ongoing_order);

        Intent intent = getIntent();
        name = intent.getStringExtra("username");
        phonenum = intent.getStringExtra("phonenum");
        orderID = intent.getStringExtra("orderID");
        riderVehicle = intent.getStringExtra("vehicle");

        senderloc = findViewById(R.id.sender_loc2);
        sendername = findViewById(R.id.sender_name2);
        sendercontact = findViewById(R.id.sender_contact2);
        receiverloc = findViewById(R.id.receiver_loc2);
        receivername = findViewById(R.id.receiver_name2);
        receivercontact = findViewById(R.id.receiver_contact2);
        order_id = findViewById(R.id.parcelorder_ID2);
        vehicletype = findViewById(R.id.vehicle2);
        usernote = findViewById(R.id.note_rider2);
        parcelprice = findViewById(R.id.txt_price2);
        customer_name = findViewById(R.id.customer_name);
        order_placed = findViewById(R.id.order_placed);
        btn_MessageCustomer = findViewById(R.id.message_customer);
        btn_CallCustomer = findViewById(R.id.call_customer);
        btn_CompleteOrder = findViewById(R.id.btn_completeOrder);
        btn_cancelOrderRider = findViewById(R.id.btn_cancelOrderRider);

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
                        orderPlaced = dataSnapshot.child("DatePlace").getValue(String.class);
                        defaultUserNum = dataSnapshot.child("defaultUserNum").getValue(String.class);

                        final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                        final DatabaseReference dr2 = database2.getReference().child("users");
                        Query query = dr2.orderByChild("userPhone").equalTo(defaultUserNum);

                        query.addChildEventListener(
                                new ChildEventListener() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                                        customerName = dataSnapshot.child("userFullname").getValue(String.class);
                                        customer_name.setText(customerName);
                                    }

                                    @Override
                                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

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

                        sendername.setText(senderName);
                        senderloc.setText(senderLocation);
                        sendercontact.setText(senderContact);
                        receivername.setText(receiverName);
                        receiverloc.setText(receiverLocation);
                        receivercontact.setText(receiverContact);
                        order_id.setText(orderID);
                        vehicletype.setText(vehicleType);
                        usernote.setText(senderNote);
                        parcelprice.setText(orderPrice);
                        order_placed.setText(orderPlaced);
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

        btn_MessageCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textnum = defaultUserNum;
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("vnd.android-dir/mms-sms");
                intent.setData(Uri.parse("sms:" + textnum));
                startActivity(intent);
            }
        });


        btn_CallCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Calling");
                String callnum = defaultUserNum;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + callnum));
                startActivity(intent);
            }
        });

        btn_CompleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference dr = database.getReference().child("userparcel");
                Query query = dr.orderByChild("OrderID").equalTo(orderID);

                query.addChildEventListener(
                        new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                                dr.child(Objects.requireNonNull(dataSnapshot.getKey())).child("parcelstatus").setValue("Completed"+phonenum);
                                dr.child(dataSnapshot.getKey()).child("dateCompleted").setValue(dateTime());
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
                Intent intent = new Intent(rider_ongoing_order.this, rider_dashboard.class);
                intent.putExtra("phonenum", phonenum);
                intent.putExtra("username", name);
                intent.putExtra("vehicle", riderVehicle);
                startActivity(intent);
            }
        });

        btn_cancelOrderRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference dr = database.getReference().child("userparcel");
                Query query = dr.orderByChild("OrderID").equalTo(orderID);

                query.addChildEventListener(
                        new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                dr.child(dataSnapshot.getKey()).child("parcelstatus").setValue("Cancelled"+phonenum);
                                String userdefnum = dataSnapshot.child("defaultUserNum").getValue().toString();
                                dr.child(dataSnapshot.getKey()).child("userParcelStatus").setValue("Cancelled"+userdefnum);
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
                Intent intent = new Intent(rider_ongoing_order.this, rider_dashboard.class);
                intent.putExtra("phonenum", phonenum);
                intent.putExtra("username", name);
                intent.putExtra("vehicle", riderVehicle);
                startActivity(intent);
            }
        });
    }

    public static String dateTime(){
        LocalDateTime myDateObj = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            myDateObj = LocalDateTime.now();
        }
        DateTimeFormatter myFormatObj = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            myFormatObj = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        }
        String formattedDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedDate = myDateObj.format(myFormatObj);
        }
        return formattedDate;
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(rider_ongoing_order.this, rider_dashboard.class);
        intent.putExtra("phonenum", phonenum);
        intent.putExtra("username", name);
        intent.putExtra("vehicle", riderVehicle);
        startActivity(intent);
    }
}