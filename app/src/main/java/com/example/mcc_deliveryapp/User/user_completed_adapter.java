package com.example.mcc_deliveryapp.User;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mcc_deliveryapp.R;
import com.example.mcc_deliveryapp.Rider.model;
import com.example.mcc_deliveryapp.Rider.rider_completed_order;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class user_completed_adapter extends FirebaseRecyclerAdapter<
        model, user_completed_adapter.recordViewholder> {

    String userNum, userName;

    public user_completed_adapter(
            @NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }
    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")
    @Override
    protected void
    onBindViewHolder(@NonNull recordViewholder holder,
                     int position, @NonNull model model)
    {
        holder.receivercontact.setText(model.getReceivercontact());
        holder.receivername.setText(model.getReceivername());
        holder.receiverlocation.setText(model.getReceiverlocation());
        holder.sendercontact.setText(model.getSendercontact());
        holder.sendername.setText(model.getSendername());
        holder.senderlocation.setText(model.getSenderlocation());
        holder.vehicletype.setText(model.getVehicletype());
        holder.fee.setText("₱"+model.getFee());
        holder.orderID.setText(model.getOrderID());
        holder.ridernum.setText(model.getRidernum());
    }

    // Function to tell the class about the Card view
    // which the data will be shown
    @NonNull
    @Override
    public recordViewholder onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.completed_userlayoutdesign, parent, false);
        return new recordViewholder(view);
    }

    public String getUserNum(String userNum){
        this.userNum = userNum;
        return userNum;
    }

    public String getUserName(String userName){
        this.userName = userName;
        return userName;
    }


    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class recordViewholder
            extends RecyclerView.ViewHolder {
        TextView receivercontact,receiverlocation,receivername,sendercontact,senderlocation,
                sendername, vehicletype, customernotes,fee, orderID, ridernum;
        Button copyID;
        Context context;
        public recordViewholder(@NonNull View itemView)
        {
            super(itemView);

            receivercontact = itemView.findViewById(R.id.txt_receiver_contact);
            receiverlocation = itemView.findViewById(R.id.txt_receiver_loc);
            receivername = itemView.findViewById(R.id.txt_receiver_name);
            sendercontact = itemView.findViewById(R.id.txt_sender_contact);
            senderlocation = itemView.findViewById(R.id.txt_sender_loc);
            sendername = itemView.findViewById(R.id.txt_sender_name);
            vehicletype = itemView.findViewById(R.id.txt_vehicletype);
            fee = itemView.findViewById(R.id.priceRecord);
            orderID = itemView.findViewById(R.id.courier_record_orderID);
            ridernum = itemView.findViewById(R.id.inv_usernum);
            copyID = itemView.findViewById(R.id.copyOrderID);

            copyID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager clipboardManager = (ClipboardManager)
                            context.getSystemService(context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("nonsense_data",
                            orderID.getText().toString());
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(context, "Order ID Copied", Toast.LENGTH_SHORT).show();
                }
            });

            CardView cv = (CardView) itemView.findViewById(R.id.completed_user_record_card);

            context = itemView.getContext();

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, user_completed_order_details.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("phonenum", userNum);
                    intent.putExtra("username", userName);
                    intent.putExtra("orderID", orderID.getText().toString());
                    intent.putExtra("vehicle", vehicletype.getText().toString());
                    intent.putExtra("ridernum", ridernum.getText().toString());
                    context.startActivity(intent);
                }
            });
        }
    }
}


