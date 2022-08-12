package com.example.mcc_deliveryapp.User;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mcc_deliveryapp.MainActivity2;
import com.example.mcc_deliveryapp.R;
import com.example.mcc_deliveryapp.Rider.RegisterRider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class signUpFragment extends Fragment {
    //setting the value of the given edit_text
    EditText editTxt_fullname,editTxt_phoneNum,editTxt_password,editTxt_Cpassword;
    Button btn_createAcc;
    String verificationCodeBySystem_user;
    TextInputLayout textInputPassword;
    EditText etCode_user;
    TextView userName, userNumber, emptyName, emptyNum, emptyPass, emptyConfirm, invalidPass, invalidConfirm;
    //Database Realtime
    FirebaseDatabase root;
    DatabaseReference DbRef;
    FirebaseAuth fAuth;

    String fullname;
    String phoneNum;
    String pass;
    String Cpass;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        // Initialization of text
        emptyName = view.findViewById(R.id.emptyName);
        emptyNum = view.findViewById(R.id.emptyNum);
        emptyPass = view.findViewById(R.id.emptyPass);
        emptyConfirm = view.findViewById(R.id.emptyConfirm);
        invalidPass = view.findViewById(R.id.invalidPass);
        invalidConfirm = view.findViewById(R.id.invalidConfirm);

        editTxt_fullname = view.findViewById(R.id.edTextUserName);
        editTxt_phoneNum = view.findViewById(R.id.edTextPhoneNo);
        editTxt_password = view.findViewById(R.id.edTextPass);
        editTxt_Cpassword = view.findViewById(R.id.edTextConfirmPass);
        btn_createAcc= view.findViewById(R.id.btn_createAccount);
        textInputPassword = view.findViewById(R.id.edTextPassL);

        btn_createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean clear = true;

                if(TextUtils.isEmpty(editTxt_fullname.getText().toString()))
                {
                    emptyName.setText("Required");
                    emptyName.setVisibility(view.VISIBLE);

                    clear = false;

                    return;

                }
                 else if(TextUtils.isEmpty(editTxt_phoneNum.getText().toString()))
                {
                    emptyNum.setText("Required");
                    emptyNum.setVisibility(view.VISIBLE);

                    clear = false;
                    return;
                }
                else if(TextUtils.isEmpty(editTxt_password.getText().toString()))
                {
                    emptyPass.setText("Required");
                    emptyPass.setVisibility(view.VISIBLE);

                    clear = false;
                    return;
                }
                else if(TextUtils.isEmpty(editTxt_Cpassword.getText().toString()))
                {
                    emptyConfirm.setText("Required");
                    emptyConfirm.setVisibility(view.VISIBLE);

                    clear = false;
                    return;
                }

                else
                {

                    clear = true;
                }
                if (editTxt_fullname.length()!=0)
                {
                    emptyName.setVisibility(view.GONE);

                }
                if (editTxt_phoneNum .length()!=0)
                {
                    emptyNum.setVisibility(view.GONE);

                }
                if (editTxt_password.length()!=0)
                {
                    emptyPass.setVisibility(view.GONE);

                }
                if (editTxt_Cpassword.length()!=0)
                {
                    emptyConfirm.setVisibility(view.GONE);

                }
                String password = editTxt_password.getText().toString();
                String pwConfirm = editTxt_Cpassword.getText().toString();
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
                    invalidPass.setText("Password most have at least 6 characters, one uppercase, lowercase, and number.");
                    invalidPass.setVisibility(view.VISIBLE);

                }
                // add confirm password function
                else if (min6 && uppercase && lowercase && digits >=1)
                {
                    invalidPass.setVisibility(view.GONE);

                    if (password.equals(pwConfirm))
                    {

                        PWgood = true;
                    }
                    else {
                        invalidConfirm.setText("Passwords do not match.");
                        invalidConfirm.setVisibility(view.VISIBLE);

                    }

                }


                if(PWgood && clear){
                    root = FirebaseDatabase.getInstance();
                    DbRef = root.getReference("users");
                    // Getting the value of The given info in sign up to store in firebase
                    fullname = editTxt_fullname.getEditableText().toString();
                    phoneNum = editTxt_phoneNum.getEditableText().toString();
                    pass = editTxt_password.getEditableText().toString();
                    Cpass = editTxt_Cpassword.getEditableText().toString();


                    // To check if the user already exists
                    Query accCheck = DbRef.orderByChild("userPhone").equalTo(phoneNum);
                    accCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Toast.makeText(getContext(), "Account Already Exists. Please Sign In", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Dialog VerifyNum = new Dialog(getContext());
                                VerifyNum.setContentView(R.layout.fragment_user_phonenum_verify);
                                VerifyNum.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                VerifyNum.setCancelable(false);
                                VerifyNum.getWindow().getAttributes().windowAnimations = R.style.animation;
                                etCode_user = VerifyNum.findViewById(R.id.etVerify_user);
                                userName= VerifyNum.findViewById(R.id.user_Fullname);
                                userNumber= VerifyNum.findViewById(R.id.user_ContactNo);
                                userName.setText(fullname);
                                userNumber.setText(phoneNum);
                                Button verify_user = VerifyNum.findViewById(R.id.btnVerify_user);

                                verify_user.setOnClickListener(new View.OnClickListener() {
                                @Override
                                    public void onClick(View view) {
                                        String code_user = etCode_user.getText().toString();
                                        if (TextUtils.isEmpty(etCode_user.getText().toString())){
                                            etCode_user.setError("Required");
                                            Toast.makeText(getActivity(), "Please Enter The Code.", Toast.LENGTH_SHORT).show();
                                        }else{
                                                VerifyCodeUser(code_user);
                                        }
                                        }
                                    });
                                    //sendVerificationCodeToUser(Objects.requireNonNull(login_editTxt_phoneNum.getEditText()).getText().toString());
                                    sendVerificationCodeToUser(editTxt_phoneNum.getText().toString());
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

        return view;
    }

    // Clear the Sign up Section
    private void Clear(){

        editTxt_fullname.setText("");
        editTxt_phoneNum.setText("");
        editTxt_password.setText("");
        editTxt_Cpassword.setText("");
    }

    private void sendVerificationCodeToUser(String PhoneNo_User) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber("+63" + PhoneNo_User,60, TimeUnit.SECONDS, getActivity(),mCallBacks);

    }
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationCodeBySystem_user = s;
            Toast.makeText(getContext(),"Code Sent.",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code_user = phoneAuthCredential.getSmsCode();
           etCode_user.setText(code_user);
            if(code_user != null)
            {
                VerifyCodeUser(code_user);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };
    private void VerifyCodeUser(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem_user,code);

        signInByCredential(credential);

    }
    private void signInByCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {

                   //user helper class in order to store the the given info in sign up form
                  UserHelperClass userHelperClass = new UserHelperClass(fullname, phoneNum, pass, "", "");
                  DbRef.child(phoneNum).setValue(userHelperClass);
                  Toast.makeText(getContext(), "Account Successfully Created", Toast.LENGTH_SHORT).show();
                  //Implementing the Clear Section in Sign up after the Creation of Account
                  Intent intent = new Intent(getActivity(), MainActivity2.class);
                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                  startActivity(intent);
                  Clear();
                }
                else
                {
                    Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}