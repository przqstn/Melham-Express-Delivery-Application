package com.example.mcc_deliveryapp.User.Module;

import java.util.List;
public interface DirectionFinderListener {
// in this part this is where the calling of direction finder
	void onDirectionFinderStart();
	void onDirectionFinderSuccess(List<Route> route);
}
