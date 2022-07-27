package com.example.mcc_deliveryapp.Rider;

public class model {

	String receivercontact,receiverlocation,receivername,sendercontact,senderlocation,
			vehicletype, sendername, customernotes;

	public model() {
	}

	public model(String receivercontact, String receiverlocation, String receivername,
				 String sendercontact, String senderlocation, String sendername,
				 String vehicletype, String customernotes) {
		this.receivercontact = receivercontact;
		this.receiverlocation = receiverlocation;
		this.receivername = receivername;
		this.sendercontact = sendercontact;
		this.senderlocation = senderlocation;
		this.sendername = sendername;
		this.vehicletype = vehicletype;
		this.customernotes = customernotes;
	}

	public String getVehicletype() {return vehicletype;}

	public String getCustomerNotes() {
		return customernotes;
	}

	public String getReceivercontact() {
		return receivercontact;
	}

	public String getReceiverlocation() {
		return receiverlocation;
	}

	public String getReceivername() {
		return receivername;
	}

	public String getSendercontact() {
		return sendercontact;
	}

	public String getSenderlocation() {
		return senderlocation;
	}

	public String getSendername() {
		return sendername;
	}

	public void setVehicletype(String vehicletype) {this.vehicletype = vehicletype;}

	public void setCustomerNotes(String customernotes) {
		this.customernotes = customernotes;
	}

	public void setReceivercontact(String receivercontact) {
		this.receivercontact = receivercontact;
	}

	public void setReceiverlocation(String receiverlocation) {
		this.receiverlocation = receiverlocation;
	}

	public void setReceivername(String receivername) {
		this.receivername = receivername;
	}

	public void setSendercontact(String sendercontact) {
		this.sendercontact = sendercontact;
	}

	public void setSenderlocation(String senderlocation) {
		this.senderlocation = senderlocation;
	}

	public void setSendername(String sendername) {
		this.sendername = sendername;
	}
}
