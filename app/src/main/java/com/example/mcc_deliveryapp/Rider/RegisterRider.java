package com.example.mcc_deliveryapp.Rider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mcc_deliveryapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RegisterRider extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference rootie;
    private DatePickerDialog datePickerDialog;
    private Dialog regRiderStep1, regRiderStep2, regRiderStep3, regRiderStep4, regRiderStep5;
    private Boolean hasError = false;
    private String verificationCodeBySystem, vehiclebrandandmodel;
    private EditText password, pwConfirm, etCode, etPhoneNum;
    private ImageView frontImg, sideImg, backImg, certRegImg, profImg, clearanceImg;
    private Boolean frontImgClicked =false, sideImgClicked =false, backImgClicked =false,
            certRegImgClicked =false, profImgClicked=false, clearanceImgClicked =false;
    TextView emptyNum, emptyPass, emptyConfirm, invalidPass, invalidConfirm, emptyCity, emptyVehicle;

    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_rider);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        etPhoneNum = findViewById(R.id.editTextPhoneNumDriver);
        password = findViewById(R.id.pwfield);
        pwConfirm = findViewById(R.id.pwConfirm);
        Button btnReg = findViewById(R.id.btnRegRider);
        emptyNum = findViewById(R.id.phoneErrorRider);
        emptyPass = findViewById(R.id.passErrorRider);
        emptyConfirm = findViewById(R.id.confirmErrorRider);
        invalidPass = findViewById(R.id.invalidPassRider);
        invalidConfirm = findViewById(R.id.invalidConfirmRider);
        emptyCity = findViewById(R.id.cityError);
        emptyVehicle = findViewById(R.id.vehicleError);
        CheckBox checkRider = findViewById(R.id.checkBoxAgreeRider);
        Spinner spinCity = findViewById(R.id.spinnerCityDriver);
        Spinner spinVehicle = findViewById(R.id.spinnerVehicleDriver);
        Spinner spinBrand =  findViewById(R.id.spinnerVehicleBrand);

        //Getting City Rider Item List
        ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(this, R.array.cityRider,R.layout.spinner_items_1);
        adapterCity.setDropDownViewResource(R.layout.spinner_items_1);
        spinCity.setAdapter(adapterCity);

        //Getting Vehicle Rider Type Item List
        ArrayAdapter<CharSequence> adapterVehicle = ArrayAdapter.createFromResource (this,R.array.ridervehicletype,R.layout.spinner_items_1);
        adapterVehicle.setDropDownViewResource(R.layout.spinner_items_1);
        spinVehicle.setAdapter(adapterVehicle);

        //Spinner City
        spinCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItemText = (String) adapterView.getItemAtPosition(i);
                if(i > 0){
                    Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    TextView tv = (TextView) view;
                    if(i == 0 )
                    {
                        tv.setTextColor(Color.GRAY);
                    }
                    else
                    {
                        tv.setTextColor(Color.BLACK);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Spinner Rider Vehicle Type
        spinVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItemText = (String) adapterView.getItemAtPosition(i);

                if(i == 1)
                {
                    ArrayAdapter<CharSequence> adapterBrand = ArrayAdapter.createFromResource(getApplicationContext(), R.array.ridervehiclebrandMC,R.layout.spinner_items_1);
                    adapterBrand.setDropDownViewResource(R.layout.spinner_items_1);
                    spinBrand.setAdapter(adapterBrand);
                    spinBrand.setVisibility(View.VISIBLE);
                }
                else if(i == 2)
                {
                    ArrayAdapter<CharSequence> adapterBrand = ArrayAdapter.createFromResource(getApplicationContext(), R.array.ridervehiclebrandSedan,R.layout.spinner_items_1);
                    adapterBrand.setDropDownViewResource(R.layout.spinner_items_1);
                    spinBrand.setAdapter(adapterBrand);
                    spinBrand.setVisibility(View.VISIBLE);
                }
                else if(i == 3)
                {
                    ArrayAdapter<CharSequence> adapterBrand = ArrayAdapter.createFromResource(getApplicationContext(), R.array.ridervehiclebrandSUV,R.layout.spinner_items_1);
                    adapterBrand.setDropDownViewResource(R.layout.spinner_items_1);
                    spinBrand.setAdapter(adapterBrand);
                    spinBrand.setVisibility(View.VISIBLE);
                }
                else if(i == 4)
                {
                    ArrayAdapter<CharSequence> adapterBrand = ArrayAdapter.createFromResource(getApplicationContext(), R.array.ridervehiclebrandMPV,R.layout.spinner_items_1);
                    adapterBrand.setDropDownViewResource(R.layout.spinner_items_1);
                    spinBrand.setAdapter(adapterBrand);
                    spinBrand.setVisibility(View.VISIBLE);
                }
                else if(i == 5)
                {
                    ArrayAdapter<CharSequence> adapterBrand = ArrayAdapter.createFromResource(getApplicationContext(), R.array.ridervehiclebrandSmallTruck,R.layout.spinner_items_1);
                    adapterBrand.setDropDownViewResource(R.layout.spinner_items_1);
                    spinBrand.setAdapter(adapterBrand);
                    spinBrand.setVisibility(View.VISIBLE);
                }

                if(i > 0){
                    Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                }
                else {
                    TextView tv = (TextView) view;
                    if (i == 0) {
                        tv.setTextColor(Color.GRAY);
                        spinBrand.setVisibility(View.GONE);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        EditText etModelVehicle = findViewById(R.id.editTextVehicleModel);
        EditText etBrandVehicle = findViewById(R.id.editTextVehicleBrand);
        //Spinner Vehicle Brand
        spinBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItemText = (String) adapterView.getItemAtPosition(i);

                if(i==5)
                {
                    etBrandVehicle.setVisibility(View.VISIBLE);

                }
                else
                {
                    etBrandVehicle.setVisibility(View.GONE);
                }

                if(i > 0){
                    Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                    etModelVehicle.setVisibility(View.VISIBLE);
                }
                else {
                    TextView tv = (TextView) view;
                    if (i == 0) {
                        tv.setTextColor(Color.GRAY);
                        etModelVehicle.setVisibility(View.GONE);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Dialog VerifyNum = new Dialog(RegisterRider.this);
        VerifyNum.setContentView(R.layout.fragment_rider_phonenum_verify);
        VerifyNum.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        VerifyNum.setCancelable(true);
        VerifyNum.getWindow().getAttributes().windowAnimations = R.style.animation;


        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean clear = true;
                if(TextUtils.isEmpty(etPhoneNum.getText().toString()))
                {
                    emptyNum.setText("Required");
                    emptyNum.setVisibility(view.VISIBLE);
                    etPhoneNum.setBackgroundResource(R.drawable.error_border_edittext);
                    clear = false;
                    return;

                }
                else  if(spinCity.getSelectedItemId() == 0)
                {
                    emptyCity.setText("Required");
                    emptyCity.setVisibility(view.VISIBLE);
                    spinCity.setBackgroundResource(R.drawable.error_border_edittext);
                    clear = false;
                    return;

                }
                else  if(spinVehicle.getSelectedItemId() == 0)
                {
                    emptyVehicle.setText("Required");
                    emptyVehicle.setVisibility(view.VISIBLE);
                    spinVehicle.setBackgroundResource(R.drawable.error_border_edittext);
                    clear = false;
                    return;

                }
                else  if(!checkRider.isChecked())
                {
                    Toast.makeText(RegisterRider.this,"Please agree on the terms and condition.",Toast.LENGTH_SHORT).show();
                    clear = false;
                    return;

                }
                else if(TextUtils.isEmpty(etModelVehicle.getText().toString()))
                {
                    etModelVehicle.setBackgroundResource(R.drawable.error_border_edittext);
                    clear = false;
                    return;

                }
                else if(etBrandVehicle.getVisibility() == View.VISIBLE && TextUtils.isEmpty(etBrandVehicle.getText().toString()))
                {
                    etBrandVehicle.setBackgroundResource(R.drawable.error_border_edittext);
                    clear = false;
                    return;

                }
                if (password.length() == 0)
                {
                    emptyPass.setText("Required");
                    emptyPass.setVisibility(view.VISIBLE);

                    findViewById(R.id.pwfield).setBackgroundResource(R.drawable.error_border_edittext);
                }
                if (pwConfirm.length() == 0)
                {
                    emptyConfirm.setText("Required");
                    emptyConfirm.setVisibility(view.VISIBLE);

                    findViewById(R.id.pwConfirm).setBackgroundResource(R.drawable.error_border_edittext);
                }
                else
                {
                    etPhoneNum.setBackgroundResource(R.drawable.graphics_edittext_1);
                    etModelVehicle.setBackgroundResource(R.drawable.graphics_edittext_1);
                    spinVehicle.setBackgroundResource(R.drawable.graphics_edittext_1);
                    etPhoneNum.setBackgroundResource(R.drawable.graphics_edittext_1);
                    etBrandVehicle.setBackgroundResource(R.drawable.graphics_edittext_1);
                    findViewById(R.id.pwConfirm).setBackgroundResource(R.drawable.graphics_edittext_1);
                    findViewById(R.id.pwfield).setBackgroundResource(R.drawable.graphics_edittext_1);
                    emptyNum.setVisibility(view.GONE);
                    emptyPass.setVisibility(view.GONE);
                    emptyCity.setVisibility(view.GONE);
                    emptyConfirm.setVisibility(view.GONE);
                    clear = true;
                }

                if(etBrandVehicle.getVisibility() == View.VISIBLE)
                {
                    vehiclebrandandmodel = etBrandVehicle.getText().toString() + " " + etModelVehicle.getText().toString();
                }
                else {
                    vehiclebrandandmodel = spinBrand.getSelectedItem().toString() + " " + etModelVehicle.getText().toString();
                }

                String password = ((EditText)findViewById(R.id.pwfield)).getText().toString();
                String pwConfirm = ((EditText)findViewById(R.id.pwConfirm)).getText().toString();
                boolean uppercase = !password.equals(password.toLowerCase());
                boolean lowercase = !password.equals(password.toUpperCase());
                boolean min6  = password.length() > 5;
                boolean PWgood = false;

                int digits = 0;
                int upper = 0;

                for (int i = 0; i < password.length(); i++) {
                    char ch = password.charAt(i);
                    if (ch >= 48 && ch <= 57)
                        digits++;
                    else if(ch>='A' && ch<='Z'){
                        upper++;
                    }
                }
                //check if password satisfies conditions
                if(!uppercase || !lowercase || !min6 || digits == 0)
                {
                    invalidPass.setText("Password must have at least 6 characters, one uppercase, lowercase, and number.");
                    invalidPass.setVisibility(view.VISIBLE);

                    findViewById(R.id.pwfield).setBackgroundResource(R.drawable.error_border_edittext);

                }

                // add confirm password function
                else if (min6 && uppercase && lowercase && digits >=1)
                {
                    invalidPass.setVisibility(view.GONE);

                    if (password.equals(pwConfirm))
                    {

                        findViewById(R.id.pwfield).setBackgroundResource(R.drawable.graphics_edittext_1);
                        findViewById(R.id.pwConfirm).setBackgroundResource(R.drawable.graphics_edittext_1);
                        PWgood = true;
                    }
                    else {
                        invalidConfirm.setVisibility(view.VISIBLE);
                        invalidConfirm.setText("Passwords do not match");
                        emptyConfirm.setVisibility(view.GONE);

                        findViewById(R.id.pwfield).setBackgroundResource(R.drawable.error_border_edittext);
                        findViewById(R.id.pwConfirm).setBackgroundResource(R.drawable.error_border_edittext);
                    }

                }

                if (PWgood && clear)
                {

                    db = FirebaseDatabase.getInstance();
                    rootie = db.getReference("riders");
                    // Getting the value of The given info in sign up to store in firebase
                    String phoneNum = etPhoneNum.getEditableText().toString();
                    Query accCheck = rootie.orderByChild("riderphone").equalTo(phoneNum);
                    // To check if the rider already exists
                    accCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Toast.makeText(btnReg.getContext(), "Account Already Exists. Please Sign In", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                sendVerificationCodeToUser(etPhoneNum.getText().toString());
                                VerifyNum.show();
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

        });

        regRiderStep1 = new Dialog(RegisterRider.this);
        regRiderStep1.setContentView(R.layout.fragment_signup_rider_step1);
        regRiderStep1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        regRiderStep1.setCancelable(true);
        regRiderStep1.getWindow().getAttributes().windowAnimations = R.style.animation;

        regRiderStep2 = new Dialog(RegisterRider.this);
        regRiderStep2.setContentView(R.layout.fragment_signup_rider_step2);
        regRiderStep2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        regRiderStep2.setCancelable(true);
        regRiderStep2.getWindow().getAttributes().windowAnimations = R.style.animation;

        regRiderStep3 = new Dialog(RegisterRider.this);
        regRiderStep3.setContentView(R.layout.fragment_signup_rider_step3);
        regRiderStep3.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        regRiderStep3.setCancelable(true);
        regRiderStep3.getWindow().getAttributes().windowAnimations = R.style.animation;

        regRiderStep4 = new Dialog(RegisterRider.this);
        regRiderStep4.setContentView(R.layout.fragment_signup_rider_step4);
        regRiderStep4.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        regRiderStep4.setCancelable(true);
        regRiderStep4.getWindow().getAttributes().windowAnimations = R.style.animation;

        regRiderStep5 = new Dialog(RegisterRider.this);
        regRiderStep5.setContentView(R.layout.fragment_signup_rider_step5);
        regRiderStep5.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        regRiderStep5.setCancelable(true);
        regRiderStep5.getWindow().getAttributes().windowAnimations = R.style.animation;

        Dialog successfullyRegistered = new Dialog(RegisterRider.this);
        successfullyRegistered.setContentView(R.layout.success_dialog);
        successfullyRegistered.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        successfullyRegistered.setCancelable(true);
        successfullyRegistered.getWindow().getAttributes().windowAnimations = R.style.animation;

        Button btnSuccessOkay = successfullyRegistered.findViewById(R.id.btnSuccessRegRider);


        btnSuccessOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterRider.this, riderLogin.class);
                startActivity(intent);
            }
        });

        EditText etVerifyCode =  VerifyNum.findViewById(R.id.etVerify);
        etCode =  VerifyNum.findViewById(R.id.etVerify);
        Button verify =  VerifyNum.findViewById(R.id.btnVerify);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = etVerifyCode.getText().toString();
                if(TextUtils.isEmpty(etVerifyCode.getText().toString()))
                {
                    etVerifyCode.setError("Required");
                    Toast.makeText(RegisterRider.this,"Please Enter The Code.",Toast.LENGTH_SHORT).show();
                    return;
                }
                verifyCode(code);
                VerifyNum.dismiss();
            }
        });


        Button nextstep1 = regRiderStep1.findViewById(R.id.nextstep1);
        Button nextstep2 = regRiderStep2.findViewById(R.id.nextstep2);
        Button nextstep3 = regRiderStep3.findViewById(R.id.nextstep3);
        Button nextstep4 = regRiderStep4.findViewById(R.id.nextstep4);
        Button register = regRiderStep5.findViewById(R.id.register);
        //for image upload
        //vehicle front button
        frontImg = regRiderStep5.findViewById(R.id.imgfront);
        frontImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
                frontImgClicked =true;
            }
        });
        //vehicle side button
        sideImg = regRiderStep5.findViewById(R.id.imgside);
        sideImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
                sideImgClicked =true;
            }
        });
        //vehicle back button
        backImg = regRiderStep5.findViewById(R.id.imgback);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
                backImgClicked =true;
            }
        });
        //vehicle cert reg button
        certRegImg = regRiderStep5.findViewById(R.id.imgcert);
        certRegImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
                certRegImgClicked =true;
            }
        });
        //vehicle profile photo button
        profImg = regRiderStep5.findViewById(R.id.imgprofile);
        profImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
                profImgClicked =true;
            }
        });
        //vehicle police clearance button
        clearanceImg = regRiderStep5.findViewById(R.id.imgclearance);
        clearanceImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
                clearanceImgClicked =true;
            }
        });

        EditText etDatePicker =  regRiderStep2.findViewById(R.id.etRiderDateofBirth);
        etDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = makeDateString(day,month,year);
                        etDatePicker.setText(date);
                    }
                };
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int style = AlertDialog.THEME_HOLO_LIGHT;

                datePickerDialog = new DatePickerDialog(RegisterRider.this,style,dateSetListener,year,month,day);
                datePickerDialog.show();
            }
        });

        nextstep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmptyEditText(regRiderStep1.findViewById(R.id.etNameRider));
                if(hasError)
                {
                    hasError = false;
                    return;
                }

                else
                {
                    regRiderStep2.show();
                    regRiderStep1.dismiss();
                }

            }
        });

        nextstep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmptyEditText(regRiderStep2.findViewById(R.id.etRiderDateofBirth));
                checkEmptyEditText(regRiderStep2.findViewById(R.id.etRiderDriverLicense));
                checkEmptyEditText(regRiderStep2.findViewById(R.id.etRiderDriverLicenseExpiry));
                checkEmptyEditText(regRiderStep2.findViewById(R.id.etRiderCurrentAddress));
                if(hasError)
                {
                    hasError = false;
                    return;
                }

                else
                {
                    regRiderStep3.show();
                    regRiderStep2.dismiss();
                }

            }
        });

        nextstep3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmptyEditText(regRiderStep3.findViewById(R.id.etRiderVehicleNumber));
                checkEmptyEditText(regRiderStep3.findViewById(R.id.etRiderManufactureYear));
                if(hasError)
                {
                    hasError = false;
                    return;
                }
                else
                {
                    regRiderStep4.show();
                    regRiderStep3.dismiss();
                }
            }
        });

        nextstep4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                regRiderStep5.show();
                regRiderStep4.dismiss();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String riderName  = getTextFromEditText(regRiderStep1.findViewById(R.id.etNameRider));
                String riderEmail = getTextFromEditText(regRiderStep1.findViewById(R.id.etEmailRider));
                String riderDateofBirth = getTextFromEditText(regRiderStep2.findViewById(R.id.etRiderDateofBirth));
                String riderDriverLicenseNumber = getTextFromEditText(regRiderStep2.findViewById(R.id.etRiderDriverLicense));
                String riderDriverLicenseExpiry = getTextFromEditText(regRiderStep2.findViewById(R.id.etRiderDriverLicenseExpiry));
                String riderCurrentAddress = getTextFromEditText(regRiderStep2.findViewById(R.id.etRiderCurrentAddress));
                String riderVehiclePlateNumber = getTextFromEditText(regRiderStep3.findViewById(R.id.etRiderVehicleNumber));
                String riderVehicleManufacturerYear = getTextFromEditText(regRiderStep3.findViewById(R.id.etRiderManufactureYear));
                String riderEMPerson = getTextFromEditText(regRiderStep5.findViewById(R.id.etEmergencyPerson));
                String riderEMNumber = getTextFromEditText(regRiderStep5.findViewById(R.id.etEmergencyNumber));
                String phonenum = getTextFromEditText(findViewById(R.id.editTextPhoneNumDriver));
                String riderpass = getTextFromEditText(findViewById(R.id.pwfield));
                checkEmptyEditText(regRiderStep5.findViewById(R.id.etEmergencyPerson));
                checkEmptyEditText(regRiderStep5.findViewById(R.id.etEmergencyNumber));


                if(hasError)
                {
                    hasError = false;
                    return;
                }
                else
                {
                    FirebaseUser userCurrent = mAuth.getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(String.valueOf(riderName)).build();
                    userCurrent.updateProfile(profileUpdates);
                    rootie = db.getReference();

                    HashMap riderInfo = new HashMap<>();
                    riderInfo.put("city",spinCity.getSelectedItem().toString());
                    riderInfo.put("vehicletype",spinVehicle.getSelectedItem().toString());
                    riderInfo.put("riderphone",phonenum);
                    riderInfo.put("name",riderName);
                    riderInfo.put("email",riderEmail);
                    riderInfo.put("riderpass",riderpass);
                    riderInfo.put("dateofbirth",riderDateofBirth);
                    riderInfo.put("driverlicensenumber",riderDriverLicenseNumber);
                    riderInfo.put("driverlicenseexpiry",riderDriverLicenseExpiry);
                    riderInfo.put("currentaddress",riderCurrentAddress);
                    riderInfo.put("vehicleplatenumber",riderVehiclePlateNumber);
                    riderInfo.put("manufactureryear",riderVehicleManufacturerYear);
                    riderInfo.put("emergencyperson",riderEMPerson);
                    riderInfo.put("emergencynumber",riderEMNumber);
                    riderInfo.put("vehiclebrandandmodel",vehiclebrandandmodel);
                    riderInfo.put("latitude","");
                    riderInfo.put("longitude","");
                    riderInfo.put("rate_total", 0);
                    riderInfo.put("rate_count", 0);


                    rootie.child("riders").child(phonenum).setValue(riderInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            successfullyRegistered.show();
                            regRiderStep5.dismiss();
                        }
                    });
                }
            }

        });

    }

    //to set image
    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }
    //get image data
    String imgName;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri=data.getData();
            if(frontImgClicked){
                frontImg.setImageURI(imageUri);
                imgName="front_image.jpg";
                uploadPicture();
                frontImgClicked =false;
            }else if(sideImgClicked){
                sideImg.setImageURI(imageUri);
                imgName="side_image.jpg";
                uploadPicture();
                sideImgClicked =false;
            }else if(backImgClicked){
                backImg.setImageURI(imageUri);
                imgName="back_image.jpg";
                uploadPicture();
                backImgClicked =false;
            }else if(certRegImgClicked){
                certRegImg.setImageURI(imageUri);
                imgName="certReg_image.jpg";
                uploadPicture();
                certRegImgClicked =false;
            }else if(profImgClicked){
                profImg.setImageURI(imageUri);
                imgName="profile_image.jpg";
                uploadPicture();
                profImgClicked =false;
            }else if(clearanceImgClicked){
                clearanceImg.setImageURI(imageUri);
                imgName="clearance_image.jpg";
                uploadPicture();
                clearanceImgClicked =false;
            }
        }
    }
    //upload method
    private void uploadPicture() {
        final ProgressDialog pd= new ProgressDialog(this);
        pd.setTitle("Uploading Image");
        pd.show();

        StorageReference riversRef=storageReference.child("rider/"+etPhoneNum.getText().toString()+"/"+imgName);

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded.", Snackbar.LENGTH_LONG).show();

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
                        pd.setMessage("Percentage: " + (int) progressPercent +"%");
                    }
                });


    }

    private String getTextFromEditText(EditText et)
    {
        EditText ett = et;
        return ett.getText().toString();
    }

    private String makeDateString(int day,int month,int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";

        if(month == 2)
            return "FEB";

        if(month == 3)
            return "MAR";

        if(month == 4)
            return "APR";

        if(month == 5)
            return "MAY";

        if(month == 6)
            return "JUNE";

        if(month == 7)
            return "JUL";

        if(month == 8)
            return "AUG";

        if(month == 9)
            return "SEP";

        if(month == 10)
            return "OCT";

        if(month == 11)
            return "NOV";

        if(month == 12)
            return "DEC";

        return "JAN";

    }

    public void checkEmptyEditText(EditText et)
    {
        EditText ett = et;
        String etstring = ett.getText().toString();
        if(TextUtils.isEmpty(etstring))
        {
            ett.setError("Required");
            hasError = true;
        }
    }

    private void sendVerificationCodeToUser(String phoneNo)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+63" + phoneNo,60, TimeUnit.SECONDS, this,mCallBacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
    {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationCodeBySystem = s;
            Toast.makeText(RegisterRider.this,"Code Sent.",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


            String code = phoneAuthCredential.getSmsCode();
            etCode.setText(code);
            if(code != null)
            {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(RegisterRider.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };


    private void verifyCode(String code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem,code);

        signInByCredential(credential);


    }

    private void signInByCredential(PhoneAuthCredential credential)
    {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    regRiderStep1.show();

                }
                else
                {
                    Toast.makeText(RegisterRider.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}