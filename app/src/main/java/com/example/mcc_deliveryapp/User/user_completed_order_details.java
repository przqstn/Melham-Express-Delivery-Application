package com.example.mcc_deliveryapp.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mcc_deliveryapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class user_completed_order_details extends AppCompatActivity {

    String name, phonenum, orderID, riderName, riderBrandModel, riderPlateNumber, orderReceived,
            riderVehicle, senderName, senderLocation, senderContact, ridernum, orderPlaced,
            receiverName, receiverLocation, receiverContact, vehicleType, senderNote, orderPrice;
    TextView senderloc, sendername, sendercontact, receiverloc, receivername, receivercontact,
            order_id, rider_name, vehicletype, usernote, parcelprice, plate_number, order_placed,
            order_received;
    Button btn_getReceipt;

    ImageView profilePic;
    StorageReference storageReference;

    // declaring width and height
    // for our PDF file.
    int pageHeight = 1120;
    int pagewidth = 792;

    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_completed_order_details);

        Intent intent = getIntent();
        name = intent.getStringExtra("username");
        phonenum = intent.getStringExtra("phonenum");
        orderID = intent.getStringExtra("orderID");
        String rider_num = intent.getStringExtra("ridernum");

        senderloc = findViewById(R.id.sender_loc2);
        sendername = findViewById(R.id.sender_name2);
        sendercontact = findViewById(R.id.sender_contact2);
        receiverloc = findViewById(R.id.receiver_loc2);
        receivername = findViewById(R.id.receiver_name2);
        receivercontact = findViewById(R.id.receiver_contact2);
        order_id = findViewById(R.id.parcelorder_ID2);
        rider_name = findViewById(R.id.rider_name);
        vehicletype = findViewById(R.id.vehicle_details);
        usernote = findViewById(R.id.note_rider2);
        parcelprice = findViewById(R.id.txt_price2);
        plate_number = findViewById(R.id.plate_number);
        order_placed = findViewById(R.id.order_placed);
        order_received = findViewById(R.id.order_received);
        btn_getReceipt = findViewById(R.id.btn_getReceipt);

        profilePic = findViewById(R.id.user_profile_completed_order);

        storageReference= FirebaseStorage.getInstance().getReference().child("rider/"+rider_num+"/profile_image.jpg");

        try{
            final File file= File.createTempFile("profile_image", "jpg");
            storageReference.getFile(file)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                            profilePic.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }catch (IOException e){
            e.printStackTrace();
        }


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference dr = database.getReference().child("userparcel");
        Query query = dr.orderByChild("OrderID").equalTo(orderID);
        System.out.println(orderID);
        System.out.println(rider_num+"Success");

        query.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        senderName = dataSnapshot.child("sendername").getValue(String.class);
                        senderLocation = dataSnapshot.child("senderlocation").getValue(String.class);
                        senderContact = dataSnapshot.child("sendercontact").getValue(String.class);
                        receiverName = dataSnapshot.child("receivername").getValue(String.class);
                        receiverLocation = dataSnapshot.child("receiverlocation").getValue(String.class);
                        receiverContact = dataSnapshot.child("receivercontact").getValue(String.class);
//                        riderName = dataSnapshot.child("ridername").getValue(String.class);
                        vehicleType = dataSnapshot.child("vehicletype").getValue(String.class);
                        senderNote = dataSnapshot.child("customernotes").getValue(String.class);
                        orderPrice = dataSnapshot.child("fee").getValue(String.class);
                        orderPlaced = dataSnapshot.child("DatePlace").getValue(String.class);
                        orderReceived = dataSnapshot.child("dateCompleted").getValue(String.class);
                        ridernum = dataSnapshot.child("ridernum").getValue(String.class);

                        final FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                        final DatabaseReference dr2 = database2.getReference().child("riders");
                        Query query = dr2.orderByChild("riderphone").equalTo(ridernum);
                        System.out.println(phonenum);

                        query.addChildEventListener(
                                new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        riderName = dataSnapshot.child("name").getValue(String.class);
                                        riderBrandModel = dataSnapshot.child("vehiclebrandandmodel").getValue(String.class);
                                        riderPlateNumber = dataSnapshot.child("vehicleplatenumber").getValue(String.class);
                                        rider_name.setText(riderName);
                                        vehicletype.setText(vehicleType + " ("+ riderBrandModel + ")");
                                        plate_number.setText("Plate Number: "+ riderPlateNumber);
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

                        sendername.setText(senderName);
                        senderloc.setText(senderLocation);
                        sendercontact.setText(senderContact);
                        receivername.setText(receiverName);
                        receiverloc.setText(receiverLocation);
                        receivercontact.setText(receiverContact);
                        order_id.setText(orderID);
                        usernote.setText(senderNote);
                        parcelprice.setText("₱"+orderPrice);
                        order_placed.setText(orderPlaced);
                        order_received.setText(orderReceived);
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

        btn_getReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.melham_express_logo_bg_blue);
                scaledbmp = Bitmap.createScaledBitmap(bmp, 110, 110, false);

                // below code is used for
                // checking our permissions.
                if (checkPermission()) {
//                    Toast.makeText(user_completed_order_details.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    generatePDF();
                } else {
                    requestPermission();
                }
            }
        });

    }

    private void generatePDF() {
        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        Paint paint = new Paint();
        Paint title = new Paint();

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        // below line is used for setting
        // start page for our PDF file.
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        // creating a variable for canvas
        // from our page of PDF.
        Canvas canvas = myPage.getCanvas();

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        canvas.drawBitmap(scaledbmp, 56, 40, paint);

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(15);

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(this, R.color.black));

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText("Melham Express", 209, 80, title);
        canvas.drawText("\"Always Going the Extra Mile, Just for You!\"", 209, 100, title);
        canvas.drawText("Order ID: " + order_id.getText().toString(), 209, 120, title);

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        title.setColor(ContextCompat.getColor(this, R.color.black));
        title.setTextSize(15);

        // below line is used for setting
        // our text to center of PDF.
        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("THIS SERVES AS YOUR DELIVERY RECEIPT", 396, 200, title);

        title.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Completed Order Details", 100, 240, title);
        title.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Sender Details", 100, 280, title);
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        canvas.drawText("Sender Location: " + senderloc.getText().toString(), 100, 300, title);
        canvas.drawText("Sender Name: " + sendername.getText().toString(), 100, 320, title);

        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        canvas.drawText("Receiver Details", 100, 360, title);
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        canvas.drawText("Receiver Location: " + receiverloc.getText().toString(), 100, 380, title);
        canvas.drawText("Receiver Name: " + receivername.getText().toString(), 100, 400, title);

        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        canvas.drawText("Order Remarks", 100, 440, title);
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        canvas.drawText(usernote.getText().toString(), 100, 460, title);

        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        canvas.drawText("Time Details", 100, 500, title);
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        canvas.drawText("Order Placed: " + order_placed.getText().toString(), 100, 520, title);
        canvas.drawText("Order Completed: " + order_received.getText().toString(), 100, 540, title);

        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        canvas.drawText("Courier Details", 100, 580, title);
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        canvas.drawText("Courier Name: " + rider_name.getText().toString(), 100, 600, title);
        canvas.drawText("Courier Vehicle: " + vehicletype.getText().toString() + " | " + plate_number.getText().toString(), 100, 620, title);

        title.setTextAlign(Paint.Align.CENTER);
        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        canvas.drawText("Price Breakdown", 396, 700, title);
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        canvas.drawText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -", 396, 720, title);
        canvas.drawText("Base Price: " + parcelprice.getText().toString(), 396, 740, title);
        canvas.drawText("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -", 396, 760, title);
        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        canvas.drawText("Total Price: " + parcelprice.getText().toString() , 396, 780, title);

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage);

        // below line is used to set the name of
        // our PDF file and its path.
        File file = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + "Receipt.pdf");
        }
        else
        {
            file = new File(Environment.getExternalStorageDirectory() + "/" + "Receipt.pdf");
        }

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));


            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(user_completed_order_details.this, "Receipt Saved to Files", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(user_completed_order_details.this, user_navigation.class);
        intent.putExtra("phonenum", phonenum);
        intent.putExtra("username", name);
        intent.putExtra("orderID", orderID);
        startActivity(intent);
    }
}