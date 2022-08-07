package com.example.mcc_deliveryapp.Rider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.mcc_deliveryapp.R;
import com.example.mcc_deliveryapp.User.user_navigation;
import com.example.mcc_deliveryapp.User.user_track_rider;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class rider_takeorder_map extends FragmentActivity implements OnMapReadyCallback {
    String name, phonenum, orderID, riderVehicle;

    private GoogleMap mMap;
    private DatabaseReference databaseReference;
    private DatabaseReference riderReference;
    private static final int DEFAULT_ZOOM = 18;
    Button track;
    HashMap markerMap = new HashMap();
    String inputNum = null;
    Button back;
    String userPhoneNum, userName, riderNumber;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private final long MIN_TIME = 1000;
    private final long MIN_DIST = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        name = intent.getStringExtra("username");
        phonenum = intent.getStringExtra("phonenum");
        orderID = intent.getStringExtra("orderID");
        riderVehicle = intent.getStringExtra("vehicle");



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_trackmap);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        databaseReference = FirebaseDatabase.getInstance().getReference("userparcel");
        riderReference = FirebaseDatabase.getInstance().getReference("riders");
        Query checkUser = databaseReference.orderByChild("defaultUserNum");
        Query checkRider = riderReference.orderByChild("riderphone");

        back = findViewById(R.id.backbutton);


        checkRider.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> latitudes = new ArrayList<>(Collections.emptyList());
                List<String> longitudes = new ArrayList<>(Collections.emptyList());
                List<String> riderphonenum = new ArrayList<>(Collections.emptyList());

                mMap.clear();
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    if (locationSnapshot.child("riderphone").getValue().equals(phonenum))
                    {
                        String latitude = locationSnapshot.child("latitude").getValue().toString();
                        latitudes.add(latitude);
                    }
                }
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    if (locationSnapshot.child("riderphone").getValue().equals(phonenum))
                    {
                        String longitude = locationSnapshot.child("longitude").getValue().toString();
                        longitudes.add(longitude);
                    }
                }
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    if (locationSnapshot.child("riderphone").getValue().equals(phonenum))
                    {
                        String riderphone = locationSnapshot.child("riderphone").getValue().toString();
                        riderphonenum.add(riderphone);
                    }
                }
                System.out.println(latitudes.toString() + longitudes + riderphonenum);
                for (int i = 0; i < latitudes.size(); i++) {
                    LatLng latLng = new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i)));
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(riderphonenum.get(i)));
                    markerMap.put(riderphonenum.get(i), marker);
                }

                Marker marker = (Marker)markerMap.get(phonenum);
                if (marker != null) {
                    LatLng pos = marker.getPosition();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, DEFAULT_ZOOM));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(rider_takeorder_map.this, rider_ongoing_order.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("phonenum", phonenum);
                intent.putExtra("username", name);
                intent.putExtra("vehicle", riderVehicle);
                intent.putExtra("orderID", orderID);
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
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                riderReference.child(phonenum).child("latitude").setValue(location.getLatitude());
                riderReference.child(phonenum).child("longitude").setValue(location.getLongitude());

//                HashMap<Object, Object> UserInfo= new HashMap<>();
//                UserInfo.put("latitude",location.getLatitude());
//                UserInfo.put("longitude",location.getLongitude());
//                riderReference.child(phonenum).setValue(UserInfo);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }
            @Override
            public void onProviderEnabled(String s) {
            }
            @Override
            public void onProviderDisabled(String s) {
            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}


