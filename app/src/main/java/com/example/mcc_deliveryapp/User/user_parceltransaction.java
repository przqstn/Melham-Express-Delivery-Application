package com.example.mcc_deliveryapp.User;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.mcc_deliveryapp.R;
import com.example.mcc_deliveryapp.Rider.rider_dashboard;
import com.example.mcc_deliveryapp.Rider.take_order;
import com.example.mcc_deliveryapp.User.Module.DirectionFinder;
import com.example.mcc_deliveryapp.User.Module.DirectionFinderListener;
import com.example.mcc_deliveryapp.User.Module.Route;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.google.android.gms.maps.model.BitmapDescriptor;

import android.graphics.Bitmap;
import android.graphics.Canvas;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Task;

import android.location.Location;


import android.util.Log;

import com.google.android.gms.location.LocationServices;


public class user_parceltransaction extends FragmentActivity implements LocationListener, OnMapReadyCallback, DirectionFinderListener {

	GoogleMap mMap;
	private EditText etOrigin;
	private EditText etDestination;
	private List<Marker> originMarkers = new ArrayList<>();
	private List<Marker> destinationMarkers = new ArrayList<>();
	private List<Polyline> polylinePaths = new ArrayList<>();
	private ProgressDialog progressDialog;
	public LocationManager locationManager;
	public double latitude;
	public double longitude;
	public Criteria criteria;
	public String bestProvider;

	Geocoder geocoder;
	List<Address> addresses;

	Button address_dialog;
	View mapview, locationButton;
	String GOOGLE_API_KEY = "AIzaSyD5glPPR-FTV888MG4YDIXldF-SpuwR7YQ";
	EditText senderloc, sendercontact, sendername;

	EditText receiverloc, receivercontact, receivername;

	String userNumber, userName;

	private static final String TAG = user_parceltransaction.class.getSimpleName();
	private LatLng lastKnownLocation;
	private static final int DEFAULT_ZOOM = 15;
	private final LatLng defaultLocation = new LatLng(14.5928, 120.9801);
	private FusedLocationProviderClient fusedLocationClient;

	@SuppressLint("WakelockTimeout")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_parceltransaction);

		Places.initialize(this, GOOGLE_API_KEY);
		requestPermission();
		Intent intent = getIntent();
		userNumber = intent.getStringExtra("phonenum");
		userName = intent.getStringExtra("username");
		fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		assert mapFragment != null;
		mapview = Objects.requireNonNull(mapFragment).getView();
		mapFragment.getMapAsync(this);

		locationButton = ((View) mapview.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
		etOrigin = (EditText) findViewById(R.id.etOrigin);
		etDestination = (EditText) findViewById(R.id.etDestination);
		address_dialog = (Button) findViewById(R.id.img_addressbtndialog);

		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

		//prevent lock screen
		PowerManager powerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		@SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
		wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);


		etOrigin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Set the fields to specify which types of place data to
				// return after the user has made a selection.
				List<Place.Field> field = Arrays.asList(Place.Field.ID, Place.Field.ADDRESS);

				// Start the autocomplete intent.
				Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, field)
						.setCountry("PH")
						.build(user_parceltransaction.this);
				intent.putExtra("Initial Location", lastKnownLocation);
				//start activity result
				etDestination.setText("");
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
				startActivityForResult(intent, 1);


			}
		});
// this is for drop off location listener
		etDestination.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Set the fields to specify which types of place data to
				// return after the user has made a selection.
				List<Place.Field> field = Arrays.asList(Place.Field.ID, Place.Field.ADDRESS);

				// Start the autocomplete intent.
				Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, field)
						.setCountry("PH")
						.build(user_parceltransaction.this);
				//start activity result
				startActivityForResult(intent, 2);

			}
		});

		address_dialog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
						user_parceltransaction.this, R.style.BottomSheetDialogTheme
				);
				String origin = etOrigin.getText().toString();
				String destination = etDestination.getText().toString();

				if (!origin.equals("")) {
					if (!destination.equals("")) {
						View bottomSheetView = LayoutInflater.from(getApplicationContext())
								.inflate(R.layout.user_adressdetail, (LinearLayout) findViewById(R.id.Sender_addressDetailsDialog)
								);

						senderloc = bottomSheetView.findViewById(R.id.sender_edTextAddress);
						senderloc.setText(origin);
						sendercontact = bottomSheetView.findViewById(R.id.sender_edTextPhoneNumber);
						sendername = bottomSheetView.findViewById(R.id.sender_name);


						bottomSheetView.findViewById(R.id.sender_btnConfirm).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								//authentication of the sender dialog box
								if (senderloc.length() == 0) {
									sendercontact.setError("Required");
								} else if (sendercontact.length() == 0) {
									sendercontact.setError("Required");
								} else if (sendername.length() == 0) {
									sendername.setError("Required");
								} else {
									//getting and sharing the preferences of the data in the sender area
									String sender_loc = senderloc.getText().toString();
									String sender_contact = sendercontact.getText().toString();
									String sender_name = sendername.getText().toString();

									SharedPreferences.Editor editor = sharedPref.edit();
									editor.putString("key 1", sender_loc);
									editor.putString("key 2", sender_contact);
									editor.putString("key 3", sender_name);
									editor.apply();

									View bottomSheetView2 = LayoutInflater.from(getApplicationContext())
											.inflate(R.layout.user_receiver_address_detail, (LinearLayout) findViewById(R.id.Receiver_addressDetailsDialog)
											);
									receiverloc = (EditText) bottomSheetView2.findViewById(R.id.receiver_edTextAddress);
									receiverloc.setText(destination);
									receivercontact = (EditText) bottomSheetView2.findViewById(R.id.receiver_edTextPhoneNumber);
									receivername = (EditText) bottomSheetView2.findViewById(R.id.receiver_name);


									bottomSheetView2.findViewById(R.id.Receiver_btnConfirm).setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View view) {
											//authentication of the receiver dialog box
											if (receiverloc.length() == 0) {
												receiverloc.setError("Required");
											} else if (receivercontact.length() == 0) {
												receivercontact.setError("Required");
											} else if (receivername.length() == 0) {
												receivername.setError("Required");
											} else {
												//getting and sharing the preferences of the data in the receiver area
												String receiver_loc = receiverloc.getText().toString();
												String receiver_contact = receivercontact.getText().toString();
												String receiver_name = receivername.getText().toString();

												SharedPreferences.Editor editor = sharedPref.edit();
												editor.putString("key 4", receiver_loc);
												editor.putString("key 5", receiver_contact);
												editor.putString("key 6", receiver_name);
												editor.putString("key 9", userNumber);
												editor.putString("key 10", userName);
												editor.apply();
												Intent intent = new Intent(user_parceltransaction.this, user_checkrate.class);
												// release lock prevention
												if (wakeLock.isHeld())
													wakeLock.release();

												startActivity(intent);
											}
										}
									});


									bottomSheetDialog.setContentView(bottomSheetView2);
									bottomSheetDialog.show();
								}
							}

						});
						bottomSheetDialog.setContentView(bottomSheetView);
						bottomSheetDialog.show();
					}
				}
			}
		});


	}

	public void requestPermission() {
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
										Manifest.permission.ACCESS_COARSE_LOCATION, false);
							}
//					Boolean backgroundLocationGranted = null;
//					if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//						backgroundLocationGranted = result.getOrDefault(
//								Manifest.permission.ACCESS_BACKGROUND_LOCATION,false);
//					}

							if (fineLocationGranted != null && fineLocationGranted) {
								// Precise location access granted.
							} else if (coarseLocationGranted != null && coarseLocationGranted) {
								// Only approximate location access granted.
							} else {
								onBackPressed();
							}
						}
				);

		// Before you perform the actual permission request, check whether your app
		// already has the permissions, and whether your app needs to show a permission
		// rationale dialog. For more details, see Request permissions.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			locationPermissionRequest.launch(new String[]{
					Manifest.permission.ACCESS_FINE_LOCATION,
					Manifest.permission.ACCESS_COARSE_LOCATION
			});
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

			if (ActivityCompat.checkSelfPermission(this,
					Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
					&& ActivityCompat.checkSelfPermission(this,
					Manifest.permission.ACCESS_COARSE_LOCATION) !=
					PackageManager.PERMISSION_GRANTED) {
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
								lastKnownLocation = new LatLng(latitude, longitude);
								requestLoc();
							} else {
								mMap.moveCamera(CameraUpdateFactory
										.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
								mMap.getUiSettings().setMyLocationButtonEnabled(true);
								if (ActivityCompat.checkSelfPermission(user_parceltransaction.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(user_parceltransaction.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
									// TODO: Consider calling
									//    ActivityCompat#requestPermissions
									// here to request the missing permissions, and then overriding
									//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
									//                                          int[] grantResults)
									// to handle the case where the user grants the permission. See the documentation
									// for ActivityCompat#requestPermissions for more details.
									return;
								}
								locationManager.requestLocationUpdates(bestProvider, 1000, 0, user_parceltransaction.this);
							}
						}
					});
		}
		else
		{
			onBackPressed();
		}
	}


	@Override
	protected void onPause() {
		super.onPause();
		try {
			locationManager.removeUpdates(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		//Hey, a non null location! Sweet!

		//remove location callback:
		locationManager.removeUpdates(this);

		//open the map:
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		lastKnownLocation = new LatLng(latitude, longitude);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		System.out.println("stat changed");
	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	public void requestLoc() {
		if (lastKnownLocation != null) {
								mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLocation, DEFAULT_ZOOM));
								geocoder = new Geocoder(user_parceltransaction.this, Locale.getDefault());
								try {
									addresses = geocoder.getFromLocation(lastKnownLocation.latitude, lastKnownLocation.longitude, 1);
								} catch (IOException e) {
									e.printStackTrace();
								}

								String address = addresses.get(0).getAddressLine(0);
								etOrigin.setText(address);
							}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// this code is for pick up and drop off places if request code == 1 the pick up edit text area will set the first places
		// if request code == 2 this means that the edit text is for destination area.

		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				//When success initialize place
				Place place = Autocomplete.getPlaceFromIntent(data);
				//set address on edittext
				etOrigin.setText(place.getAddress());
			}
			else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
				// TODO: Handle the error.
				Status status = Autocomplete.getStatusFromIntent(data);
				//Log.i(TAG, status.getStatusMessage());
			}
			else if (resultCode == RESULT_CANCELED) {
				// The user canceled the operation.
				etOrigin.setText("");
			}
		}
		if (requestCode == 2) {
			if (resultCode == RESULT_OK) {
				//When success initialize place
				Place place2 = Autocomplete.getPlaceFromIntent(data);
				//set address on edittext
				etDestination.setText(place2.getAddress());
				sendRequest();
			} else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
				// TODO: Handle the error.
				Status status = Autocomplete.getStatusFromIntent(data);
				//Log.i(TAG, status.getStatusMessage());
			} else if (resultCode == RESULT_CANCELED) {
				// The user canceled the operation.
				etDestination.setText("");
			}

		}

	}

	public void sendRequest() {
		String origin = etOrigin.getText().toString();
		String destination = etDestination.getText().toString();
		geocoder = new Geocoder(user_parceltransaction.this, Locale.getDefault());

		if (lastKnownLocation != null) {
			try {
				addresses = geocoder.getFromLocation(
						lastKnownLocation.latitude, lastKnownLocation.longitude, 1);
			} catch (IOException e) {
				e.printStackTrace();
			}

			String address = addresses.get(0).getAddressLine(0);

			if (origin.equals(address)) {
				origin = lastKnownLocation.latitude + "," + lastKnownLocation.longitude;
			}
		}

		if (origin.isEmpty()) {
			Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (destination.isEmpty()) {
			Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
			return;
		}

		try {
			new DirectionFinder( this, origin, destination, "Driving").execute();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMapReady(@NonNull GoogleMap googleMap) {
		mMap = googleMap;

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
			layoutParams.setMargins(0, 0, 30, 400);


		}

		mMap.getUiSettings().setZoomControlsEnabled(true);

// this is for customize map
		try {
			boolean success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.customize_maps_style));
			if (!success)
				Toast.makeText(this, "Map style failed to Load", Toast.LENGTH_SHORT).show();

		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}

		LatLng ncr = new LatLng(14.672520, 121.046824);
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ncr, 20));


		// if this is for permission check if the user denied the permission there will be no map appear
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
		mMap.setMyLocationEnabled(true);
		mMap.getUiSettings().setMyLocationButtonEnabled(true);

		mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
			@Override
			public boolean onMyLocationButtonClick() {
				if (mapview != null) {
					getLocation();
//					getDeviceLocation();
					etDestination.setText("");
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
				return false;
			}
		});

		getLocation();


	}

	@Override
	public void onDirectionFinderStart() {
		progressDialog = ProgressDialog.show(this, "Please wait.",
				"Finding direction..!", true);

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

	private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
		Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
		vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
		Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		vectorDrawable.draw(canvas);
		return BitmapDescriptorFactory.fromBitmap(bitmap);
	}

	@Override
	public void onDirectionFinderSuccess(List<Route> routes) {
		progressDialog.dismiss();
		polylinePaths = new ArrayList<>();
		originMarkers = new ArrayList<>();
		destinationMarkers = new ArrayList<>();

		for (Route route : routes) {
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
			((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
			((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

			originMarkers.add(mMap.addMarker(new MarkerOptions()
					.icon(bitmapDescriptorFromVector(user_parceltransaction.this, R.drawable.pickup))
					.title(route.startAddress)
					.position(route.startLocation)

			));

			destinationMarkers.add(mMap.addMarker(new MarkerOptions()
					.icon(bitmapDescriptorFromVector(user_parceltransaction.this, R.drawable.location))
					.title(route.endAddress)
					.position(route.endLocation)));

			PolylineOptions polylineOptions = new PolylineOptions().
					geodesic(true).
					color(Color.BLUE).
					width(10);

			for (int i = 0; i < route.points.size(); i++)
				polylineOptions.add(route.points.get(i));

			polylinePaths.add(mMap.addPolyline(polylineOptions));

			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = sharedPref.edit();

			String startAdd = route.startLocation.toString().replace("lat/lng: (", "")
					.replace(")","");
			String endAdd = route.endLocation.toString().replace("lat/lng: (", "")
					.replace(")","");


			editor.putString("key 8",route.distance.text);
			editor.putString("key 11", startAdd);
			editor.putString("key 12", endAdd);
			editor.apply();

		}
	}
	@Override
	public void onBackPressed()
	{
		Intent intent = new Intent(user_parceltransaction.this, user_permission.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.putExtra("username", userName);
		intent.putExtra("phonenum", userNumber);
		startActivity(intent);
	}
}