package com.example.mcc_deliveryapp.Rider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.fragment.app.Fragment;

import com.example.mcc_deliveryapp.R;


public class record_fragment extends Fragment {
	private RecyclerView recyclerView;
	private String riderPhoneNum, riderName;

	record_adapter
			adapter; // Create Object of the Adapter class
	DatabaseReference mbase; // Create object of the
	// Firebase Realtime Database


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
						 Bundle savedInstanceState)
	{
		View view =  inflater.inflate(R.layout.fragment_record_fragment, container, false);

		// Create a instance of the database and get
		// its reference
		mbase = FirebaseDatabase.getInstance().getReference().child("userparcel");

		System.out.println(mbase);
		recyclerView = view.findViewById(R.id.recycler_record);

		// To display the Recycler view linearly
		recyclerView.setLayoutManager(
				new LinearLayoutManager(getContext()));

		Intent intent = getActivity().getIntent();
		riderPhoneNum = intent.getStringExtra("phonenum");
//		riderName = intent.getStringExtra("username");


		final FirebaseDatabase database = FirebaseDatabase.getInstance();
		final DatabaseReference dr = database.getReference().child("riders");
		Query query = dr.orderByChild("riderphone").equalTo(riderPhoneNum);

		query.addChildEventListener(
				new ChildEventListener() {
					@Override
					public void onChildAdded(DataSnapshot dataSnapshot, String s) {
						riderName = dataSnapshot.child("name").getValue(String.class);
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
						.child("userparcel").orderByChild("parcelstatus")
						.equalTo("Completed"+riderPhoneNum), model.class)
						.build();

		// Connecting object of required Adapter class to
		// the Adapter class itself
		adapter = new record_adapter(options);
		adapter.getUserNum(riderPhoneNum);
		adapter.getUserName(riderName);
		// Connecting Adapter class with the Recycler view*/
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