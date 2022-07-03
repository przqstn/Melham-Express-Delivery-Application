package com.example.mcc_deliveryapp.User;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.mcc_deliveryapp.R;
import com.example.mcc_deliveryapp.User.Module.DirectionFinder;
import com.example.mcc_deliveryapp.User.Module.DirectionFinderListener;
import com.example.mcc_deliveryapp.User.Module.Route;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import androidx.core.app.ActivityCompat;

public class user_parceltransaction extends FragmentActivity implements OnMapReadyCallback ,DirectionFinderListener{

	GoogleMap mMap;
	private Button btnFindPath;
	private EditText etOrigin;
	private EditText etDestination;
	private List<Marker> originMarkers = new ArrayList<>();
	private List<Marker> destinationMarkers = new ArrayList<>();
	private List<Polyline> polylinePaths = new ArrayList<>();
	private ProgressDialog progressDialog;
	ImageView address_dialog;
	View mapview;
	String apiKey = "AIzaSyADbrHx5UL02dbtkEkDMlrBvkv-pk3JfHU";
	EditText senderloc;
	EditText sendercontact;
	EditText sendername;


	EditText receiverloc;
	EditText receivercontact;
	EditText receivername;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_parceltransaction);

		Places.initialize(this,apiKey);

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
		.findFragmentById(R.id.map);
		mapview = mapFragment.getView();
		mapFragment.getMapAsync(this);


		btnFindPath = (Button) findViewById(R.id.btnFindPath);
		etOrigin = (EditText) findViewById(R.id.etOrigin);
		etDestination =  (EditText) findViewById(R.id.etDestination);
		address_dialog = (ImageView) findViewById(R.id.img_addressbtndialog);

		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
// this is for pick up location listener
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
				//start activity result
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
						user_parceltransaction.this,R.style.BottomSheetDialogTheme
				);
					String origin = etOrigin.getText().toString();

				View bottomSheetView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.user_adressdetail, (LinearLayout)findViewById(R.id.Sender_addressDetailsDialog)
						);

				senderloc = (EditText) bottomSheetView.findViewById(R.id.sender_edTextAddress);
				senderloc.setText(origin);
				sendercontact = (EditText)bottomSheetView.findViewById(R.id.sender_edTextPhoneNumber);
				sendername = (EditText)bottomSheetView.findViewById(R.id.sender_name);




				bottomSheetView.findViewById(R.id.sender_btnConfirm).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						//authentication of the sender dialog box
						if(senderloc.length() == 0){
							sendercontact.setError("Required");
						}else if (sendercontact.length()==0){
							sendercontact.setError("Required");
						}else if (sendername.length()==0 ){
							sendername.setError("Required");
						}else{
							//getting and sharing the preferences of the data in the sender area
							String sender_loc = senderloc.getText().toString();
							String sender_contact=sendercontact.getText().toString();
							String sender_name=sendername.getText().toString();

							SharedPreferences.Editor editor = sharedPref.edit();
							editor.putString("key 1",sender_loc);
							editor.putString("key 2",sender_contact);
							editor.putString("key 3",sender_name);
							editor.commit();
						}
						String destination = etDestination.getText().toString();
						View bottomSheetView2 = LayoutInflater.from(getApplicationContext())
								.inflate(R.layout.user_receiver_address_detail, (LinearLayout)findViewById(R.id.Receiver_addressDetailsDialog)
								);
						receiverloc = (EditText) bottomSheetView2.findViewById(R.id.receiver_edTextAddress);
						receiverloc.setText(destination);
						receivercontact=(EditText)bottomSheetView2.findViewById(R.id.receiver_edTextPhoneNumber);
						receivername = (EditText)bottomSheetView2.findViewById(R.id.receiver_name);



						bottomSheetView2.findViewById(R.id.Receiver_btnConfirm).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								//authentication of the receiver dialog box
								if(receiverloc.length() == 0){
									receiverloc.setError("Required");
								}else if (receivercontact.length()==0){
									receivercontact.setError("Required");
								}else if (receivername.length()==0 ){
									receivername.setError("Required");
								}else{
									//getting and sharing the preferences of the data in the receiver area
									String receiver_loc = receiverloc.getText().toString();
									String receiver_contact=receivercontact.getText().toString();
									String receiver_name=receivername.getText().toString();

									SharedPreferences.Editor editor = sharedPref.edit();
									editor.putString("key 4",receiver_loc);
									editor.putString("key 5",receiver_contact);
									editor.putString("key 6",receiver_name);
									editor.commit();
								}

								Intent intent = new Intent(user_parceltransaction.this,user_paymentmethod.class);
								startActivity(intent);
							}
						});
						bottomSheetDialog.setContentView(bottomSheetView2);
						bottomSheetDialog.show();
//						Intent intent = new Intent(user_parceltransaction.this,user_paymentmethod.class);
//						startActivity(intent);
					}
				});
				bottomSheetDialog.setContentView(bottomSheetView);
				bottomSheetDialog.show();
			}
		});

//		btnFindPath.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				sendRequest();
//			}
//		});



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

		if (origin.isEmpty()) {
			Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (destination.isEmpty()) {
			Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
			return;
		}

		try {
			new DirectionFinder( this, origin, destination).execute();
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
			View locationButton = ((View) mapview.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
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
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.pickup))
					.title(route.startAddress)
					.position(route.startLocation)));
			destinationMarkers.add(mMap.addMarker(new MarkerOptions()
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.location))
					.title(route.endAddress)
					.position(route.endLocation)));

			PolylineOptions polylineOptions = new PolylineOptions().
					geodesic(true).
					color(Color.BLUE).
					width(10);

			for (int i = 0; i < route.points.size(); i++)
				polylineOptions.add(route.points.get(i));

			polylinePaths.add(mMap.addPolyline(polylineOptions));
		}
	}
}