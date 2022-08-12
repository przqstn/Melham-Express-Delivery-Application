package com.example.mcc_deliveryapp.User;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mcc_deliveryapp.R;
import com.example.mcc_deliveryapp.Rider.rider_dashboard;
import com.example.mcc_deliveryapp.Rider.rider_takeorder_map;
import com.example.mcc_deliveryapp.Rider.take_order;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

public class user_track_rider extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {
    private GoogleMap mMap;
    private static final int DEFAULT_ZOOM = 18;
    View mapview, locationButton;
    String userLatitude, userLongitude, riderLatitude, riderLongitude;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    private Location lastKnownLocation;
    private final LatLng defaultLocation = new LatLng(15.594197, 120.970414);
    private FusedLocationProviderClient fusedLocationProviderClient;
    private ImageView profilePic, riderVehicleIcon; // line 51 added ImageView variable
    private StorageReference storageReference; //line 52 added StorageReference
    HashMap markerMap = new HashMap();
    Button back, btn_message_courier,btn_call_courier ;
    String riderNumber, orderID, phonenum, name, riderName, riderVehicle, riderNum;
    TextView riderNameUI, riderVehicleUI, riderPlateUI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        orderID = intent.getStringExtra("orderID");
        name = intent.getStringExtra("username");
        phonenum = intent.getStringExtra("phonenum");
        riderName = intent.getStringExtra("ridername");
        riderNum = intent.getStringExtra("ridernum");
        requestPermission();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_track_rider);
        btn_message_courier = findViewById(R.id.message_courier);
        btn_call_courier = findViewById(R.id.call_courier);
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


        if (isLocationEnabled(this)) {
            checkUser.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot parcelSnapshot : snapshot.getChildren()) {
                        if (parcelSnapshot.child("OrderID").getValue().equals(orderID))
                        {
                            profilePic = findViewById(R.id.user_profile_ongoing);

                            riderNumber = parcelSnapshot.child("ridernum").getValue(String.class);
                            storageReference= FirebaseStorage.getInstance().getReference().child("rider/"+riderNumber+"/profile_image.jpg");
                            try{
                                final File file= File.createTempFile("profile_image", "jpg");
                                storageReference.getFile(file)
                                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                                                profilePic.setImageBitmap(bitmap);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                            }catch (IOException e){
                                e.printStackTrace();
                            }


                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            btn_message_courier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String textnum = riderNumber;
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setType("vnd.android-dir/mms-sms");
                    intent.setData(Uri.parse("sms:" + textnum));
                    startActivity(intent);
                }
            });


            btn_call_courier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Calling");
                    String callnum = riderNumber;
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + callnum));
                    startActivity(intent);
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
                            String riderBrandModel = locationSnapshot.child("vehiclebrandandmodel").getValue(String.class);
                            riderPlateUI.setText(locationSnapshot.child("vehicleplatenumber").getValue().toString());
                            riderVehicle = (locationSnapshot.child("vehicletype").getValue().toString());
                            riderLatitude = locationSnapshot.child("latitude").getValue().toString();
                            String riderphone = locationSnapshot.child("riderphone").getValue().toString();
                            riderLongitude = locationSnapshot.child("longitude").getValue().toString();
                            riderVehicleIcon = findViewById(R.id.riderVehicleIcon);
                            riderVehicleUI.setText(riderVehicle + " ("+ riderBrandModel + ")");

                            latitudes.add(riderLatitude);
                            longitudes.add(riderLongitude);
                            riderphonenum.add(riderphone);
                            switch (riderVehicle) {
                                case "Motorcycle":
                                    for (int i = 0; i < latitudes.size(); i++) {
                                        LatLng latLng = new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i)));
                                        Marker marker = mMap.addMarker(new MarkerOptions()
                                                .icon(bitmapDescriptorFromVector(user_track_rider.this, R.drawable.motorcycle))
                                                .position(latLng)
                                                .title(riderphonenum.get(i)));
                                        markerMap.put(riderphonenum.get(i), marker);

                                        riderVehicleIcon.setImageDrawable(ContextCompat.getDrawable(user_track_rider.this, R.drawable.motorcycle));
                                    }

                                    break;
                                case "Sedan":
                                    for (int i = 0; i < latitudes.size(); i++) {
                                        LatLng latLng = new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i)));
                                        Marker marker = mMap.addMarker(new MarkerOptions()
                                                .icon(bitmapDescriptorFromVector(user_track_rider.this, R.drawable.sedan))
                                                .position(latLng)
                                                .title(riderphonenum.get(i)));
                                        markerMap.put(riderphonenum.get(i), marker);
                                        riderVehicleIcon.setImageDrawable(ContextCompat.getDrawable(user_track_rider.this, R.drawable.sedan));

                                    }

                                    break;
                                case "SUV":
                                    for (int i = 0; i < latitudes.size(); i++) {
                                        LatLng latLng = new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i)));
                                        Marker marker = mMap.addMarker(new MarkerOptions()
                                                .icon(bitmapDescriptorFromVector(user_track_rider.this, R.drawable.suv))
                                                .position(latLng)
                                                .title(riderphonenum.get(i)));
                                        markerMap.put(riderphonenum.get(i), marker);
                                        riderVehicleIcon.setImageDrawable(ContextCompat.getDrawable(user_track_rider.this, R.drawable.suv));

                                    }

                                    break;
                                case "MPV":
                                    for (int i = 0; i < latitudes.size(); i++) {
                                        LatLng latLng = new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i)));
                                        Marker marker = mMap.addMarker(new MarkerOptions()
                                                .icon(bitmapDescriptorFromVector(user_track_rider.this, R.drawable.mpv))
                                                .position(latLng)
                                                .title(riderphonenum.get(i)));
                                        markerMap.put(riderphonenum.get(i), marker);
                                        riderVehicleIcon.setImageDrawable(ContextCompat.getDrawable(user_track_rider.this, R.drawable.mpv));

                                    }

                                    break;
                                case "Small Truck":
                                    for (int i = 0; i < latitudes.size(); i++) {
                                        LatLng latLng = new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i)));
                                        Marker marker = mMap.addMarker(new MarkerOptions()
                                                .icon(bitmapDescriptorFromVector(user_track_rider.this, R.drawable.truck))
                                                .position(latLng)
                                                .title(riderphonenum.get(i)));
                                        markerMap.put(riderphonenum.get(i), marker);
                                        riderVehicleIcon.setImageDrawable(ContextCompat.getDrawable(user_track_rider.this, R.drawable.truck));

                                    }

                                    break;
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


        }
        else {
            onBackPressed();
            Toast.makeText(getBaseContext(), "Please turn of Location Service and try again.", Toast.LENGTH_SHORT).show();
        }
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
                                Toast.makeText(getBaseContext(), "Please turn of Location Service and try again.", Toast.LENGTH_SHORT).show();
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

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();

                            if (lastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(true);

                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.customize_maps_style));
        getLocationPermission();
        getDeviceLocation();
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

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                userLatitude = String.valueOf(location.getLatitude());
                userLongitude = String.valueOf(location.getLongitude());
                sendRequest();
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
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));

            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(bitmapDescriptorFromVector(user_track_rider.this, R.drawable.location))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

//            polylineOptions.add(riderLocation);
            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));


            polylinePaths.add(mMap.addPolyline(polylineOptions));



        }
    }

        public static boolean isLocationEnabled(Context context) {
            int locationMode = 0;
            String locationProviders;
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            {
                try
                {
                    locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
                return locationMode != Settings.Secure.LOCATION_MODE_OFF;
            }
            else
            {
                locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                return !TextUtils.isEmpty(locationProviders);
            }
        }

    public void sendRequest() {
        if (userLatitude != null) {
            String test1 = userLatitude + "," + userLongitude;
            String test2 = riderLatitude + "," + riderLongitude;
            System.out.println(test1 + test2);

            try {
                new DirectionFinder(this, test2, test1, riderVehicle.toLowerCase()).execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(user_track_rider.this, user_ongoing_order_details.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("phonenum", phonenum);
        intent.putExtra("username", name);
        intent.putExtra("name", name);
        intent.putExtra("orderID",  orderID);
        intent.putExtra("ridernum", riderNum);
        startActivity(intent);
    }

}

