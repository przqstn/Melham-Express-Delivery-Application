package com.example.mcc_deliveryapp.User;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mcc_deliveryapp.R;
import com.example.mcc_deliveryapp.Rider.editprofile_changePass;
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

public class user_editprofile_changePass extends AppCompatActivity {

    private String phoneNum, verificationCodeBySystem;
    private EditText oldPass, newPass, confirmPass;
    private TextView forgotPass;
    private ImageButton btnSaveChanges;
    private Dialog VerifyNum, UpdatePW, ForgotPW;
    private DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        Intent intent=getIntent();
        phoneNum=intent.getStringExtra("usernum");

        btnSaveChanges=findViewById(R.id.btn_saveChanges);
        oldPass=findViewById(R.id.enter_oldPW);
        newPass=findViewById(R.id.enter_newPW);
        confirmPass=findViewById(R.id.confirm_newPW);
        forgotPass=findViewById(R.id.forgotPass);

        root = FirebaseDatabase.getInstance().getReference("users");
        Query check=root.orderByChild("userPhone").equalTo(phoneNum);

        oldPass.addTextChangedListener(new GenericTextWatcher(oldPass));
        newPass.addTextChangedListener(new GenericTextWatcher(newPass));
        confirmPass.addTextChangedListener(new GenericTextWatcher(confirmPass));

        btnSaveChanges.setVisibility(View.GONE);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String usernum=snapshot.child(phoneNum).child("userPass").getValue(String.class);
                            if(oldPass.getEditableText().toString().equals(usernum)) {
                                String password = newPass.getEditableText().toString();
                                String pwConfirm = confirmPass.getEditableText().toString();
                                if (!oldPass.getEditableText().toString().equals(password)) {
                                    boolean uppercase = !password.equals(password.toLowerCase());
                                    boolean lowercase = !password.equals(password.toUpperCase());
                                    boolean min6 = password.length() > 5;
                                    boolean PWgood = false;

                                    int digits = 0;
                                    int upper = 0;

                                    for (int i = 0; i < password.length(); i++) {
                                        char ch = password.charAt(i);
                                        if (ch >= 48 && ch <= 57)
                                            digits++;
                                        else if (ch >= 'A' && ch <= 'Z') {
                                            upper++;
                                        }
                                    }
                                    if (!uppercase || !lowercase || !min6 || digits == 0) {
                                        newPass.setError("Password must have at least 6 characters, one uppercase, lowercase, and number.");
                                    } else if (min6 && uppercase && lowercase && digits >= 1) {
                                        if (password.equals(pwConfirm)) {
                                            PWgood = true;
                                        } else {
                                            confirmPass.setError("Passwords do not match");
                                        }
                                    }
                                    if (PWgood) {
                                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                                        DatabaseReference rootie = db.getReference("users");
                                        rootie = db.getReference();

                                        HashMap userInfo = new HashMap();
                                        userInfo.put("userPass", password);
                                        rootie.child("users").child(phoneNum).updateChildren(userInfo);

                                        final Dialog dialog = new Dialog(btnSaveChanges.getContext());
                                        dialog.setContentView(R.layout.passwordchanged_dialog);
                                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                                        dialog.setCancelable(true);
                                        dialog.show();
                                        Button btnOk = dialog.findViewById(R.id.btn_ok);
                                        btnOk.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.dismiss();
                                                finish();
                                            }
                                        });
                                    }
                                }else{
                                    newPass.setError("Password cannot be the same!");
                                }
                            }else {
                                oldPass.setError("Password does not exist!");
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        });
        //for forgot password
        ForgotPW = new Dialog(this);
        ForgotPW.setContentView(R.layout.fragment_forgot_password);
        ForgotPW.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        ForgotPW.setCancelable(true);
        ForgotPW.getWindow().getAttributes().windowAnimations = R.style.animation;

        VerifyNum = new Dialog(this);
        VerifyNum.setContentView(R.layout.fragment_rider_phonenum_verify);
        VerifyNum.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        VerifyNum.setCancelable(true);
        VerifyNum.getWindow().getAttributes().windowAnimations = R.style.animation;

        UpdatePW = new Dialog(this);
        UpdatePW.setContentView(R.layout.fragment_update_password);
        UpdatePW.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        UpdatePW.setCancelable(true);
        UpdatePW.getWindow().getAttributes().windowAnimations = R.style.animation;
        EditText etVerifyCode =  VerifyNum.findViewById(R.id.etVerify);

        Button btnVerify = VerifyNum.findViewById(R.id.btnVerify);
        Button forgotPassnext = ForgotPW.findViewById(R.id.forgotPassnext);
        EditText numberRider2 = ForgotPW.findViewById(R.id.forgotNumber);
        Button updatePW = UpdatePW.findViewById(R.id.updatePW);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotPW.show();
            }
        });
        forgotPassnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference rootie = db.getReference("users");
                String userNumF = getTextFromEditText(ForgotPW.findViewById(R.id.forgotNumber));
                Query accCheck = rootie.orderByChild("userPhone").equalTo(userNumF);
                accCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            VerifyNum.show();
                            ForgotPW.dismiss();
                            sendVerificationCodeToUser(userNumF);
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
                    Toast.makeText(user_editprofile_changePass.this,"Please Enter The Code.",Toast.LENGTH_SHORT).show();
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
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference rootie = db.getReference("users");
                    db = FirebaseDatabase.getInstance();
                    rootie = db.getReference("users");
                    FirebaseUser userCurrent = mAuth.getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(String.valueOf(riderNumF)).build();
                    userCurrent.updateProfile(profileUpdates);
                    rootie = db.getReference();

                    HashMap userInfo = new HashMap();
                    userInfo.put("userPass",password);
                    rootie.child("users").child(riderNumF).updateChildren(userInfo);
                    UpdatePW.dismiss();

                    final Dialog dialog = new Dialog(btnSaveChanges.getContext());
                    dialog.setContentView(R.layout.passwordchanged_dialog);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                    dialog.setCancelable(true);
                    dialog.show();
                    Button btnOk = dialog.findViewById(R.id.btn_ok);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                }
            }
            private String getTextFromEditText(EditText et)
            {
                EditText ett = et;
                return ett.getText().toString();
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (!TextUtils.isEmpty(newPass.getEditableText().toString())
                ||!TextUtils.isEmpty(confirmPass.getEditableText().toString())){
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
    private class GenericTextWatcher implements TextWatcher {
        private View view;
        private GenericTextWatcher(View view) {
            this.view = view;
        }
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            if(!TextUtils.isEmpty(oldPass.getText().toString())&&
                    !TextUtils.isEmpty(newPass.getText().toString())&&
                    !TextUtils.isEmpty(confirmPass.getText().toString())){
                btnSaveChanges.setVisibility(View.VISIBLE);
            }else{
                btnSaveChanges.setVisibility(View.GONE);
            }
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
            Toast.makeText(user_editprofile_changePass.this,"Code Sent.",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(user_editprofile_changePass.this,e.getMessage(),Toast.LENGTH_LONG).show();
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
