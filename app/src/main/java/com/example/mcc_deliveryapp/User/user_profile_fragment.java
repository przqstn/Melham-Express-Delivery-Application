package com.example.mcc_deliveryapp.User;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mcc_deliveryapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class user_profile_fragment extends Fragment {
    TextView userName, userPhone;
    String phone, imgName;
    View view;
    //
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference storageReference, sr;

    ImageView profile_user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile_fragment, container, false);
        profile_user = view.findViewById(R.id.profile_user);
        userName = view.findViewById(R.id.user_name);
        userPhone =  view.findViewById(R.id.user_number);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

        Intent intent = getActivity().getIntent();
        phone = intent.getStringExtra("phonenum");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    if (ds.child("userPhone").getValue().equals(phone)){
                        userName.setText(ds.child("userFullname").getValue(String.class));
                        userPhone.setText(ds.child("userPhone").getValue(String.class));
                    }

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }



        });

        profile_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        //retrieved courier's profile picture from firebase storage
        sr = FirebaseStorage.getInstance().getReference().child("user/"+phone+"/profile_image.jpg");
        try{
            final File file= File.createTempFile("profile_image", "jpg");
            sr.getFile(file)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Toast.makeText(profile_rider.getContext(), "Retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                            ((ImageView)view.findViewById(R.id.profile_user)).setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }catch (IOException e){
            e.printStackTrace();
        }

        return view;
    }
    //upload picture for user profile
    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri=data.getData();
            profile_user.setImageURI(imageUri);
            imgName="profile_image.jpg";
            uploadPicture();
        }
    }
    //upload method
    private void uploadPicture() {
        final ProgressDialog pd= new ProgressDialog(view.getContext());
        pd.setTitle("Uploading Image");
        pd.show();

        StorageReference riversRef=storageReference.child("user/"+phone+"/"+imgName);
        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Percentage: " + (int) progressPercent +"%");
                    }
                });


    }
}