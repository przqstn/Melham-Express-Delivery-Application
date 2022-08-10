package com.example.mcc_deliveryapp.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mcc_deliveryapp.R;
import com.example.mcc_deliveryapp.Rider.model;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class user_home_fragment extends Fragment {
    String userPhoneNum, userName;
    TextView welcomeText, emptyText;
    Button bookOrder;
    RecyclerView recyclerView_pickup;
    home_adapter home_adapter;
    StorageReference storageReference;
    ImageView emptyHome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_home_fragment, container, false);

        recyclerView_pickup = view.findViewById(R.id.user_home_recycler);
        recyclerView_pickup.setLayoutManager(new LinearLayoutManager(getContext()));

        Intent intent = getActivity().getIntent();
        userPhoneNum = intent.getStringExtra("phonenum");
        userName = intent.getStringExtra("username");
        bookOrder = view.findViewById(R.id.bookOrder);

        welcomeText = view.findViewById(R.id.hi_user_2);
        emptyHome = view.findViewById(R.id.emptyImage);
        emptyText = view.findViewById(R.id.emptyText);
        welcomeText.setText("Hi, "+ userName);

        // These lines of code calls the database

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                        .child("userparcel").orderByChild("userParcelStatus").equalTo("Ongoing"+userPhoneNum)
                                ,model.class ).build();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("userparcel");
        Query checkUser = databaseReference.orderByChild("defaultUserNum");

        home_adapter = new home_adapter(options);
        home_adapter.getUserNum(userPhoneNum);
        home_adapter.getUserName(userName);
        recyclerView_pickup.setAdapter(home_adapter);

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
//                    else
//                    {
//                        emptyHome.setVisibility(View.VISIBLE);
//                        emptyText.setVisibility(View.VISIBLE);
//                        recyclerView_pickup.setVisibility(View.GONE);
//                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bookOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),user_parceltransaction.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("phonenum", userPhoneNum);
                intent.putExtra("username", userName);
                startActivity(intent);
            }
        });

        //retrieved courier's profile picture from firebase storage
        storageReference= FirebaseStorage.getInstance().getReference().child("user/"+userPhoneNum+"/profile_image.jpg");
        try{
            final File file= File.createTempFile("profile_image", "jpg");
            storageReference.getFile(file)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                            ((ImageView)view.findViewById(R.id.home_logo2)).setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        home_adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
//        home_adapter.stopListening();
    }
}