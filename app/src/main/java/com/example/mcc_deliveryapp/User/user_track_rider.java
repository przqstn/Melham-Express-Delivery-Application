package com.example.mcc_deliveryapp.User;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mcc_deliveryapp.R;
import com.example.mcc_deliveryapp.User.Module.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class user_track_rider extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private DatabaseReference databaseReference;
    private DatabaseReference riderReference;
    private static final int DEFAULT_ZOOM = 18;

    HashMap markerMap = new HashMap();
    Button back;
    String riderNumber, orderID, phonenum, name, riderName;
    TextView riderNameUI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        orderID = intent.getStringExtra("orderID");
        name = intent.getStringExtra("username");
        phonenum = intent.getStringExtra("phonenum");
        riderName = intent.getStringExtra("ridername");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_track_rider);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        databaseReference = FirebaseDatabase.getInstance().getReference("userparcel");
        riderReference = FirebaseDatabase.getInstance().getReference("riders");
        Query checkUser = databaseReference.orderByChild("OrderID");
        Query checkRider = riderReference.orderByChild("riderphone");
        riderNameUI =  findViewById(R.id.RiderName);
        back = findViewById(R.id.backbutton);
      riderNameUI.setText(riderName);


checkUser.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        for (DataSnapshot parcelSnapshot : snapshot.getChildren()) {
                if (parcelSnapshot.child("OrderID").getValue().equals(orderID))
                {
                    riderNumber = parcelSnapshot.child("ridernum").getValue(String.class);

                }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});

        checkRider.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> latitudes = new ArrayList<>(Collections.emptyList());
                List<String> longitudes = new ArrayList<>(Collections.emptyList());
                List<String> riderphonenum = new ArrayList<>(Collections.emptyList());

                mMap.clear();
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    if (locationSnapshot.child("riderphone").getValue().equals(riderNumber))
                    {
                        String latitude = locationSnapshot.child("latitude").getValue().toString();
                        latitudes.add(latitude);
                    }
                }
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    if (locationSnapshot.child("riderphone").getValue().equals(riderNumber))
                    {
                        String longitude = locationSnapshot.child("longitude").getValue().toString();
                        longitudes.add(longitude);
                    }
                }
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    if (locationSnapshot.child("riderphone").getValue().equals(riderNumber))
                    {
                        String riderphone = locationSnapshot.child("riderphone").getValue().toString();
                        riderphonenum.add(riderphone);
                    }
                }

                for (int i = 0; i < latitudes.size(); i++) {
                    LatLng latLng = new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i)));
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(riderphonenum.get(i)));
                    markerMap.put(riderphonenum.get(i), marker);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user_track_rider.this, user_navigation.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("orderID",  orderID);
                intent.putExtra("phonenum", phonenum);
                intent.putExtra("username", name);
                startActivity(intent);
            }
        });


       /* bale eto yung dating button tinanggal ko na muna
        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inputNum = riderphone.getText().toString();
                Marker marker = (Marker) markerMap.get(inputNum);

                if (marker != null) {
                    LatLng pos = marker.getPosition();
                    LatLngBounds lockOn = new LatLngBounds(pos, pos);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, DEFAULT_ZOOM));
                }
                else {
                    Toast.makeText(getBaseContext(), "Phone number not registered", Toast.LENGTH_SHORT).show();
                }


            }
        });
        if (inputNum != null) {
            Marker marker = (Marker) markerMap.get(inputNum);
            LatLng pos = marker.getPosition();
            LatLngBounds lockOn = new LatLngBounds(pos, pos);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, DEFAULT_ZOOM));
        }
      */
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.customize_maps_style));

    }


}

