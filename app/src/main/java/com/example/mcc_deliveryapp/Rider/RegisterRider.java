package com.example.mcc_deliveryapp.Rider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mcc_deliveryapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class RegisterRider extends AppCompatActivity {

    FirebaseAuth mAuth;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference rootie;

    EditText etCode;

    DatePickerDialog datePickerDialog;

    Dialog regRiderInfo;

    Boolean hasError = false;

    String verificationCodeBySystem;

    String vehiclebrandandmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();



        setContentView(R.layout.activity_register_rider);

        EditText etPhoneNum = (EditText) findViewById(R.id.editTextPhoneNumDriver);

        EditText password = findViewById(R.id.pwfield);
        EditText pwConfirm = findViewById(R.id.pwConfirm);

        Button btnReg = (Button) findViewById(R.id.btnRegRider);

        CheckBox checkRider = (CheckBox) findViewById(R.id.checkBoxAgreeRider);

        Spinner spinCity = (Spinner) findViewById(R.id.spinnerCityDriver);
        Spinner spinVehicle = (Spinner) findViewById(R.id.spinnerVehicleDriver);
        Spinner spinBrand = (Spinner) findViewById(R.id.spinnerVehicleBrand);




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
                    ArrayAdapter<CharSequence> adapterBrand = ArrayAdapter.createFromResource(getApplicationContext(), R.array.ridervehiclebrandmotorcycle,R.layout.spinner_items_1);
                    adapterBrand.setDropDownViewResource(R.layout.spinner_items_1);
                    spinBrand.setAdapter(adapterBrand);
                    spinBrand.setVisibility(View.VISIBLE);
                }
                else if(i == 2)
                {
                    ArrayAdapter<CharSequence> adapterBrand = ArrayAdapter.createFromResource(getApplicationContext(), R.array.ridervehiclebrandsuvcycle,R.layout.spinner_items_1);
                    adapterBrand.setDropDownViewResource(R.layout.spinner_items_1);
                    spinBrand.setAdapter(adapterBrand);
                    spinBrand.setVisibility(View.VISIBLE);
                }
                else if(i == 3)
                {
                    ArrayAdapter<CharSequence> adapterBrand = ArrayAdapter.createFromResource(getApplicationContext(), R.array.ridervehiclebrandvancycle,R.layout.spinner_items_1);
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

        EditText etModelVehicle = (EditText) findViewById(R.id.editTextVehicleModel);
        EditText etBrandVehicle = (EditText) findViewById(R.id.editTextVehicleBrand);
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
        VerifyNum.setCancelable(false);
        VerifyNum.getWindow().getAttributes().windowAnimations = R.style.animation;


        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                if(TextUtils.isEmpty(etPhoneNum.getText().toString()))
                {
                    etPhoneNum.setError("Required");
                    Toast.makeText(RegisterRider.this,"A Number is Required.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password.getText().toString()))
                {
                    password.setError("Fill up your Password");
                    Toast.makeText(RegisterRider.this,"Password cannot be Empty",Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(pwConfirm.getText().toString()))
                {
                    pwConfirm.setError("Fill up your Confirm Password");
                    Toast.makeText(RegisterRider.this,"Password Confirm cannot be Empty",Toast.LENGTH_SHORT).show();
                }
                if(spinCity.getSelectedItemId() == 0)
                {
                    Toast.makeText(RegisterRider.this,"Input a city.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(spinVehicle.getSelectedItemId() == 0)
                {
                    Toast.makeText(RegisterRider.this,"Input a type of Vehicle.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!checkRider.isChecked())
                {
                    Toast.makeText(RegisterRider.this,"Please agree on the terms and condition..",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(etModelVehicle.getText().toString()))
                {
                    Toast.makeText(RegisterRider.this,"Vehicle Model is required.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(etBrandVehicle.getVisibility() == View.VISIBLE && TextUtils.isEmpty(etBrandVehicle.getText().toString()))
                {
                    Toast.makeText(RegisterRider.this,"Vehicle Brand is required.",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(etBrandVehicle.getVisibility() == View.VISIBLE)
                {
                    vehiclebrandandmodel = etBrandVehicle.getText().toString() + " " + etModelVehicle.getText().toString();
                }
                else {
                    vehiclebrandandmodel = spinBrand.getSelectedItem().toString() + " " + etModelVehicle.getText().toString();
                }
                if(password.getText().toString().equals(pwConfirm.getText().toString())){
                    sendVerificationCodeToUser(etPhoneNum.getText().toString());
                    VerifyNum.show();

                }else{
                    pwConfirm.setError("Pw is not Match");
                    Toast.makeText(RegisterRider.this,"Password is not Match",Toast.LENGTH_SHORT).show();
                }
            }
        });

        regRiderInfo = new Dialog(RegisterRider.this);
        regRiderInfo.setContentView(R.layout.fragment_signup_rider_step1);
        regRiderInfo.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        regRiderInfo.setCancelable(true);
        regRiderInfo.getWindow().getAttributes().windowAnimations = R.style.animation;

        Dialog successfullyRegistered = new Dialog(RegisterRider.this);
        successfullyRegistered.setContentView(R.layout.success_dialog);
        successfullyRegistered.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        successfullyRegistered.setCancelable(false);
        successfullyRegistered.getWindow().getAttributes().windowAnimations = R.style.animation;

        Button btnSuccessOkay = successfullyRegistered.findViewById(R.id.btnSuccessRegRider);

        btnSuccessOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterRider.this, riderLogin.class);
                startActivity(intent);
            }
        });

        EditText etVerifyCode = (EditText) VerifyNum.findViewById(R.id.etVerify);
        etCode = (EditText) VerifyNum.findViewById(R.id.etVerify);
        Button verify = (Button) VerifyNum.findViewById(R.id.btnVerify);

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

        Button btnRegRiderInfo = (Button) regRiderInfo.findViewById(R.id.btnRegRiderInfo);

        EditText etDatePicker = (EditText) regRiderInfo.findViewById(R.id.etRiderDateofBirth);

        etDatePicker.setText(getTodaysDate());

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

        btnRegRiderInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkEmptyEditText(regRiderInfo.findViewById(R.id.etNameRider));
                checkEmptyEditText(regRiderInfo.findViewById(R.id.etRiderDateofBirth));
                checkEmptyEditText(regRiderInfo.findViewById(R.id.etRiderDriverLicense));
                checkEmptyEditText(regRiderInfo.findViewById(R.id.etRiderDriverLicenseExpiry));
                checkEmptyEditText(regRiderInfo.findViewById(R.id.etRiderCurrentAddress));
                checkEmptyEditText(regRiderInfo.findViewById(R.id.etRiderVehicleNumber));
                checkEmptyEditText(regRiderInfo.findViewById(R.id.etRiderManufactureYear));

                if(hasError)
                {
                    hasError = false;
                    return;

                }

                String riderName = getTextFromEditText(regRiderInfo.findViewById(R.id.etNameRider));
                String riderEmail = getTextFromEditText(regRiderInfo.findViewById(R.id.etEmailRider));
                String riderDateofBirth = getTextFromEditText(regRiderInfo.findViewById(R.id.etRiderDateofBirth));
                String riderDriverLicenseNumber = getTextFromEditText(regRiderInfo.findViewById(R.id.etRiderDriverLicense));
                String riderDriverLicenseExpiry = getTextFromEditText(regRiderInfo.findViewById(R.id.etRiderDriverLicenseExpiry));
                String riderCurrentAddress = getTextFromEditText(regRiderInfo.findViewById(R.id.etRiderCurrentAddress));
                String riderVehiclePlateNumber = getTextFromEditText(regRiderInfo.findViewById(R.id.etRiderVehicleNumber));
                String riderVehicleManufacturerYear = getTextFromEditText(regRiderInfo.findViewById(R.id.etRiderManufactureYear));
                String riderpass = ((EditText)findViewById(R.id.pwfield)).getText().toString();
                String phonenum = ((EditText)findViewById(R.id.editTextPhoneNumDriver)).getText().toString();

                FirebaseUser userCurrent = mAuth.getCurrentUser();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(riderName).build();

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
                riderInfo.put("vehiclebrandandmodel",vehiclebrandandmodel);


                rootie.child("riders").child(phonenum).setValue(riderInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        successfullyRegistered.show();
                        regRiderInfo.dismiss();

                    }
                });

            }
        });



    }
    /*
    //Password Validation
    public void validatePW(View view){
        String password = ((EditText)findViewById(R.id.pwfield)).getText().toString();
        String pwConfirm = ((EditText)findViewById(R.id.pwConfirm)).getText().toString();
        boolean uppercase = !password.equals(password.toLowerCase());
        boolean lowercase = !password.equals(password.toUpperCase());
        boolean min6  = password.length() > 5;
        boolean good = false;
        //check number of digits, uppercase, and lowercase char for password checking
        //lowercase not used
        int digits = 0;
        int upper = 0;
        int lower = 0;
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (ch >= 48 && ch <= 57)
                digits++;
            else if(ch>='A' && ch<='Z'){
                upper++;
            }
            else if(ch>='a' && ch<='z'){
                lower++;
            }
        }
        //check if password satisfies conditions
        if(!uppercase && min6 && digits >= 1)
        {
            ((EditText)findViewById(R.id.pwfield)).setError("Must have an uppercase letter", null);
            findViewById(R.id.pwfield).setBackgroundResource(R.drawable.error_border_edittext);

        }
        else if(!lowercase && min6 && digits >= 1)
        {
            ((EditText)findViewById(R.id.pwfield)).setError("Must have a lowercase letter", null);
            findViewById(R.id.pwfield).setBackgroundResource(R.drawable.error_border_edittext);

        }
        else if(!lowercase && !uppercase && !min6 && digits >= 1)
        {
            ((EditText)findViewById(R.id.pwfield)).setError("Must have uppercase and lowercase letter", null);
            findViewById(R.id.pwfield).setBackgroundResource(R.drawable.error_border_edittext);

        }
        else if (!min6 && lowercase && uppercase && digits >= 1)
        {
            ((EditText)findViewById(R.id.pwfield)).setError("Too short");
            findViewById(R.id.pwfield).setBackgroundResource(R.drawable.error_border_edittext);

        }
        else if (!min6 && !lowercase && digits >= 1)
        {
            ((EditText)findViewById(R.id.pwfield)).setError("Too short and must have lowercase letter", null);
            findViewById(R.id.pwfield).setBackgroundResource(R.drawable.error_border_edittext);

        }
        else if (!min6 && !uppercase && digits >= 1)
        {
            ((EditText)findViewById(R.id.pwfield)).setError("Too short and must have uppercase letter", null);
            findViewById(R.id.pwfield).setBackgroundResource(R.drawable.error_border_edittext);

        }
        else if(!uppercase && min6 && digits == 0)
        {
            ((EditText)findViewById(R.id.pwfield)).setError("Must have an uppercase letter and number", null);
            findViewById(R.id.pwfield).setBackgroundResource(R.drawable.error_border_edittext);

        }
        else if(!lowercase && min6 && digits == 0)
        {
            ((EditText)findViewById(R.id.pwfield)).setError("Must have a lowercase letter and number", null);
            findViewById(R.id.pwfield).setBackgroundResource(R.drawable.error_border_edittext);

        }
        else if (!min6 && !lowercase && digits == 0)
        {
            ((EditText)findViewById(R.id.pwfield)).setError("Too short and must have lowercase letter and number", null);
            findViewById(R.id.pwfield).setBackgroundResource(R.drawable.error_border_edittext);

        }
        else if (!min6 && !uppercase && digits == 0)
        {
            ((EditText)findViewById(R.id.pwfield)).setError("Too short and must have uppercase letter and number", null);
            findViewById(R.id.pwfield).setBackgroundResource(R.drawable.error_border_edittext);

        }
        else if (digits == 0)
        {
            ((EditText)findViewById(R.id.pwfield)).setError("Must have number", null);
            findViewById(R.id.pwfield).setBackgroundResource(R.drawable.error_border_edittext);


        }
        // add confirm password function
        else if (min6 && uppercase && lowercase && digits >=1)
        {
            //temporary toast
            if (password.equals(pwConfirm))
            {
                Toast.makeText(RegisterRider.this,"Password is good!",Toast.LENGTH_SHORT).show();
                findViewById(R.id.pwfield).setBackgroundResource(R.drawable.graphics_edittext_1);
                findViewById(R.id.pwConfirm).setBackgroundResource(R.drawable.graphics_edittext_1);
                good = true;
            }
            else {
                ((EditText)findViewById(R.id.pwConfirm)).setError("Passwords do not match", null);
                findViewById(R.id.pwfield).setBackgroundResource(R.drawable.error_border_edittext);
                findViewById(R.id.pwConfirm).setBackgroundResource(R.drawable.error_border_edittext);
            }

        }

        else if (password.length() == 0)
        {
            Toast.makeText(RegisterRider.this,"Password cannot be empty",Toast.LENGTH_SHORT).show();
        }

        String strength = "";

        //check for password strength
        if (good)
        {
            float lengthscore = 0;
            float UCscore = 0;
            float numscore = 0;
            float strengthscore = 0;
            if (password.length() == 6)
            {
                lengthscore = (float) 0.2;
            }
            else if (password.length()>= 7 && password.length() <= 9)
            {
                lengthscore = (float) 0.3;
            }
            else if (password.length() > 9)
            {
                lengthscore = (float) 0.5;
            }
            if(upper == 2)
            {
                UCscore = (float) 0.2;
            }
            else if(upper > 2)
            {
                UCscore = (float) 0.3;
            }
            if(digits == 2)
            {
                numscore = (float) 0.2;
            }
            else if(digits > 2)
            {
                numscore = (float) 0.3;
            }
            strengthscore = lengthscore + UCscore + numscore;

            if (strengthscore < 0.5)
            {
                strength = "Weak";
            }
            else if (strengthscore >= 0.5 && strengthscore < 1)
            {
                strength = "Moderate";
            }
            else if (strengthscore >=1 )
            {
                strength = "Strong";
            }
            ((TextView)findViewById(R.id.pwStrength)).setText("Password Strength: " + strength);
        }

        else {
            ((TextView)findViewById(R.id.pwStrength)).setText("Password does not satisfy conditions");

        }
    }*/

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        month = month + 1;

        return makeDateString(day,month,year);
    }

    private String getTextFromEditText(EditText et)
    {
        EditText ett = (EditText) et;
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
        EditText ett = (EditText) et;
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

                    regRiderInfo.show();
                    /*Intent intent = new Intent(RegisterRider.this, MainActivityRider.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);*/
                }
                else
                {
                    Toast.makeText(RegisterRider.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

}