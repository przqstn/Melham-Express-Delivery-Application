package com.example.mcc_deliveryapp.User;

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

public class ConfirmCancelDialog extends AppCompatDialogFragment {
    String name, phonenum, orderID;
    Button cancel_order, cancel_cancel_order;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        name = intent.getStringExtra("username");
        phonenum = intent.getStringExtra("phonenum");
        orderID = intent.getStringExtra("orderID");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.user_cancel_dialog, null);

        cancel_order = view.findViewById(R.id.btn_cancelOrderUser);
//        cancel_cancel_order = view.findViewById(R.id.btn_cancelOrderCancelUser);

        cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference dr = database.getReference().child("userparcel");
                Query query = dr.orderByChild("OrderID").equalTo(orderID);

                query.addChildEventListener(
                        new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                dr.child(dataSnapshot.getKey()).child("parcelstatus").setValue("Cancelled"+phonenum);
                                String userdefnum = dataSnapshot.child("defaultUserNum").getValue().toString();
                                dr.child(dataSnapshot.getKey()).child("userParcelStatus").setValue("Cancelled"+userdefnum);
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
                Intent intent = new Intent(getActivity(), user_cancelled_order_details.class);
                intent.putExtra("phonenum", phonenum);
                intent.putExtra("username", name);
                intent.putExtra("orderID", orderID);
                startActivity(intent);
            }
        });

//        cancel_order.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss();
//            }
//        });

        builder.setView(view);
        return builder.create();
    }
}
