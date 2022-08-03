package com.example.mcc_deliveryapp.User;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mcc_deliveryapp.R;
import com.example.mcc_deliveryapp.Rider.model;
import com.example.mcc_deliveryapp.Rider.myadapter2;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class user_home_fragment extends Fragment {
    String userPhoneNum, userName;
    TextView welcomeText;
    Button bookOrder;
    RecyclerView recyclerView_pickup;
    home_adapter home_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_home_fragment, container, false);

        recyclerView_pickup = view.findViewById(R.id.user_home_recycler);
        recyclerView_pickup.setLayoutManager(new LinearLayoutManager(getContext()));

        Intent intent = getActivity().getIntent();
        userPhoneNum = intent.getStringExtra("phonenum");
        userName = intent.getStringExtra("username");
        bookOrder = view.findViewById(R.id.bookOrder);

        welcomeText = view.findViewById(R.id.hi_user_2);

        welcomeText.setText("Hi, "+ userName);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                        .child("userparcel").orderByChild("userParcelStatus").equalTo("Ongoing"+userPhoneNum)
                                ,model.class ).build();

        home_adapter = new home_adapter(options);
        recyclerView_pickup.setAdapter(home_adapter);

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
        home_adapter.stopListening();
    }
}