package com.example.mcc_deliveryapp.Rider;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.CellInfoGsm;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class riderLogin extends AppCompatActivity {
    DatabaseReference rootie;
    EditText numberRider,passRider, numberRider2;
    Button btnLogin;
    Button btnRegister, forgotPassnext, btnVerify, updatePW;
    TextView forgotPass, errorNumber, errorPass;
    FirebaseAuth mAuth;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    String verificationCodeBySystem;
    Dialog ForgotPW, VerifyNum, UpdatePW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_login);
        numberRider = findViewById(R.id.edtTextRiderNumber);
        passRider = findViewById(R.id.edtTextRiderPassword);
        btnLogin = findViewById(R.id.btnLoginRider);
        btnRegister = findViewById(R.id.btnRegisterRider);
        errorNumber = findViewById(R.id.errorNumber);
        errorPass = findViewById(R.id.errorPass);
        mAuth = FirebaseAuth.getInstance();
        forgotPass = findViewById(R.id.forgotPass);

        ForgotPW = new Dialog(riderLogin.this);
        ForgotPW.setContentView(R.layout.fragment_forgot_password);
        ForgotPW.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        ForgotPW.setCancelable(true);
        ForgotPW.getWindow().getAttributes().windowAnimations = R.style.animation;

        VerifyNum = new Dialog(riderLogin.this);
        VerifyNum.setContentView(R.layout.fragment_rider_phonenum_verify);
        VerifyNum.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        VerifyNum.setCancelable(true);
        VerifyNum.getWindow().getAttributes().windowAnimations = R.style.animation;

        UpdatePW = new Dialog(riderLogin.this);
        UpdatePW.setContentView(R.layout.fragment_update_password);
        UpdatePW.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        UpdatePW.setCancelable(true);
        UpdatePW.getWindow().getAttributes().windowAnimations = R.style.animation;
        EditText etVerifyCode =  VerifyNum.findViewById(R.id.etVerify);

        btnVerify = VerifyNum.findViewById(R.id.btnVerify);
        forgotPassnext = ForgotPW.findViewById(R.id.forgotPassnext);
        numberRider2 = ForgotPW.findViewById(R.id.forgotNumber);
        updatePW = UpdatePW.findViewById(R.id.updatePW);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginRider();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(riderLogin.this, RegisterRider.class);
                startActivity(intent);
                finish();
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotPW.show();

            }
        });

        forgotPassnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootie = db.getReference("riders");
                String riderNumF = getTextFromEditText(ForgotPW.findViewById(R.id.forgotNumber));
                Query accCheck = rootie.orderByChild("riderphone").equalTo(riderNumF);
                accCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            VerifyNum.show();
                            ForgotPW.dismiss();
                            sendVerificationCodeToUser(riderNumF);
                        }

                        else{
                            Toast.makeText(forgotPassnext.getContext(), "Account does not exist.", Toast.LENGTH_SHORT).show();

                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            private String getTextFromEditText(EditText et)
            {
                EditText ett = et;
                return ett.getText().toString();
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = etVerifyCode.getText().toString();
                if(TextUtils.isEmpty(etVerifyCode.getText().toString()))
                {
                    etVerifyCode.setError("Required");
                    Toast.makeText(riderLogin.this,"Please Enter The Code.",Toast.LENGTH_SHORT).show();
                    return;
                }
                verifyCode(code);
                VerifyNum.dismiss();
            }
        });
        updatePW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText pwfield = UpdatePW.findViewById(R.id.pwfield2);
                EditText pwconfirm = UpdatePW.findViewById(R.id.pwConfirm2);
                String riderNumF = getTextFromEditText(ForgotPW.findViewById(R.id.forgotNumber));
                String password = pwfield.getText().toString();
                String pwConfirm = pwconfirm.getText().toString();
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
                if(!uppercase || !lowercase || !min6 || digits == 0)
                {
                    pwfield.setError("Password most have at least 6 characters, one uppercase, lowercase, and number.", null);
                    pwfield.setBackgroundResource(R.drawable.error_border_edittext);

                }

                else if (min6 && uppercase && lowercase && digits >=1)
                {

                    if (password.equals(pwConfirm))
                    {
                        pwfield.setBackgroundResource(R.drawable.graphics_edittext_1);
                        pwconfirm.setBackgroundResource(R.drawable.graphics_edittext_1);
                        PWgood = true;
                    }
                    else {
                        pwconfirm.setError("Passwords do not match", null);
                        pwfield.setBackgroundResource(R.drawable.error_border_edittext);
                        pwconfirm.setBackgroundResource(R.drawable.error_border_edittext);
                    }

                }

                if (PWgood)
                {

                    db = FirebaseDatabase.getInstance();
                    rootie = db.getReference("riders");
                    FirebaseUser userCurrent = mAuth.getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(String.valueOf(riderNumF)).build();
                    userCurrent.updateProfile(profileUpdates);
                    rootie = db.getReference();

                    HashMap riderInfo = new HashMap();
                    riderInfo.put("riderpass",password);
                    rootie.child("riders").child(riderNumF).updateChildren(riderInfo);
                    Toast.makeText(riderLogin.this,"Password Updated",Toast.LENGTH_SHORT).show();
                    numberRider2.setText("");
                    pwfield.setText("");
                    pwconfirm.setText("");
                    UpdatePW.dismiss();


                }

            }

            private String getTextFromEditText(EditText et)
            {
                EditText ett = et;
                return ett.getText().toString();
            }
        });

    }

    private void loginRider()
    {
        String number = numberRider.getText().toString();
        String pass = passRider.getText().toString();


        if(TextUtils.isEmpty(number))
        {
            errorNumber.setText("Number Required");
            errorNumber.setVisibility(View.VISIBLE);
            numberRider.requestFocus();
            return;
        }
        else
        {
            errorNumber.setVisibility(View.GONE);
        }
        if(TextUtils.isEmpty(pass))
        {
            errorPass.setText("Password Required");
            errorPass.setVisibility(View.VISIBLE);
            passRider.requestFocus();
            return;
        }
        else
        {
            errorPass.setVisibility(View.GONE);
        }
        String riderNumEntered = numberRider.getText().toString().trim();
        String riderpassEntered = passRider.getText().toString().trim();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("riders");
        Query checkRider = ref.orderByChild("riderphone").equalTo(riderNumEntered);
        checkRider.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String riderpass = snapshot.child(riderNumEntered).child("riderpass").getValue(String.class);
                    String ridervehicle = snapshot.child(riderNumEntered).child("vehicletype").getValue(String.class);
                    String ridername = snapshot.child(riderNumEntered).child("name").getValue(String.class);

                    if (riderpass.equals(riderpassEntered))
                    {

                        Intent intent = new Intent(riderLogin.this, rider_dashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("phonenum", riderNumEntered);
                        intent.putExtra("vehicle", ridervehicle);
                        intent.putExtra("name", ridername);
                        startActivity(intent);
                    }

                    else {
                        errorPass.setText("Wrong Password");
                        errorPass.setVisibility(View.VISIBLE);

                        }

                }

                else{
                    errorNumber.setText("Account does not exist");
                    errorNumber.setVisibility(View.VISIBLE);
                    errorPass.setVisibility(View.GONE);

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });




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
            Toast.makeText(riderLogin.this,"Code Sent.",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


            String code = phoneAuthCredential.getSmsCode();
            if(code != null)
            {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(riderLogin.this,e.getMessage(),Toast.LENGTH_LONG).show();
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
                    UpdatePW.show();

                }
            }
        });

    }
}
