package com.example.mcc_deliveryapp.Rider;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mcc_deliveryapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class pickup_fragment extends Fragment {
	RecyclerView recyclerView_pickup;
	myadapter myadapter;
	String riderPhoneNum;
	String riderNum;
	String riderName;
	String riderVehicle;
	String receiverLoc;
	String receiverName;
	String customerNotes;
	String senderContact;
	String senderLoc;
	String senderName;
	String vehicleType;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view =  inflater.inflate(R.layout.fragment_pickup_fragment, container, false);

		recyclerView_pickup = view.findViewById(R.id.Recycleview_pickup);
		recyclerView_pickup.setLayoutManager(new LinearLayoutManager(getContext()));

		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("riders");
		//FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

		Intent intent = getActivity().getIntent();
		riderPhoneNum = intent.getStringExtra("phonenum");
		ref.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				for (DataSnapshot ds: snapshot.getChildren()) {
					if (ds.child("riderphone").getValue().equals(riderPhoneNum)){
						riderName = ds.child("name").getValue(String.class);
						riderVehicle = ds.child("vehicletype").getValue(String.class);
					}
				}
			}
			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
//		System.out.println(riderPhoneNum);
//		System.out.println(riderName+riderVehicle);

		FirebaseRecyclerOptions<model> options =
				new FirebaseRecyclerOptions.Builder<model>()
						.setQuery(FirebaseDatabase.getInstance().getReference()
								.child("userparcel").orderByChild("vehicletype").equalTo(riderVehicle),model.class ).build();

		myadapter = new myadapter(options);
		myadapter.getRiderNum(riderPhoneNum);
		recyclerView_pickup.setAdapter(myadapter);

		return view;

	}
	public void getParcelInfo(String riderNum, String receiverLoc, String receiverName,
							  String customerNotes, String senderContact, String senderLoc,
							  String senderName, String vehicleType){
		this.riderNum = riderNum;
		this.receiverLoc = receiverLoc;
		this.receiverName = receiverName;
		this.customerNotes = customerNotes;
		this.senderContact = senderContact;
		this.senderLoc = senderLoc;
		this.senderName = senderName;
		this.vehicleType = vehicleType;
		System.out.println(riderNum + receiverLoc + receiverName + customerNotes + senderContact
				+ senderLoc + senderName + vehicleType);
	}

	@Override
	public void onStart() {
		super.onStart();
		myadapter.startListening();
	}

	@Override
	public void onStop() {
		super.onStop();
		myadapter.stopListening();
	}
}