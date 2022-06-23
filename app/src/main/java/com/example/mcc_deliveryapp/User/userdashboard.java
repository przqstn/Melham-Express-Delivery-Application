package com.example.mcc_deliveryapp.User;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mcc_deliveryapp.FetchURL;
import com.example.mcc_deliveryapp.R;
import com.example.mcc_deliveryapp.TaskLoadedCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Arrays;
import java.util.List;

public class userdashboard extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {


	GoogleMap map;

	TextView sampleText;

	MarkerOptions place1,place2;

	int PLACE_PICKER_REQUEST = 1;

	String apiKey = "AIzaSyADbrHx5UL02dbtkEkDMlrBvkv-pk3JfHU";

	Polyline currentPolyLine;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userdashboard);

		Places.initialize(this,apiKey);

		PlacesClient placesClient = Places.createClient(this);

		List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG);


		sampleText = findViewById(R.id.txtPlaceSample);
		AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
				getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

		autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));

		autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
			@Override
			public void onPlaceSelected(Place place) {
				// TODO: Get info about the selected place.

				Place myPlace = place;

				Log.i("TAG", "Longitude: " + place.getLatLng().longitude + ", Latitude: " + place.getLatLng().latitude);
				sampleText.setText("Place: " + place.getLatLng());
				place1 = new MarkerOptions().position(new LatLng(place.getLatLng().latitude,place.getLatLng().longitude));
				place2 = new MarkerOptions().position(new LatLng(14.6049335, 121.0298628));
				map.addMarker(place1);
				map.addMarker(place2);
				String url = getURL(place1.getPosition(),place2.getPosition(),"driving");
				new FetchURL(userdashboard.this).execute(url,"driving");



			}

			@Override
			public void onError(Status status) {
				// TODO: Handle the error.
				Log.i("TAG", "An error occurred: " + status);

			}
		});

		MapFragment mapFragment =  (MapFragment) getFragmentManager().findFragmentById(R.id.mapFrag);
		mapFragment.getMapAsync(this);




		Button btnGetStart = findViewById(R.id.btnGetStart);
		btnGetStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
						userdashboard.this,R.style.BottomSheetDialogTheme
				);
				View bottomSheetView = LayoutInflater.from(getApplicationContext())
						.inflate(
								R.layout.activity_userdashboard,
								(LinearLayout)findViewById(R.id.bottomSheetContainer)
						);
				bottomSheetView.findViewById(R.id.txtViewCancel).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Toast.makeText(userdashboard.this, "Order Cancelled", Toast.LENGTH_SHORT).show();
						bottomSheetDialog.dismiss();
					}
				});
				bottomSheetDialog.setContentView(bottomSheetView);
				bottomSheetDialog.show();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PLACE_PICKER_REQUEST) {
			if (resultCode == RESULT_OK) {
				Place place = Autocomplete.getPlaceFromIntent(data);
			//	Log.i("TAG", "Longitude: " + place.getLatLng().longitude + ", Latitude: " + place.getLatLng().latitude);
			//	sampleText.setText("Place: " + place.getName() + ", " + place.getId());
			/*	place1 = new MarkerOptions().position(new LatLng(place.getLatLng().latitude,place.getLatLng().longitude));
				place2 = new MarkerOptions().position(new LatLng(place.getLatLng().latitude + 10, place.getLatLng().longitude + 10));
				map.addMarker(place1);
				map.addMarker(place2);
				String url = getURL(place1.getPosition(),place2.getPosition(),"driving");

				new FetchURL(userdashboard.this).execute(url,"driving");*/
			} else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
				// TODO: Handle the error.
				Status status = Autocomplete.getStatusFromIntent(data);
				Log.i("TAG", status.getStatusMessage());
			} else if (resultCode == RESULT_CANCELED) {
				// The user canceled the operation.
			}
		}
	}

	private String getURL(LatLng origin,LatLng dest,String directionMode)
	{
		String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		String mode = "mode=" + directionMode;
		String parameters = str_origin + "&"+ str_dest + "&" + mode;

		String output = "json";

		String url = "https://maps.googleapis.com/maps/api/directions/"+ output + "?" + parameters + "&key=" + apiKey;

		return url;

	}

	@Override
	public void onMapReady(@NonNull GoogleMap googleMap) {
		map = googleMap;


	}

	@Override
	public void onTaskDone(Object... values) {
		if(currentPolyLine != null)
			currentPolyLine.remove();
		currentPolyLine = map.addPolyline((PolylineOptions) values[0]);
	}
}