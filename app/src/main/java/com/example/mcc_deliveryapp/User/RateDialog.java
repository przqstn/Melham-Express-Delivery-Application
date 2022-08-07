package com.example.mcc_deliveryapp.User;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.mcc_deliveryapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class RateDialog extends AppCompatDialogFragment {
    String name, phonenum, orderID, ridernum;
    float rateTotal, rateCount;
    RatingBar ratingbar;
    Button submit_rating;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        name = intent.getStringExtra("username");
        phonenum = intent.getStringExtra("phonenum");
        orderID = intent.getStringExtra("orderID");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.rate_courier_dialog, null);

        ratingbar = view.findViewById(R.id.ratingBar);
        submit_rating = view.findViewById(R.id.btn_rateCourier);
        ratingbar.setRating(5);

        submit_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println("Clicked"+name+orderID);
//                System.out.println(ratingbar.getRating());

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference dr = database.getReference().child("userparcel");
                Query query = dr.orderByChild("OrderID").equalTo(orderID);

                query.addChildEventListener(
                        new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                ridernum = dataSnapshot.child("ridernum").getValue(String.class);
                                String userdefnum = dataSnapshot.child("defaultUserNum").getValue().toString();
                                dr.child(dataSnapshot.getKey()).child("userParcelStatus").setValue("Completed"+userdefnum);

                                final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                                final DatabaseReference dr2 = database2.getReference().child("riders");
                                Query query = dr2.orderByChild("riderphone").equalTo(ridernum);

                                query.addChildEventListener(
                                        new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                                                rateTotal = dataSnapshot.child("rate_total").getValue(float.class);
                                                rateCount = dataSnapshot.child("rate_count").getValue(float.class);
                                                dr2.child(dataSnapshot.getKey()).child("rate_total").setValue(rateTotal+ratingbar.getRating());
                                                dr2.child(dataSnapshot.getKey()).child("rate_count").setValue(rateCount+1.0);
                                            }

                                            @Override
                                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

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
                Intent intent2 = new Intent(getActivity(), user_navigation.class);
                intent2.putExtra("phonenum", phonenum);
                intent2.putExtra("username", name);
                intent2.putExtra("orderID", orderID);
                startActivity(intent2);
            }
        });

        builder.setView(view);
        return builder.create();
    }
}
