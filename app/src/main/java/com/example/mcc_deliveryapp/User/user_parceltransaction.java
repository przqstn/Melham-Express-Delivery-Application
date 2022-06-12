package com.example.mcc_deliveryapp.User;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.mcc_deliveryapp.R;
import com.example.mcc_deliveryapp.User.Module.DirectionFinder;
import com.example.mcc_deliveryapp.User.Module.DirectionFinderListener;
import com.example.mcc_deliveryapp.User.Module.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_parceltransaction);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
		.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		btnFindPath = (Button) findViewById(R.id.btnFindPath);
		etOrigin = (EditText) findViewById(R.id.etOrigin);
		etDestination = (EditText) findViewById(R.id.etDestination);
		address_dialog = (ImageView) findViewById(R.id.img_addressbtndialog);

		address_dialog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
						user_parceltransaction.this,R.style.BottomSheetDialogTheme
				);
				View bottomSheetView = LayoutInflater.from(getApplicationContext())
						.inflate(
								R.layout.user_adressdetail,
								(LinearLayout)findViewById(R.id.addressDetailsDialog)
						);
				bottomSheetView.findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(user_parceltransaction.this,user_paymentmethod.class);
						startActivity(intent);
					}
				});
				bottomSheetDialog.setContentView(bottomSheetView);
				bottomSheetDialog.show();
			}
		});


		btnFindPath.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendRequest();
			}
		});

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

		LatLng ncr = new LatLng(14.672520, 121.046824);
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ncr, 18));
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		/*mMap.addMarker(new MarkerOptions()
				.position(ncr)
				.title("National Capital Region")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.pushpin))
		);*/

//		LatLng ncr2 = new LatLng(14.672520, 121.046824);
//
//		mMap.addPolyline(new PolylineOptions()
//				.add(ncr,
//				new LatLng(14.672520, 121.046824),
//						new LatLng(14.673049, 121.043573),
//						ncr2
//				)
//				.width(10)
//				.color(Color.RED)
//		);

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