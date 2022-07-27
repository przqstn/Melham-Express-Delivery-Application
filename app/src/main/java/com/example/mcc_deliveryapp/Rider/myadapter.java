package com.example.mcc_deliveryapp.Rider;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mcc_deliveryapp.R;
import com.example.mcc_deliveryapp.User.UserHelperClass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class myadapter extends FirebaseRecyclerAdapter<model,myadapter.myviewholder> {

	public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
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
		holder.customernotes.setText("Notes:" + model.getCustomerNotes());
	}

	@NonNull
	@Override
	public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pickuplayoutdesign,parent,false);
		return new myviewholder(view);

	}

	public class myviewholder extends RecyclerView.ViewHolder{


		TextView receivercontact,receiverlocation,receivername,sendercontact,senderlocation,
				sendername, vehicletype, customernotes, btn_takeOrder;

		//Database Realtime
		FirebaseDatabase root;
		DatabaseReference DbRef;
		FirebaseAuth fAuth;

		profile_fragment profile_fragment;

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
			btn_takeOrder = (Button) itemView.findViewById(R.id.btn_takeOrder);
			btn_takeOrder.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					System.out.println(sendername.getText().toString());
//					System.out.println(profile_fragment.RiderName);
				}
			});
		}
		public class takeOrder extends AppCompatActivity {

			Button btn_takeOrder;

			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.pickuplayoutdesign);
				btn_takeOrder = (Button) findViewById(R.id.btn_takeOrder);
				btn_takeOrder.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						System.out.println("sendername");
					}
				});
			}
		}
	}
}
