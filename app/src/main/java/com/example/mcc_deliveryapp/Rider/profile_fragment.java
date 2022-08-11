package com.example.mcc_deliveryapp.Rider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mcc_deliveryapp.MainActivity2;
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
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class profile_fragment extends Fragment {
	TextView RiderName, RiderVehicle, RiderPlate, RiderAddress, RiderNumber, rating;
	static final String Rider = "riders";
	String phone;
	float total, count;

	private StorageReference storageReference;
	private ImageView profile_rider;

    private ImageButton btn_editProfile;
	private Button btnRider_Logout;

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
		btn_editProfile = view.findViewById(R.id.btnRider_EditProfile);
		btnRider_Logout = view.findViewById(R.id.btnRider_Logout);
		rating = view.findViewById(R.id.txt_ratings);

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
						if (ds.child("rate_total").getValue(float.class) != null){
							total = ds.child("rate_total").getValue(float.class);
							count = ds.child("rate_count").getValue(float.class);
							double final_rating = total/count;
							DecimalFormat df = new DecimalFormat("#.##");
							df.setRoundingMode(RoundingMode.CEILING);

							String final_rating_string = df.format(final_rating);
							System.out.println(final_rating_string);
							if (final_rating_string.equals("-NaN")){
								rating.setText("N/A");

							}
							else {
								rating.setText(final_rating_string);
							}
						}


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
							Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
							profile_rider.setImageBitmap(bitmap);
						}
					}).addOnFailureListener(new OnFailureListener() {
						@Override
						public void onFailure(@NonNull Exception e) {

						}
					});
		}catch (IOException e){
			e.printStackTrace();
		}

		btn_editProfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(view.getContext(), editprofile_fragment.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
				intent.putExtra("riderphone", phone);
				intent.putExtra("name", RiderName.getText().toString());
				intent.putExtra("vehicletype", RiderVehicle.getText().toString());
				intent.putExtra("platenumber", RiderPlate.getText().toString());
				intent.putExtra("address", RiderAddress.getText().toString());
				view.getContext().startActivity(intent);
			}
		});

		btnRider_Logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(view.getContext(), MainActivity2.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
								Intent.FLAG_ACTIVITY_CLEAR_TASK |
								Intent.FLAG_ACTIVITY_NEW_TASK);
				view.getContext().startActivity(intent);
			}
		});

		return view;

	}


}