package com.example.mcc_deliveryapp.Rider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mcc_deliveryapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class myadapter2 extends FirebaseRecyclerAdapter<model, myadapter2.myviewholder> {

	String riderNum;
	String riderName;

	public myadapter2(@NonNull FirebaseRecyclerOptions<model> options) {
		super(Objects.requireNonNull(options));
	}

	@Override
	public void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull model model) {
		holder.receivercontact.setText(model.getReceivercontact());
		holder.receivername.setText(model.getReceivername());
		holder.receiverlocation.setText(model.getReceiverlocation());
		holder.sendercontact.setText(model.getSendercontact());
		holder.sendername.setText(model.getSendername());
		holder.senderlocation.setText(model.getSenderlocation());
		holder.vehicletype.setText(model.getVehicletype());
		holder.fee.setText(model.getFee());
		holder.customernotes.setText("Notes:" + model.getCustomerNotes());
	}

	@NonNull
	@Override
	public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ongoing_courierlayoutdesign,parent,false);
		return new myviewholder(view);

	}

	public String getRiderNum(String riderNum){
		this.riderNum = riderNum;
		return riderNum;
	}

	public String getRiderName(String riderName){
		this.riderName = riderName;
		return riderName;
	}

	public class myviewholder extends RecyclerView.ViewHolder {


		TextView receivercontact,receiverlocation,receivername,sendercontact,senderlocation,
				sendername, vehicletype, customernotes, fee;

		//Database Realtime
		FirebaseDatabase root;
		DatabaseReference DbRef;
		FirebaseAuth mAuth;
		pickup_fragment pickup_fragment;

		public myviewholder(@NonNull View itemView) {
			super(itemView);
			receivercontact = itemView.findViewById(R.id.txt_receiver_contact);
			receiverlocation = itemView.findViewById(R.id.txt_receiver_loc);
			receivername = itemView.findViewById(R.id.txt_receiver_name);
			customernotes = itemView.findViewById(R.id.txt_note);
			sendercontact = itemView.findViewById(R.id.txt_sender_contact);
			senderlocation = itemView.findViewById(R.id.txt_sender_loc);
			sendername = itemView.findViewById(R.id.txt_sender_name);
			vehicletype = itemView.findViewById(R.id.txt_vehicletype);
			fee = itemView.findViewById(R.id.txt_price);
		}
	}
}

