package com.example.mcc_deliveryapp.Rider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mcc_deliveryapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class profile_fragment extends Fragment {
	TextView RiderName, RiderVehicle, RiderPlate, RiderAddress, RiderNumber;
	static final String Rider = "riders";
	String phone;

	private StorageReference storageReference;
	ImageView profile_rider;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view  =  inflater.inflate(R.layout.fragment_profile_fragment, container, false);

		RiderName = view.findViewById(R.id.txt_name);
		RiderVehicle =  view.findViewById(R.id.riderVehicle);
		RiderPlate =  view.findViewById(R.id.riderPlate);
		RiderAddress =  view.findViewById(R.id.riderAddress);
		RiderNumber =  view.findViewById(R.id.riderNumber);
		profile_rider = view.findViewById(R.id.profile_user);

		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("riders");
		//FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

		Intent intent = getActivity().getIntent();
		phone = intent.getStringExtra("phonenum");

		//rework to get current user id instead of using for loop
		//authenticate current user if method is implemented in the rider log in page
		ref.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				for (DataSnapshot ds: snapshot.getChildren()) {
					if (ds.child("riderphone").getValue().equals(phone)){
						RiderName.setText(ds.child("name").getValue(String.class));
						RiderVehicle.setText(ds.child("vehicletype").getValue(String.class));
						RiderPlate.setText(ds.child("vehicleplatenumber").getValue(String.class));
						RiderAddress.setText(ds.child("currentaddress").getValue(String.class));
						RiderNumber.setText(ds.child("riderphone").getValue(String.class));
					}
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}

		});

		//retrieved courier's profile picture from firebase storage
		storageReference= FirebaseStorage.getInstance().getReference().child("rider/"+phone+"/profile_image.jpg");
		try{
			final File file= File.createTempFile("profile_image", "jpg");
			storageReference.getFile(file)
					.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
						@Override
						public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
							//Toast.makeText(profile_rider.getContext(), "Retrieved", Toast.LENGTH_SHORT).show();
							Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
							((ImageView)view.findViewById(R.id.profile_user)).setImageBitmap(bitmap);
						}
					}).addOnFailureListener(new OnFailureListener() {
						@Override
						public void onFailure(@NonNull Exception e) {
							Toast.makeText(profile_rider.getContext(), "failed", Toast.LENGTH_SHORT).show();
						}
					});
		}catch (IOException e){
			e.printStackTrace();
		}


		return view;

	}


}