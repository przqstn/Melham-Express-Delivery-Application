package com.example.mcc_deliveryapp.Rider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.mcc_deliveryapp.R;
import com.example.mcc_deliveryapp.User.Module.DirectionFinder;
import com.example.mcc_deliveryapp.User.Module.DirectionFinderListener;
import com.example.mcc_deliveryapp.User.Module.Route;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class rider_takeorder_map extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {
    String name, phonenum, orderID, riderVehicle, parcelState;
    View mapview, locationButton;
    private LatLng startLocation;
    private LatLng endLocation;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    List<Address> addressesRider, addressesStart, addressesEnd;
    Geocoder geocoder;
//    private int locationStatus = 0;

    public LocationManager locationManager;
    public double latitude;
    public double longitude;
    public Criteria criteria;
    public String bestProvider;
    private final LatLng defaultLocation = new LatLng(14.5928, 120.9801);
    private FusedLocationProviderClient fusedLocationClient;


    private LatLng riderLocation;
    private GoogleMap mMap;
    private DatabaseReference riderReference, databaseReference;
    private static final int DEFAULT_ZOOM = 18;
    ImageButton back;
    Button pickedComplete;

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            riderReference.child(phonenum).child("latitude").setValue(location.getLatitude());
            riderReference.child(phonenum).child("longitude").setValue(location.getLongitude());

            riderLocation = new LatLng(location.getLatitude(), location.getLongitude());

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


    @SuppressLint("WakelockTimeout")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        name = intent.getStringExtra("username");
        phonenum = intent.getStringExtra("phonenum");
        orderID = intent.getStringExtra("orderID");
        riderVehicle = intent.getStringExtra("vehicle");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        requestPermission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_trackmap);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync((OnMapReadyCallback) this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        databaseReference = FirebaseDatabase.getInstance().getReference("userparcel");
        riderReference = FirebaseDatabase.getInstance().getReference("riders");
        Query checkOrderID = databaseReference.orderByChild("OrderID").equalTo(orderID);
        Query checkRider = riderReference.orderByChild("riderphone");

        back = findViewById(R.id.backbutton);
        pickedComplete = findViewById(R.id.pickedComplete);

        //prevent lock screen
        PowerManager powerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        wakeLock.acquire();

        checkOrderID.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {

                        String[] splitStart = Objects.requireNonNull(dataSnapshot.child("startLocation").getValue(String.class)).split(",");
                        startLocation = new LatLng(Double.parseDouble(splitStart[0]), Double.parseDouble(splitStart[1]));

                        String[] endStart = Objects.requireNonNull(dataSnapshot.child("endLocation").getValue(String.class)).split(",");
                        endLocation = new LatLng(Double.parseDouble(endStart[0]), Double.parseDouble(endStart[1]));

                        parcelState = dataSnapshot.child("parcelState").getValue(String.class);

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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

                if (originMarkers != null) {
                    for (Marker marker : originMarkers) {
                        marker.remove();
                    }
                }

                if (destinationMarkers != null) {
                    for (Marker marker : destinationMarkers) {
                        marker.remove();
                    }
                }

                if (polylinePaths != null) {
                    for (Polyline polyline : polylinePaths) {
                        polyline.remove();
                    }
                }

                mMap.clear();
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    if (Objects.requireNonNull(locationSnapshot.child("riderphone").getValue()).equals(phonenum)) {
                        String latitude = Objects.requireNonNull(locationSnapshot.child("latitude").getValue()).toString();
                        latitudes.add(latitude);
                    }
                }
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    if (Objects.requireNonNull(locationSnapshot.child("riderphone").getValue()).equals(phonenum)) {
                        String longitude = Objects.requireNonNull(locationSnapshot.child("longitude").getValue()).toString();
                        longitudes.add(longitude);
                    }
                }
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    if (Objects.requireNonNull(locationSnapshot.child("riderphone").getValue()).equals(phonenum)) {
                        String riderphone = Objects.requireNonNull(locationSnapshot.child("riderphone").getValue()).toString();
                        riderphonenum.add(riderphone);
                    }
                }
                for (int i = 0; i < latitudes.size(); i++) {
                    LatLng latLng = new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));

                }

                sendRequest();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(rider_takeorder_map.this, rider_dashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("phonenum", phonenum);
                intent.putExtra("username", name);

                //release lock screen prevention
                if (wakeLock.isHeld())
                    wakeLock.release();

                startActivity(intent);

            }
        });

        pickedComplete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (parcelState.equals("pending")) {
//                    locationStatus = 1;
                    Log.e("TAG", parcelState);
                    Log.e("TAG", orderID);
                    pickedComplete.setText("Parcel Delivered");
                    parcelState = "parcelPicked";

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference dr = database.getReference().child("userparcel");
                    Query query = dr.orderByChild("OrderID").equalTo(orderID);

                    query.addChildEventListener(
                            new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    dr.child(dataSnapshot.getKey()).child("parcelState").setValue("parcelPicked");
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



                    sendRequest();
                } else if (parcelState.equals("parcelPicked")) {

//                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                    final DatabaseReference dr = database.getReference().child("userparcel");
//                    Query query = dr.orderByChild("OrderID").equalTo(orderID);
//
//                    query.addChildEventListener(
//                            new ChildEventListener() {
//                                @Override
//                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
////                                    dr.child(dataSnapshot.getKey()).child("parcelState").setValue("delivered");
//                                }
//
//                                @Override
//                                public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                                }
//
//                                @Override
//                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                }
//
//                                @Override
//                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                                }
//                            });

//                    Log.e("TAG", parcelState);
//                    parcelState = "parcelPicked";
                    Intent intent = new Intent(rider_takeorder_map.this, rider_ongoing_order.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("phonenum", phonenum);
                    intent.putExtra("username", name);
                    intent.putExtra("vehicle", riderVehicle);
                    intent.putExtra("orderID", orderID);

                    //release lock screen prevention
                    if (wakeLock.isHeld())
                        wakeLock.release();
//                    locationStatus = 0;
                    pickedComplete.setText("Parcel Picked Up");
                    startActivity(intent);
                }
            }
        });

    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onDirectionFinderStart() {

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(bitmapDescriptorFromVector(rider_takeorder_map.this, R.drawable.pickup))
                    .title(route.startAddress)
                    .position(route.startLocation)
            ));

            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(bitmapDescriptorFromVector(rider_takeorder_map.this, R.drawable.location))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            polylineOptions.add(riderLocation);
            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));


            polylinePaths.add(mMap.addPolyline(polylineOptions));


        }
    }

    public void sendRequest() {
        geocoder = new Geocoder(this, Locale.getDefault());
        if (riderLocation != null) {
            try {
                addressesRider = geocoder.getFromLocation(riderLocation.latitude, riderLocation.longitude, 1);
                addressesStart = geocoder.getFromLocation(startLocation.latitude, startLocation.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String test1 = riderLocation.latitude + "," + riderLocation.longitude;
            String test2 = startLocation.latitude + "," + startLocation.longitude;
            String test3 = endLocation.latitude + "," + endLocation.longitude;

            if (parcelState.equals("pending")) {
                pickedComplete.setText("Parcel Picked Up");
                try {
                    Log.e("TAG", riderVehicle);
                    new DirectionFinder(this, test1, test2, riderVehicle).execute();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else if ((parcelState.equals("parcelPicked"))) {
                pickedComplete.setText("Parcel Delivered");
                try {
                    new DirectionFinder(this, test1, test3, riderVehicle).execute();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    protected void getLocation() {
        if (isLocationEnabled(this)) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));

            //You can still do this if you like, you might get lucky:
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            fusedLocationClient.getLastLocation().addOnSuccessListener(
                    this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                riderReference.child(phonenum).child("latitude").setValue(latitude);
                                riderReference.child(phonenum).child("longitude").setValue(longitude);
//                                lastKnownLocation = new LatLng(latitude, longitude);
//                                requestLoc();
                            } else {
                                mMap.moveCamera(CameraUpdateFactory
                                        .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                                if (ActivityCompat.checkSelfPermission(rider_takeorder_map.this,
                                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                        && ActivityCompat.checkSelfPermission(rider_takeorder_map.this,
                                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                locationManager.requestLocationUpdates(bestProvider, 1000,
                                        0, (LocationListener) rider_takeorder_map.this);
                            }
                        }
                    });
        }
        else
        {
            mMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.customize_maps_style));

        // this is for location button position
        if (mapview != null &&
                mapview.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);


        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        try {
            long MIN_TIME = 1000;
            long MIN_DIST = 5;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        getLocation();
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
                                // No location access granted.
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

}


