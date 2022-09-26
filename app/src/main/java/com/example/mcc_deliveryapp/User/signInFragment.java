package com.example.mcc_deliveryapp.User;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mcc_deliveryapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class signInFragment extends Fragment {
    FirebaseAuth mAuth;

    FirebaseDatabase db;

    DatabaseReference root;
    EditText numberUser;
    Button forgotPassnext, btnVerify, updatePW;
    TextView forgotPass, wrongNum, wrongPass;
    String verificationCodeBySystem;
    Dialog ForgotPW, VerifyNum, UpdatePW;

    TextInputLayout login_editTxt_phoneNum, login_editTxt_password;
    Button btn_Login, btn_sign_with_google;

    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        wrongNum = view.findViewById(R.id.phoneError);
        wrongPass = view.findViewById(R.id.passError);
        login_editTxt_phoneNum = view.findViewById(R.id.edTextPhoneNo);
        login_editTxt_password = view.findViewById(R.id.edTextPass);
        btn_Login = view.findViewById(R.id.btn_login);
        btn_sign_with_google = view.findViewById(R.id.btn_sign_with_google);

        forgotPass = view.findViewById(R.id.forgotPassUser);
        ForgotPW = new Dialog(getActivity());
        ForgotPW.setContentView(R.layout.fragment_forgot_password);
        ForgotPW.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        ForgotPW.setCancelable(true);
        ForgotPW.getWindow().getAttributes().windowAnimations = R.style.animation;

        VerifyNum = new Dialog(getActivity());
        VerifyNum.setContentView(R.layout.fragment_rider_phonenum_verify);
        VerifyNum.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        VerifyNum.setCancelable(true);
        VerifyNum.getWindow().getAttributes().windowAnimations = R.style.animation;

        UpdatePW = new Dialog(getActivity());
        UpdatePW.setContentView(R.layout.fragment_update_password);
        UpdatePW.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        UpdatePW.setCancelable(true);
        UpdatePW.getWindow().getAttributes().windowAnimations = R.style.animation;
        EditText etVerifyCode =  VerifyNum.findViewById(R.id.etVerify);

        btnVerify = VerifyNum.findViewById(R.id.btnVerify);
        forgotPassnext = ForgotPW.findViewById(R.id.forgotPassnext);
        numberUser = ForgotPW.findViewById(R.id.forgotNumber);
        updatePW = UpdatePW.findViewById(R.id.updatePW);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions);
        verifyEmail();



        btn_sign_with_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserLogin();
                if (TextUtils.isEmpty(login_editTxt_phoneNum.getEditText().getText().toString())) {
                    wrongNum.setVisibility(view.VISIBLE);
                    wrongNum.setText("Phone number is required.");

                }
                else {
                    wrongNum.setVisibility(view.GONE);
                }

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
                db = FirebaseDatabase.getInstance();
                root = db.getReference("users");
                String userNumF = getTextFromEditText(ForgotPW.findViewById(R.id.forgotNumber));
                Query accCheck = root.orderByChild("userPhone").equalTo(userNumF);
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
                    Toast.makeText(getActivity(),"Please Enter The Code.",Toast.LENGTH_SHORT).show();
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
                String userNumF = getTextFromEditText(ForgotPW.findViewById(R.id.forgotNumber));
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
                    pwfield.setError("Password must have at least 6 characters, one uppercase, lowercase, and number.", null);
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
                    HashMap userInfo = new HashMap();
                    userInfo.put("userPass",password);
                    root.child(userNumF).updateChildren(userInfo);
                    Toast.makeText(getActivity(),"Password Updated",Toast.LENGTH_SHORT).show();
                    numberUser.setText("");
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
        return view;

    }

    private void signIn(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                verifyEmail();
            } catch (ApiException e) {
                Toast.makeText(requireContext().getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

    }


    void navigateToSecondActivity(String userName, String userPhone){
        Intent intent = new Intent(getContext(), user_navigation.class);
        intent.putExtra("username", userName);
        intent.putExtra("phonenum", userPhone);
        startActivity(intent);
    }

    private void verifyEmail(){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(requireContext());
        if(acct!=null) {
            Log.e("Google2", acct.getEmail());
            String inEmail = acct.getEmail();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference dr = database.getReference().child("users");
            Query query = dr.orderByChild("userEmail").equalTo(inEmail);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.e("GetEmail", String.valueOf(dataSnapshot));
                    List<String> phone = new ArrayList<>(Collections.emptyList());
                    List<String> name = new ArrayList<>(Collections.emptyList());

                    for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                        if (Objects.requireNonNull(locationSnapshot.child("userEmail").getValue()).equals(inEmail)) {
                            String getPhone = Objects.requireNonNull(locationSnapshot.child("userPhone").getValue()).toString();
                            phone.add(getPhone);
                        }
                    }

                    for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                        if (Objects.requireNonNull(locationSnapshot.child("userEmail").getValue()).equals(inEmail)) {
                            String getName = Objects.requireNonNull(locationSnapshot.child("userFullname").getValue()).toString();
                            name.add(getName);
                        }
                    }

                    try {
                        navigateToSecondActivity(name.get(0), phone.get(0));
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Sign Up First", Toast.LENGTH_LONG).show();
                        googleSignInClient.signOut();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    };
    private void UserLogin(){

        String usernumEntered = login_editTxt_phoneNum.getEditText().getText().toString().trim();
        String userpassEntered = login_editTxt_password.getEditText().getText().toString().trim();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

        Query checkUser = ref.orderByChild("userPhone").equalTo(usernumEntered);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    login_editTxt_phoneNum.setError(null);
                    login_editTxt_phoneNum.setErrorEnabled(false);
                    String userpassDB = snapshot.child(usernumEntered).child("userPass").getValue(String.class);

                    if(userpassDB.equals(userpassEntered)){
                        login_editTxt_phoneNum.setError(null);
                        login_editTxt_phoneNum.setErrorEnabled(false);

                        String nameFromDB = snapshot.child(usernumEntered).child("userFullname").getValue(String.class);
                        String usernumFromDB = snapshot.child(usernumEntered).child("userPhone").getValue(String.class);

                        Dialog VerifyNum = new Dialog(getContext());
                        VerifyNum.setContentView(R.layout.fragment_user_phonenum_verify);
                        VerifyNum.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        VerifyNum.setCancelable(false);
                        VerifyNum.getWindow().getAttributes().windowAnimations = R.style.animation;
                        TextView name = VerifyNum.findViewById(R.id.user_Fullname);
                        TextView usernum = VerifyNum.findViewById(R.id.user_ContactNo);

                        name.setText(nameFromDB);
                        usernum.setText(usernumFromDB);

                        Intent intent = new Intent(getActivity(),user_permission.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("username", nameFromDB);
                        intent.putExtra("phonenum", usernumFromDB);
                        startActivity(intent);

                    }
                    else{

                        if (TextUtils.isEmpty(login_editTxt_password.getEditText().getText().toString())) {
                            wrongPass.setVisibility(View.VISIBLE);
                            wrongPass.setText("Password is required.");

                        }
                        else
                        {
                            wrongPass.setVisibility(View.VISIBLE);
                            wrongPass.setText("Password is incorrect.");
                        }
                    }
                }
                else{
                    wrongNum.setVisibility(View.VISIBLE);
                    wrongNum.setText("Account does not exist");
                    wrongPass.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void sendVerificationCodeToUser(String phoneNo)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+63" + phoneNo,60, TimeUnit.SECONDS, getActivity(),mCallBacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
    {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationCodeBySystem = s;
            Toast.makeText(getActivity(),"Code Sent.",Toast.LENGTH_SHORT).show();

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
            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
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
    public void onCancelled(@NonNull DatabaseError databaseError) { throw databaseError.toException(); }

}