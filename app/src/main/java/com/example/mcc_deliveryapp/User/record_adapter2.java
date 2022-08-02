package com.example.mcc_deliveryapp.User;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mcc_deliveryapp.R;
import com.example.mcc_deliveryapp.Rider.model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class record_adapter2 extends FirebaseRecyclerAdapter<
        model, record_adapter2.recordViewholder> {

    public record_adapter2(
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


//        holder.OrderID.setText(model.getOrderID());
        holder.customernotes.setText(model.getCustomerNotes());
        holder.fee.setText(model.getFee());
//        holder.parcelstatus.setText(model.getParcelstatus());
//        holder.receivercontact.setText(model.getReceivercontact());
        holder.receiverlocation.setText(model.getReceiverlocation());
//        holder.receivername.setText(model.getReceivername());
//        holder.ridername.setText(model.getRidername());
//        holder.ridernum.setText(model.getRidernum());
//        holder.sendercontact.setText(model.getSendercontact());
        holder.senderlocation.setText(model.getSenderlocation());
//        holder.sendername.setText(model.getSendername());
        holder.vehicle.setText(model.getVehicletype());
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
                .inflate(R.layout.recordlayoutdesign, parent, false);
        return new recordViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class recordViewholder
            extends RecyclerView.ViewHolder {
        TextView senderlocation, receiverlocation, customernotes, fee, vehicle ;
        public recordViewholder(@NonNull View itemView)
        {
            super(itemView);

            senderlocation = itemView.findViewById(R.id.pickupLocation);
            receiverlocation = itemView.findViewById(R.id.dropOffLocation);
            customernotes = itemView.findViewById(R.id.remarkRecord);
            vehicle = itemView.findViewById(R.id.vehicleRecord);
            fee = itemView.findViewById(R.id.priceRecord);
        }
    }
}


