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
    TextView welcome_name;
    StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_courier_home, container, false);

        recyclerView_pickup = view.findViewById(R.id.Recycleview_home);
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

        myadapter2 = new myadapter2(options);
        myadapter2.getRiderNum(riderPhoneNum);
        myadapter2.getRiderName(riderName);
        recyclerView_pickup.setAdapter(myadapter2);


        //retrieved courier's profile picture from firebase storage
        storageReference= FirebaseStorage.getInstance().getReference().child(riderPhoneNum+"/profile_image.jpg");
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
                            Toast.makeText(recyclerView_pickup.getContext(), "Retrieved", Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch (IOException e){
            e.printStackTrace();
        }

        return view;

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