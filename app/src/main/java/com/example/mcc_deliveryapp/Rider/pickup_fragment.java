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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class pickup_fragment extends Fragment {
	RecyclerView recyclerView_pickup;
	myadapter myadapter;
	String riderPhoneNum;
	String riderNum;
	String riderName;
	String riderVehicle;
	String orderID;
	private DatabaseReference mDatabase;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view =  inflater.inflate(R.layout.fragment_pickup_fragment, container, false);
		// ...
		mDatabase = FirebaseDatabase.getInstance().getReference();

		recyclerView_pickup = view.findViewById(R.id.Recycleview_pickup);
		recyclerView_pickup.setLayoutManager(new LinearLayoutManager(getContext()));

		Intent intent = getActivity().getIntent();
		riderPhoneNum = intent.getStringExtra("phonenum");
		riderVehicle = intent.getStringExtra("vehicle");
		riderName = intent.getStringExtra("name");
		System.out.println(riderVehicle + "aaaaaaaaaa");

		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference dr = database.getReference().child("riders");
		Query query = dr.orderByChild("riderphone").equalTo(riderPhoneNum);


		query.addChildEventListener(
				new ChildEventListener() {
					@Override
					public void onChildAdded(DataSnapshot dataSnapshot, String s) {
						DatabaseReference ddf = dr.child(dataSnapshot.getKey()).child("parcelstatus");
//						riderVehicle = dataSnapshot.child("vehicletype").getValue(String.class);
//						riderName = dataSnapshot.child("name").getValue(String.class);
//						System.out.println("Vehicle: " + riderVehicle);
//						System.out.println("Name of Rider: " + riderName);
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

		System.out.println(riderName + "--" + riderVehicle);

		FirebaseRecyclerOptions<model> options =
				new FirebaseRecyclerOptions.Builder<model>()
						.setQuery(FirebaseDatabase.getInstance().getReference()
										.child("userparcel").orderByChild("parcelstatus").equalTo("Pending"+riderVehicle)
//								.child("userparcel").orderByChild("parcelstatus").equalTo("Pending")
								,model.class ).build();

		myadapter = new myadapter(options);
		myadapter.getRiderNum(riderPhoneNum);
		myadapter.getRiderName(riderName);
		recyclerView_pickup.setAdapter(myadapter);

		return view;

	}

	public void getParcelInfo(String rnum, String orderID, String rname){
		this.riderNum = rnum;
		this.orderID = orderID;
		this.riderName = rname;
		System.out.println(rnum + "  " + orderID + " " + rname);

		final FirebaseDatabase database = FirebaseDatabase.getInstance();
		final DatabaseReference dr = database.getReference().child("userparcel");
		Query query = dr.orderByChild("OrderID").equalTo(orderID);

		query.addChildEventListener(
				new ChildEventListener() {
					@Override
					public void onChildAdded(DataSnapshot dataSnapshot, String s) {
						dr.child(dataSnapshot.getKey()).child("parcelstatus").setValue("Ongoing"+riderNum);
						dr.child(dataSnapshot.getKey()).child("ridernum").setValue(riderNum);
						dr.child(dataSnapshot.getKey()).child("ridername").setValue(riderName);
						String userdefnum = dataSnapshot.child("defaultUserNum").getValue().toString();
						dr.child(dataSnapshot.getKey()).child("userParcelStatus").setValue("Ongoing"+userdefnum);
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

//					@Override
//					public void onCancelled(FirebaseError firebaseError) {
//					}

					@Override
					public void onChildChanged(DataSnapshot dataSnapshot, String s) {
					}
				});
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