package com.example.mcc_deliveryapp.User;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mcc_deliveryapp.MainActivity2;
import com.example.mcc_deliveryapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Objects;

public class user_profile_fragment extends Fragment {
    private TextView userName, userPhone, mainAdd, secondaryAdd;
    private String phone, imgName;
    private View view;

    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference, imageReference;

    private ImageView profile_user;

    private ImageButton btn_settings;
    private Button btnUser_Logout;

    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user_profile_fragment, container, false);
        profile_user = view.findViewById(R.id.profile_user);
        userName = view.findViewById(R.id.txt_name);
        userPhone =  view.findViewById(R.id.user_number);
        mainAdd = view.findViewById(R.id.user_primary_add);
        secondaryAdd = view.findViewById(R.id.user_secondary_address);
        btn_settings = view.findViewById(R.id.btnUser_EditProfile);
        btnUser_Logout = view.findViewById(R.id.btnUser_Logout);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions);

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
                        mainAdd.setText(ds.child("mainAdd").getValue(String.class));
                        secondaryAdd.setText(ds.child("secondaryAdd").getValue(String.class));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        imageReference = FirebaseStorage.getInstance().getReference().child("user/"+phone+"/profile_image.jpg");
        try{
            final File file= File.createTempFile("profile_image", "jpg");
            imageReference.getFile(file)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
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

        // Settings Button Click
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment settingFrag = new user_profile_settings();
                settingFrag.show(getChildFragmentManager(), "what");
            }
        });


        // TODO: Move edit profile to settings
//        btn_UsereditProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), user_editprofile_fragment.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
//                intent.putExtra("userPhone", phone);
//                intent.putExtra("userFullname", userName.getText().toString());
//                intent.putExtra("mainAdd", mainAdd.getText().toString());
//                intent.putExtra("secondaryAdd", secondaryAdd.getText().toString());
//                view.getContext().startActivity(intent);
//            }
//        });


        // TODO: Move logout to settings w/ signOut() method
        btnUser_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        return view;
    }

    void signOut(){
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                Intent intent = new Intent(view.getContext(), MainActivity2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });
    }
}