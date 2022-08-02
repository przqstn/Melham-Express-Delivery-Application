package com.example.mcc_deliveryapp.User;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mcc_deliveryapp.R;

public class user_home_fragment extends Fragment {
    String userPhoneNum, userName;
    TextView welcomeText;
    Button bookOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_home_fragment, container, false);

        Intent intent = getActivity().getIntent();
        userPhoneNum = intent.getStringExtra("phonenum");
        userName = intent.getStringExtra("username");
        bookOrder = view.findViewById(R.id.bookOrder);

        welcomeText = view.findViewById(R.id.hi_user_2);

        welcomeText.setText("Hi, "+ userName);

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
}