package com.example.mcc_deliveryapp.Rider;

public class record_fetch {
    //variable to store the data
    private String OrderID;
    private String customernotes;
    private String fee;
    private String parcelstatus;
    private String receivercontact;
    private String receiverlocation;
    private String receivername;
    private String ridername;
    private String ridernum;
    private String sendercontact;
    private String senderlocation;
    private String sendername;
    private String vehicle;

    // Mandatory empty constructor
    // for use of FirebaseUI
    public record_fetch() {
    }

    // Getter and setter method
    public String getOrderID()
    {
        return OrderID;
    }

    public void setOrderID(String OrderID)
    {
        this.OrderID = OrderID;
    }

    public String getCustomernotes()
    {
        return customernotes;
    }

    public void setCustomernotes(String customernotes)
    {
        this.customernotes = customernotes;
    }

    public String getFee()
    {
        return fee;
    }

    public void setFee(String fee)
    {
        this.fee = fee;
    }

    public String getSendercontact()
    {
        return sendercontact;
    }

    public void setSendercontact(String sendercontact)
    {
        this.sendercontact = sendercontact;
    }

    public String getParcelstatus()
    {
        return parcelstatus;
    }

    public void setParcelstatus(String parcelstatus)
    {
        this.parcelstatus = parcelstatus;
    }

    public String getReceivercontact()
    {
        return receivercontact;
    }

    public void setReceivercontact(String receivercontact)
    {
        this.receivercontact = receivercontact;
    }

    public String getReceiverlocation()
    {
        return receiverlocation;
    }

    public void setReceiverlocation(String receiverlocation)
    {
        this.receiverlocation = receiverlocation;
    }

    public String getReceivername()
    {
        return receivername;
    }

    public void setReceivername(String receivername)
    {
        this.receivername = receivername;
    }

    public String getRidername()
    {
        return ridername;
    }

    public void setRidername(String ridername)
    {
        this.ridername = ridername;
    }

    public String getRidernum()
    {
        return ridernum;
    }

    public void setRidernum(String ridernum)
    {
        this.ridernum = ridernum;
    }

    public String getSenderlocation()
    {
        return senderlocation;
    }

    public void setSenderlocation(String senderlocation)
    {
        this.senderlocation = senderlocation;
    }

    public String getSendername()
    {
        return sendername;
    }

    public void setSendername(String sendername)
    {
        this.sendername = sendername;
    }

    public String getVehicle()
    {
        return vehicle;
    }

    public void setVehicle()
    {
        this.vehicle = vehicle;
    }
}

//    public String getPickupLocation()
//    {
//        return pickupLocation;
//    }
//
//    public void setPickupLocation(String pickupLocation)
//    {
//        this.pickupLocation = pickupLocation;
//    }
//
//    public String getDropOffLocation()
//    {
//        return dropOffLocation;
//    }
//
//    public void setDropOffLocation(String dropOffLocation)
//    {
//        this.dropOffLocation = dropOffLocation;
//    }
//
//    public String getRemarkRecord()
//    {
//        return remarkRecord;
//    }
//
//    public void setRemarkRecord(String remarkRecord)
//    {
//        this.remarkRecord = remarkRecord;
//    }
//
//    public String getPriceRecord()
//    {
//        return priceRecord;
//    }
//
//    public void setPriceRecord(String priceRecord)
//    {
//        this.priceRecord = priceRecord;
//    }
//
//}
