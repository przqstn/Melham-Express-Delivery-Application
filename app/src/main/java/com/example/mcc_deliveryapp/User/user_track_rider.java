package com.example.mcc_deliveryapp.User;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mcc_deliveryapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class user_track_rider extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private static final int DEFAULT_ZOOM = 18;

    HashMap markerMap = new HashMap();
    Button back;
    String riderNumber, orderID, phonenum, name, riderName, riderVehicle;
    TextView riderNameUI, riderVehicleUI, riderPlateUI;
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

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("userparcel");
        DatabaseReference riderReference = FirebaseDatabase.getInstance().getReference("riders");
        Query checkUser = databaseReference.orderByChild("OrderID");
        Query checkRider = riderReference.orderByChild("riderphone");
        riderNameUI =  findViewById(R.id.RiderName);
        riderVehicleUI =  findViewById(R.id.RiderVehicle);
        riderPlateUI =  findViewById(R.id.RiderPlate);

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
                        riderVehicleUI.setText(locationSnapshot.child("vehicletype").getValue().toString());
                        riderPlateUI.setText(locationSnapshot.child("vehicleplatenumber").getValue().toString());
                        riderVehicle = (locationSnapshot.child("vehicletype").getValue().toString());
                        String latitude = locationSnapshot.child("latitude").getValue().toString();
                        String riderphone = locationSnapshot.child("riderphone").getValue().toString();
                        String longitude = locationSnapshot.child("longitude").getValue().toString();
                        latitudes.add(latitude);
                        longitudes.add(longitude);
                        riderphonenum.add(riderphone);
                        if (riderVehicle.equals("Motorcycle"))
                        {
                            for (int i = 0; i < latitudes.size(); i++) {
                                LatLng latLng = new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i)));
                                Marker marker = mMap.addMarker(new MarkerOptions()
                                        .icon(bitmapDescriptorFromVector(user_track_rider.this, R.drawable.motorcycle))
                                        .position(latLng)
                                        .title(riderphonenum.get(i)));
                                markerMap.put(riderphonenum.get(i), marker);
                            }

                        }
                        else if (riderVehicle.equals("Sedan"))
                        {
                            for (int i = 0; i < latitudes.size(); i++) {
                                LatLng latLng = new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i)));
                                Marker marker = mMap.addMarker(new MarkerOptions()
                                        .icon(bitmapDescriptorFromVector(user_track_rider.this, R.drawable.sedan))
                                        .position(latLng)
                                        .title(riderphonenum.get(i)));
                                markerMap.put(riderphonenum.get(i), marker);
                            }

                        }
                        else if (riderVehicle.equals("SUV"))
                        {
                            for (int i = 0; i < latitudes.size(); i++) {
                                LatLng latLng = new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i)));
                                Marker marker = mMap.addMarker(new MarkerOptions()
                                        .icon(bitmapDescriptorFromVector(user_track_rider.this, R.drawable.suv))
                                        .position(latLng)
                                        .title(riderphonenum.get(i)));
                                markerMap.put(riderphonenum.get(i), marker);
                            }

                        }
                        else if (riderVehicle.equals("MPV"))
                        {
                            for (int i = 0; i < latitudes.size(); i++) {
                                LatLng latLng = new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i)));
                                Marker marker = mMap.addMarker(new MarkerOptions()
                                        .icon(bitmapDescriptorFromVector(user_track_rider.this, R.drawable.mpv))
                                        .position(latLng)
                                        .title(riderphonenum.get(i)));
                                markerMap.put(riderphonenum.get(i), marker);
                            }

                        }
                        else if (riderVehicle.equals("Small Truck"))
                        {
                            for (int i = 0; i < latitudes.size(); i++) {
                                LatLng latLng = new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i)));
                                Marker marker = mMap.addMarker(new MarkerOptions()
                                        .icon(bitmapDescriptorFromVector(user_track_rider.this, R.drawable.truck))
                                        .position(latLng)
                                        .title(riderphonenum.get(i)));
                                markerMap.put(riderphonenum.get(i), marker);
                            }

                        }
                        Marker marker = (Marker) markerMap.get(riderNumber);
                        if (marker != null) {
                            LatLng pos = marker.getPosition();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, DEFAULT_ZOOM));
                        }
                    }

                }
}

            private BitmapDescriptor bitmapDescriptorFromVector(user_track_rider user_track_rider, int vectorResId) {
                Drawable vectorDrawable = ContextCompat.getDrawable(user_track_rider, vectorResId);
                vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
                Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                vectorDrawable.draw(canvas);
                return BitmapDescriptorFactory.fromBitmap(bitmap);
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

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.customize_maps_style));

    }

}

