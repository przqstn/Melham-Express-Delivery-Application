package com.example.mcc_deliveryapp.User;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mcc_deliveryapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class user_profile_fragment extends Fragment {
    TextView userName, userPhone;
    String phone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile_fragment, container, false);

        userName = view.findViewById(R.id.user_name);
        userPhone =  view.findViewById(R.id.user_number);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

        Intent intent = getActivity().getIntent();
        phone = intent.getStringExtra("phonenum");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    if (ds.child("userPhone").getValue().equals(phone)){
                        userName.setText(ds.child("userFullname").getValue(String.class));
                        userPhone.setText(ds.child("userPhone").getValue(String.class));
                    }

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}