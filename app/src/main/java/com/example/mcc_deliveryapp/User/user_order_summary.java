package com.example.mcc_deliveryapp.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mcc_deliveryapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;

public class user_order_summary extends AppCompatActivity {
    FirebaseDatabase db =FirebaseDatabase.getInstance();
    DatabaseReference root = db.getReference().child("userparcel");
    TextView pickUpAddress, sendName, sendNumber, delAddress, recName, recNumber, vehicleType, totalPrice;
    EditText notes;
    Button orderPlace;
    ImageView vehicleIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_summary);
        Intent intent = getIntent();
        String userNumber = intent.getStringExtra("phonenum");
        String userName = intent.getStringExtra("username");
        String vehicletype = intent.getStringExtra("vehicle");
        String fee = intent.getStringExtra("fee");
        pickUpAddress = findViewById(R.id.pickUpAddress);
        sendName = findViewById(R.id.sendName);
        sendNumber = findViewById(R.id.sendNumber);
        delAddress = findViewById(R.id.delAddress);
        recName = findViewById(R.id.recName);
        recNumber = findViewById(R.id.recNumber);
        vehicleType = findViewById(R.id.vehicleType);
        totalPrice = findViewById(R.id.totalPrice);
        orderPlace = findViewById(R.id.orderPlace);
        vehicleIcon = findViewById(R.id.vehicleTypeIcon);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //getting the save shared preference of sender
        String senderloc =sharedPref.getString("key 1","");
        String sendercontact =sharedPref.getString("key 2","");
        String sendername =sharedPref.getString("key 3","");
        //getting the save shared preference of receiver
        String receiverloc =sharedPref.getString("key 4","");
        String receivercontact =sharedPref.getString("key 5","");
        String receivername =sharedPref.getString("key 6","");
        //String sendernotes =sharedPref.getString("key 7","");
        String distance =sharedPref.getString("key 8","");
        String userDefaultNumber =sharedPref.getString("key 9","");
        String userDefaultName = sharedPref.getString("key 10","");
        String startLatLng = sharedPref.getString("key 11","");
        String endLatLng = sharedPref.getString("key 12","");
        String parcelstatus = "Pending";
        String ridername = "Searching";
        String ridernum = "Searching";

        pickUpAddress.setText(senderloc);
        sendName.setText(sendername);
        sendNumber.setText(sendercontact);
        delAddress.setText(receiverloc);
        recName.setText(receivername);
        recNumber.setText(receivercontact);
        vehicleType.setText(vehicletype);
        totalPrice.setText("₱" + fee);

        switch (vehicletype)
        {
            case "Motorcycle":
                vehicleIcon.setImageDrawable(ContextCompat.getDrawable(user_order_summary.this, R.drawable.motorcycle));
                break;
            case "Sedan":
                vehicleIcon.setImageDrawable(ContextCompat.getDrawable(user_order_summary.this, R.drawable.sedan));
                break;
            case "SUV":
                vehicleIcon.setImageDrawable(ContextCompat.getDrawable(user_order_summary.this, R.drawable.suv));
                break;
            case "MPV":
                vehicleIcon.setImageDrawable(ContextCompat.getDrawable(user_order_summary.this, R.drawable.mpv));
                break;
            case "Small Truck":
                vehicleIcon.setImageDrawable(ContextCompat.getDrawable(user_order_summary.this, R.drawable.truck));
        }


        orderPlace.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                notes = findViewById(R.id.editTextNotes);
                String comment = notes.getText().toString();
                pushData(comment, senderloc, sendercontact, sendername, receiverloc,
                        receivercontact, receivername ,vehicletype,fee,
                        parcelstatus, ridername, ridernum, userDefaultNumber,
                        startLatLng, endLatLng);
                Intent intent = new Intent(user_order_summary.this, user_navigation.class);
                intent.putExtra("phonenum", userDefaultNumber);
                intent.putExtra("username", userDefaultName);

                startActivity(intent);

            }
        });
    }

    public void pushData(String comment , String senderLoc, String senderCon,
                         String senderName, String receiverLoc, String receiverCon,
                         String receiverName, String vehicle, String fee, String parcelstatus,
                         String ridername, String ridernum, String defaultNumber,
                         String startLoc, String endLoc) {

        HashMap<String, String> usermap = new HashMap<>();
        usermap.put("customernotes", comment);
        usermap.put("senderlocation", senderLoc);
        usermap.put("sendercontact", senderCon);
        usermap.put("sendername", senderName);
        usermap.put("receiverlocation", receiverLoc);
        usermap.put("receivercontact", receiverCon);
        usermap.put("receivername", receiverName);
        usermap.put("vehicletype", vehicle);
        usermap.put("fee", fee);
        usermap.put("parcelstatus", parcelstatus+vehicle);
        usermap.put("ridername", ridername);
        usermap.put("ridernum", ridernum);
        usermap.put("OrderID", orderID());
        usermap.put("DatePlace", dateTime());
        usermap.put("userParcelStatus", "Pending" + defaultNumber);
        usermap.put("defaultUserNum", defaultNumber);
        usermap.put("startLocation", startLoc);
        usermap.put("endLocation", endLoc);
        usermap.put("dateCompleted", "");
        usermap.put("cancellationReason", "");
        usermap.put("parcelState", "pending");

        // To get the unique key generated by firebase
        DatabaseReference newroot = root.push();
        String parcelId = newroot.getKey();
        newroot.setValue(usermap);
//		System.out.println(parcelId);
    }
    public static String orderID(){
        //generate random UUIDs
        UUID idOne = UUID.randomUUID();
        return String.valueOf(idOne);
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
}

