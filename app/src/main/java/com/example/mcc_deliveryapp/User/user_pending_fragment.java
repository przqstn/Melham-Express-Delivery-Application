package com.example.mcc_deliveryapp.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mcc_deliveryapp.R;
import com.example.mcc_deliveryapp.Rider.completed_adapter;
import com.example.mcc_deliveryapp.Rider.model;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class user_pending_fragment extends Fragment {
	private RecyclerView recyclerView;
	private String userNum, userName;
	private ImageView emptyPending;
	private TextView emptyText;

	record_adapter2 adapter;
	DatabaseReference mbase;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
						 Bundle savedInstanceState)
	{

		View view =  inflater.inflate(R.layout.fragment_user_pending_fragment, container, false);

		System.out.println("Pending view");

		mbase = FirebaseDatabase.getInstance().getReference().child("userparcel");

		System.out.println(mbase);
		recyclerView = view.findViewById(R.id.recycler_user_pending);
		emptyPending = view.findViewById(R.id.emptyImage);
		emptyText = view.findViewById(R.id.emptyText);


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


		FirebaseRecyclerOptions<model> options
				= new FirebaseRecyclerOptions.Builder<model>()
				.setQuery(FirebaseDatabase.getInstance().getReference()
						.child("userparcel").orderByChild("userParcelStatus")
						.equalTo("Pending"+userNum), model.class)
				.build();


		adapter = new record_adapter2(options);

		adapter.getUserNum(userNum);
		adapter.getUserName(userName);
		recyclerView.setAdapter(adapter);

		query.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				for (DataSnapshot parcelSnapshot : snapshot.getChildren()) {
					if (parcelSnapshot.child("defaultUserNum").getValue().equals(userNum))
					{
						if (parcelSnapshot.child("userParcelStatus").getValue().equals("Pending"+userNum))
						{
							emptyPending.setVisibility(View.GONE);
							emptyText.setVisibility(View.GONE);
							recyclerView.setVisibility(View.VISIBLE);
						}
					}
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});

		return view;
	}


	@Override
	public void onStart()
	{
		super.onStart();
		adapter.startListening();
	}


	@Override
	public void onStop()
	{
		super.onStop();
		adapter.stopListening();
	}
}