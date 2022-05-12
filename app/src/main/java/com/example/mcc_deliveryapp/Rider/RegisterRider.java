package com.example.mcc_deliveryapp.Rider;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class RegisterRider extends AppCompatActivity {

    FirebaseAuth mAuth;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference rootie;

    EditText etCode;

    String verificationCodeBySystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_rider);

        EditText etPhoneNum = (EditText) findViewById(R.id.editTextPhoneNumDriver);

        Button btnReg = (Button) findViewById(R.id.btnRegRider);

        CheckBox checkRider = (CheckBox) findViewById(R.id.checkBoxAgreeRider);

        Spinner spinCity = (Spinner) findViewById(R.id.spinnerCityDriver);
        Spinner spinVehicle = (Spinner) findViewById(R.id.spinnerVehicleDriver);

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
                if(i > 0){
                    Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                }
                else {
                    TextView tv = (TextView) view;
                    if (i == 0) {
                        tv.setTextColor(Color.GRAY);
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

                sendVerificationCodeToUser(etPhoneNum.getText().toString());
                VerifyNum.show();



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
            }
        });
    }
    private void sendVerificationCodeToUser(String phoneNo) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+63" + phoneNo,60, TimeUnit.SECONDS, this,mCallBacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

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


    private void verifyCode(String code) {
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
                    Intent intent = new Intent(RegisterRider.this, MainActivityRider.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(RegisterRider.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

}