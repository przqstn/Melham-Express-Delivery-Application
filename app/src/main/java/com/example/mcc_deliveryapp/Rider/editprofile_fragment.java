package com.example.mcc_deliveryapp.Rider;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mcc_deliveryapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class editprofile_fragment extends AppCompatActivity {

    private Button btnCancel, btnSaveChanges;
    private ImageButton btnUpload;
    private ImageView profilePic;
    private TextView viewphoneNum, viewname, viewvehicleType, viewplateNum, viewAddress;
    private DatabaseReference root;
    private String phoneNum, imgName, address;
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_editprofile_fragment);

        btnUpload = findViewById(R.id.btn_upload);
        btnCancel = findViewById(R.id.btn_cancelChanges);
        btnSaveChanges = findViewById(R.id.btn_saveChanges);
        viewAddress = findViewById(R.id.riderAddress);
        profilePic = findViewById(R.id.profile_user);

        Intent intent = getIntent();
        phoneNum = intent.getStringExtra("riderphone");
        viewphoneNum=findViewById(R.id.riderNumber);
        viewphoneNum.setText(phoneNum);

        String name = intent.getStringExtra("name");
        viewname=findViewById(R.id.txt_name);
        viewname.setText(name);

        String vehicleType = intent.getStringExtra("vehicletype");
        viewvehicleType=findViewById(R.id.riderVehicle);
        viewvehicleType.setText(vehicleType);

        String plateNum = intent.getStringExtra("platenumber");
        viewplateNum=findViewById(R.id.riderPlate);
        viewplateNum.setText(plateNum);

        address = intent.getStringExtra("address");


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storage=FirebaseStorage.getInstance();
                storageReference=storage.getReference();
                choosePicture();
                System.out.println("hello");
            }
        });

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                root=FirebaseDatabase.getInstance().getReference().child("riders");
                viewAddress = findViewById(R.id.riderAddress);
                HashMap hashMap = new HashMap();


                if(viewAddress.getEditableText().toString()==null||viewAddress.getEditableText().toString().equals("")){
                    hashMap.put("currentaddress", address);
                    root.child(phoneNum).updateChildren(hashMap);
                }else{
                    hashMap.put("currentaddress", viewAddress.getEditableText().toString());
                    root.child(phoneNum).updateChildren(hashMap);
                }

                if (imageUri != null) {
                    final ProgressDialog pd = new ProgressDialog(btnSaveChanges.getContext());

                    pd.setTitle("Uploading Image");
                    pd.show();

                    StorageReference riversRef = storageReference.child("rider/" + phoneNum + "/" + imgName);
                    riversRef.putFile(imageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    pd.dismiss();

                                    final Dialog dialog = new Dialog(btnSaveChanges.getContext());
                                    dialog.setContentView(R.layout.saved_dialog);
                                    dialog.setCancelable(false);
                                    Button viewProfile = dialog.findViewById(R.id.btn_viewProfile);
                                    viewProfile.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            onBackPressed();
                                        }
                                    });
                                    dialog.show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                    double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                    pd.setMessage("Percentage: " + (int) progressPercent + "%");
                                }
                            });
                } else {
                    //
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });
    }
    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri=data.getData();
            profilePic.setImageURI(imageUri);
            imgName="profile_image.jpg";

        }
    }

}
