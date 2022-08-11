package com.example.mcc_deliveryapp.Rider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mcc_deliveryapp.MainActivity2;
import com.example.mcc_deliveryapp.User.MyFragmentAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mcc_deliveryapp.R;


public class record_fragment extends Fragment {
	private RecyclerView recyclerView;
	private String riderPhoneNum, riderName;

//	record_adapter adapter; // Create Object of the Adapter class
	DatabaseReference mbase; // Create object of the
	// Firebase Realtime Database
	private TabLayout tabLayout;
	private ViewPager2 viewPager2;
	RiderFragmentAdapter rideradapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
						 Bundle savedInstanceState)
	{

		Intent intent = getActivity().getIntent();
		riderPhoneNum = intent.getStringExtra("phonenum");

//		intent.putExtra("ridernum", riderPhoneNum);
//		startActivity(intent);

		View view =  inflater.inflate(R.layout.fragment_record_fragment, container, false);
		tabLayout = view.findViewById(R.id.riderRecordTab);
		viewPager2 = view.findViewById(R.id.viewPager2);

//		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		rideradapter = new RiderFragmentAdapter(getChildFragmentManager(), getLifecycle());
		viewPager2.setAdapter(rideradapter);

		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				viewPager2.setCurrentItem(tab.getPosition());
				viewPager2.setEnabled(false);
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});

		viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
			@Override
			public void onPageSelected(int position) {

				tabLayout.selectTab(tabLayout.getTabAt(position));
			}
		});
//
//
//		// Create a instance of the database and get
//		// its reference
//		mbase = FirebaseDatabase.getInstance().getReference().child("userparcel");
//
//		System.out.println(mbase);
//		recyclerView = view.findViewById(R.id.recycler_record);
//
//		// To display the Recycler view linearly
//		recyclerView.setLayoutManager(
//				new LinearLayoutManager(getContext()));
//
//		Intent intent = getActivity().getIntent();
//		riderPhoneNum = intent.getStringExtra("phonenum");
////		riderName = intent.getStringExtra("username");
//
//
//		final FirebaseDatabase database = FirebaseDatabase.getInstance();
//		final DatabaseReference dr = database.getReference().child("riders");
//		Query query = dr.orderByChild("riderphone").equalTo(riderPhoneNum);
//
//		query.addChildEventListener(
//				new ChildEventListener() {
//					@Override
//					public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//						riderName = dataSnapshot.child("name").getValue(String.class);
//					}
//
//					@Override
//					public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//					}
//
//					@Override
//					public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//					}
//
//					@Override
//					public void onCancelled(@NonNull DatabaseError error) {
//
//					}
//
//					@Override
//					public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//					}
//				});
//
//
//// It is a class provide by the FirebaseUI to make a
//		// query in the database to fetch appropriate data
//		FirebaseRecyclerOptions<model> options
//				= new FirebaseRecyclerOptions.Builder<model>()
//				.setQuery(FirebaseDatabase.getInstance().getReference()
//						.child("userparcel").orderByChild("parcelstatus")
//						.equalTo("Completed"+riderPhoneNum), model.class)
//				.build();
//
//		// Connecting object of required Adapter class to
//		// the Adapter class itself
//		adapter = new record_adapter(options);
//		adapter.getUserNum(riderPhoneNum);
//		adapter.getUserName(riderName);
//		// Connecting Adapter class with the Recycler view*/
//		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//			@Override
//			public void onTabSelected(TabLayout.Tab tab) {
//				int position = tab.getPosition();
//
//				if (position == 0)
//				{
//					recyclerView.setAdapter(adapter);
//					recyclerView.setVisibility(View.VISIBLE);
//
//				}
//				else if (position == 1)
//				{
//					recyclerView.setVisibility(View.GONE);
//				}
//
//			}
//
//			@Override
//			public void onTabUnselected(TabLayout.Tab tab) {
//			}
//
//			@Override
//			public void onTabReselected(TabLayout.Tab tab) {
//
//			}
//		});



		return view;
	}

//	// Function to tell the app to start getting
//	// data from database on starting of the activity
//	@Override
//	public void onStart()
//	{
//		super.onStart();
//		adapter.startListening();
//	}
//
//	// Function to tell the app to stop getting
//	// data from database on stopping of the activity
//	@Override
//	public void onStop()
//	{
//		super.onStop();
//		adapter.stopListening();
//	}
}