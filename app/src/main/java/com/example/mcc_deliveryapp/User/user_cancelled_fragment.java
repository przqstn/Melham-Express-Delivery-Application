package com.example.mcc_deliveryapp.User;

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
import com.example.mcc_deliveryapp.Rider.model;
import com.example.mcc_deliveryapp.Rider.record_adapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class user_cancelled_fragment extends Fragment {
	private RecyclerView recyclerView;
	private String userNum, userName;

	user_cancelled_adapter adapter; // Create Object of the Adapter class
	DatabaseReference mbase; // Create object of the

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
						 Bundle savedInstanceState)
	{

		View view =  inflater.inflate(R.layout.fragment_user_cancelled_fragment, container, false);

		System.out.println("Cancelled view");
		// Create a instance of the database and get
		// its reference
		mbase = FirebaseDatabase.getInstance().getReference().child("userparcel");

		System.out.println(mbase);
		recyclerView = view.findViewById(R.id.recycler_user_cancelled);

		// To display the Recycler view linearly
		recyclerView.setLayoutManager(
				new LinearLayoutManager(getContext()));

		Intent intent = getActivity().getIntent();
		userNum = intent.getStringExtra("phonenum");
		userName = intent.getStringExtra("username");


		Query query = mbase.orderByChild("defaultUserNum").equalTo(userNum);

		query.addChildEventListener(
				new ChildEventListener() {
					@Override
					public void onChildAdded(DataSnapshot dataSnapshot, String s) {
						DatabaseReference ddf = mbase.child(dataSnapshot.getKey()).child("parcelstatus");
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

		// It is a class provide by the FirebaseUI to make a
		// query in the database to fetch appropriate data
		FirebaseRecyclerOptions<model> options
				= new FirebaseRecyclerOptions.Builder<model>()
				.setQuery(FirebaseDatabase.getInstance().getReference()
						.child("userparcel").orderByChild("userParcelStatus")
						.equalTo("Cancelled"+userNum), model.class)
				.build();

		// Connecting object of required Adapter class to
		// the Adapter class itself
		adapter = new user_cancelled_adapter(options);
		// Connecting Adapter class with the Recycler view*/
		adapter.getUserNum(userNum);
		adapter.getUserName(userName);
		recyclerView.setAdapter(adapter);

		return view;
	}

	// Function to tell the app to start getting
	// data from database on starting of the activity
	@Override
	public void onStart()
	{
		super.onStart();
		adapter.startListening();
	}

	// Function to tell the app to stop getting
	// data from database on stopping of the activity
	@Override
	public void onStop()
	{
		super.onStop();
		adapter.stopListening();
	}
}