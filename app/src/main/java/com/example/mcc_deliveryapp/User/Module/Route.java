package com.example.mcc_deliveryapp.User.Module;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Route {

	// in this part this is where the direction time duration and listing of point to point area
	public Distance distance;
	public Duration duration;
	public String endAddress;
	public LatLng endLocation;
	public String startAddress;
	public LatLng startLocation;

	public List<LatLng> points;
}
