package com.example.mcc_deliveryapp.Rider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mcc_deliveryapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class pickup_fragment extends Fragment {
RecyclerView recyclerView_pickup;
myadapter myadapter;



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view =  inflater.inflate(R.layout.fragment_pickup_fragment, container, false);

		recyclerView_pickup = view.findViewById(R.id.Recycleview_pickup);
		recyclerView_pickup.setLayoutManager(new LinearLayoutManager(getContext()));

		FirebaseRecyclerOptions<model> options =
				new FirebaseRecyclerOptions.Builder<model>()
				.setQuery(FirebaseDatabase.getInstance().getReference().child("userparcel"),model.class )
				.build();

//		FirebaseRecyclerOptions<model> options =
//				new FirebaseRecyclerOptions.Builder<model>()
//						.setQuery(FirebaseDatabase.getInstance().getReference().child("userparcel"), model.class)
//						.build();

		myadapter = new myadapter(options);
		recyclerView_pickup.setAdapter(myadapter);

		return view;

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