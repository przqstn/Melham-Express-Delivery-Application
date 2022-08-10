package com.example.mcc_deliveryapp.Rider;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mcc_deliveryapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class courierHomeFragment extends Fragment {
    RecyclerView recyclerView_pickup;
    myadapter2 myadapter2;
    String riderPhoneNum;
    String riderNum;
    String riderName;
    String riderVehicle;
    String orderID;
    TextView welcome_name, emptyTextCourier;
    StorageReference storageReference;
    ImageView emptyCourier;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_courier_home, container, false);
        requestPermission();
        recyclerView_pickup = view.findViewById(R.id.Recycleview_home);
        emptyCourier = view.findViewById(R.id.emptyCourier);
        emptyTextCourier = view.findViewById(R.id.emptyTextCourier);
        recyclerView_pickup.setLayoutManager(new LinearLayoutManager(getContext()));

        welcome_name =  view.findViewById(R.id.hi_user_);

        Intent intent = getActivity().getIntent();
        riderPhoneNum = intent.getStringExtra("phonenum");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference dr = database.getReference().child("riders");
        Query query = dr.orderByChild("riderphone").equalTo(riderPhoneNum);

        query.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        DatabaseReference ddf = dr.child(dataSnapshot.getKey()).child("parcelstatus");
                        riderVehicle = dataSnapshot.child("vehicletype").getValue(String.class);
                        riderName = dataSnapshot.child("name").getValue(String.class);
                        welcome_name.setText("Hi, " + riderName);
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

        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                        .child("userparcel").orderByChild("parcelstatus").equalTo("Ongoing"+riderPhoneNum)
                                ,model.class ).build();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("userparcel");
        Query checkUser = databaseReference.orderByChild("ridernum").equalTo(riderPhoneNum);

        myadapter2 = new myadapter2(options);
        myadapter2.getRiderNum(riderPhoneNum);
        myadapter2.getRiderName(riderName);
        recyclerView_pickup.setAdapter(myadapter2);

        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot parcelSnapshot : snapshot.getChildren()) {
                    if (parcelSnapshot.child("ridernum").getValue().equals(riderPhoneNum))
                    {
                        if (parcelSnapshot.child("parcelstatus").getValue().equals("Ongoing"+riderPhoneNum))
                        {
                            emptyCourier.setVisibility(View.GONE);
                            emptyTextCourier.setVisibility(View.GONE);
                            recyclerView_pickup.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //retrieved courier's profile picture from firebase storage
        storageReference= FirebaseStorage.getInstance().getReference().child("rider/"+riderPhoneNum+"/profile_image.jpg");
        try{
            final File file= File.createTempFile("profile_image", "jpg");
            storageReference.getFile(file)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Toast.makeText(profile_rider.getContext(), "Retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                            ((ImageView)view.findViewById(R.id.home_logo)).setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }catch (IOException e){
            e.printStackTrace();
        }

        return view;

    }
/*
     checkUser.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot parcelSnapshot : snapshot.getChildren()) {
                if (parcelSnapshot.child("defaultUserNum").getValue().equals(userPhoneNum))
                {
                    if (parcelSnapshot.child("userParcelStatus").getValue().equals("Ongoing"+userPhoneNum))
                    {
                        emptyHome.setVisibility(View.GONE);
                        emptyText.setVisibility(View.GONE);
                        recyclerView_pickup.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    emptyHome.setVisibility(View.VISIBLE);
                    emptyText.setVisibility(View.VISIBLE);
                    recyclerView_pickup.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    });
 */

    public void requestPermission(){
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                fineLocationGranted = result.getOrDefault(
                                        Manifest.permission.ACCESS_FINE_LOCATION, false);
                            }
                            Boolean coarseLocationGranted = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                coarseLocationGranted = result.getOrDefault(
                                        Manifest.permission.ACCESS_COARSE_LOCATION,false);
                            }
//					Boolean backgroundLocationGranted = null;
//					if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//						backgroundLocationGranted = result.getOrDefault(
//								Manifest.permission.ACCESS_BACKGROUND_LOCATION,false);
//					}

                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.

                            } else {
                                // No location access granted.
                            }
                        }
                );

// ...

// Before you perform the actual permission request, check whether your app
// already has the permissions, and whether your app needs to show a permission
// rationale dialog. For more details, see Request permissions.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            locationPermissionRequest.launch(new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
//					Manifest.permission.ACCESS_BACKGROUND_LOCATION
            });
        }
    }

    public void getParcelInfo(String rnum, String orderID, String rname){
        this.riderNum = rnum;
        this.orderID = orderID;
        this.riderName = rname;

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
    }

    @Override
    public void onStart() {
        super.onStart();
        myadapter2.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myadapter2.stopListening();
    }
}