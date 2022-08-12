package com.example.mcc_deliveryapp.Rider;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.mcc_deliveryapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.zip.Inflater;

public class editprofile_fragment extends AppCompatActivity {

    public static final int CHOOSE_PICTURE = 1;
    public static final int CAMERA_PERM_CODE = 2;
    public static final int CAMERA_REQUEST_CODE = 3;

    private ImageButton btnUpload, btnSaveChanges;
    private ImageView profilePic;
    private TextView viewphoneNum, viewname, viewvehicleType, viewplateNum, changePass;
    private EditText editAddress;
    private DatabaseReference root;
    private String phoneNum, imgName, address;
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_editprofile_fragment);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        changePass = findViewById(R.id.txt_changePW);
        btnUpload = findViewById(R.id.btn_upload);
        btnSaveChanges = findViewById(R.id.btn_saveChanges);
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
        editAddress = findViewById(R.id.riderAddress);
        editAddress.setText(address);


        editAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if(!editAddress.getEditableText().toString().equals(address)){
                    btnSaveChanges.setVisibility(View.VISIBLE);
                }else{
                    btnSaveChanges.setVisibility(View.GONE);
                }
                if(TextUtils.isEmpty(editAddress.getEditableText().toString())){
                    btnSaveChanges.setVisibility(View.GONE);
                }
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(btnUpload.getContext());
                dialog.setContentView(R.layout.upload_photo_fragment);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                dialog.setCancelable(true);
                dialog.show();
                Button btn_Upload = dialog.findViewById(R.id.btn_Upload);
                Button btn_takePhoto = dialog.findViewById(R.id.btn_takePhoto);

                btn_Upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        choosePicture();
                        dialog.dismiss();
                    }
                });

                btn_takePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        askCameraPermission();
                        dialog.dismiss();
                    }
                });
            }
        });

        btnSaveChanges.setVisibility(View.GONE);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                root = FirebaseDatabase.getInstance().getReference().child("riders");
                HashMap hashMap = new HashMap();

                if (imageUri != null) {
                    final ProgressDialog pd = new ProgressDialog(btnSaveChanges.getContext());
                    StorageReference riversRef = storageReference.child("rider/" + phoneNum + "/" + imgName);
                    riversRef.putFile(imageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {}
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                    double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                    pd.setMessage("Percentage: " + (int) progressPercent + "%");
                                }
                            });
                }

                if (!TextUtils.isEmpty(editAddress.getEditableText().toString())) {
                    hashMap.put("currentaddress", editAddress.getEditableText().toString());
                    root.child(phoneNum).updateChildren(hashMap);
                }

                final Dialog dialog = new Dialog(btnSaveChanges.getContext());
                dialog.setContentView(R.layout.saved_dialog);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                dialog.setCancelable(false);
                Button viewProfile = dialog.findViewById(R.id.btn_viewProfile);
                dialog.show();
                viewProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        finish();
                    }
                });
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(changePass.getContext(), editprofile_changePass.class);
                intent.putExtra("riderphone", phoneNum);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (TextUtils.isEmpty(editAddress.getEditableText().toString())
                || (!editAddress.getEditableText().toString().equals(address))
                || imageUri != null) {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.cancel_edit_dialog);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
            Button btnEdit = dialog.findViewById(R.id.btn_backToEdit);
            Button btnCancel = dialog.findViewById(R.id.btn_cancelAll);
            dialog.show();
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    finish();
                }
            });

        } else {
            finish();

        }

    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_PICTURE);
    }

    private void askCameraPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else{
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }else{

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHOOSE_PICTURE && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri=data.getData();
            profilePic.setImageURI(imageUri);
            if(TextUtils.isEmpty(editAddress.getEditableText().toString())){
               btnSaveChanges.setVisibility(View.GONE);
            }else{
                btnSaveChanges.setVisibility(View.VISIBLE);
            }
        }
        if(requestCode==CAMERA_REQUEST_CODE && resultCode==RESULT_OK){
            File f = new File(currentPhotoPath);
            profilePic.setImageURI(Uri.fromFile(f));
            imageUri=Uri.fromFile(f);
            btnSaveChanges.setVisibility(View.VISIBLE);
        }

        imgName="profile_image.jpg";
    }


    private File createImageFile() throws IOException{
        //Create an image file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "profile_image";
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, //prefix
                ".jpg" //suffix
               // storageDir   //directory
        );

        //Save a file: path for use with ACTION_VIEW  intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(getPackageManager())!= null){
            File photoFile = null;
            try{
                photoFile = createImageFile();

            }catch (IOException e){

            }
            if(photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

}
