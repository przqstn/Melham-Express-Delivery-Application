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


	DatabaseReference mbase;
	private TabLayout tabLayout;
	private ViewPager2 viewPager2;
	RiderFragmentAdapter rideradapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
						 Bundle savedInstanceState)
	{

		Intent intent = getActivity().getIntent();
		riderPhoneNum = intent.getStringExtra("phonenum");


		View view =  inflater.inflate(R.layout.fragment_record_fragment, container, false);
		tabLayout = view.findViewById(R.id.riderRecordTab);
		viewPager2 = view.findViewById(R.id.viewPager2);

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

		return view;
	}

}