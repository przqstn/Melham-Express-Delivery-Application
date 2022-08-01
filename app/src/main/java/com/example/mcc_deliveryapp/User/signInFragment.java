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

import com.example.mcc_deliveryapp.R;
import com.example.mcc_deliveryapp.Rider.riderLogin;
import com.example.mcc_deliveryapp.Rider.rider_dashboard;
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

import java.util.concurrent.TimeUnit;


public class signInFragment extends Fragment {
    FirebaseAuth mAuth;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference root;


    TextInputLayout login_editTxt_phoneNum, login_editTxt_password;
    Button btn_Login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        login_editTxt_phoneNum = view.findViewById(R.id.edTextPhoneNo);
        login_editTxt_password = view.findViewById(R.id.edTextPass);
        btn_Login = view.findViewById(R.id.btn_login);



        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserLogin();
                if (TextUtils.isEmpty(login_editTxt_phoneNum.getEditText().getText().toString())) {
                    login_editTxt_phoneNum.setError("Required");
                    Toast.makeText(getContext(), "Number is required", Toast.LENGTH_SHORT).show();

                }
                if (TextUtils.isEmpty(login_editTxt_password.getEditText().getText().toString())) {
                    login_editTxt_password.setError("Required");
                    Toast.makeText(getContext(), "Password is required", Toast.LENGTH_SHORT).show();

                }
//                name.setText("");
//                usernum.setText("");
            }

        });
        return view;

    }
    private void UserLogin(){

        String usernumEntered = login_editTxt_phoneNum.getEditText().getText().toString().trim();
        String userpassEntered = login_editTxt_password.getEditText().getText().toString().trim();
        //Bundle bundle = new Bundle();

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
                        String passFromDB =snapshot.child(usernumEntered).child("userPass").getValue(String.class);
                        String CpassFromDB = snapshot.child(usernumEntered).child("userCPass").getValue(String.class);

                        Dialog VerifyNum = new Dialog(getContext());
                        VerifyNum.setContentView(R.layout.fragment_user_phonenum_verify);
                        VerifyNum.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        VerifyNum.setCancelable(false);
                        VerifyNum.getWindow().getAttributes().windowAnimations = R.style.animation;
                        TextView name = (TextView) VerifyNum.findViewById(R.id.user_Fullname);
                        TextView usernum = (TextView) VerifyNum.findViewById(R.id.user_ContactNo);

                        name.setText(nameFromDB);
                        usernum.setText(usernumFromDB);

                        Intent intent = new Intent(getActivity(),user_permission.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("username", nameFromDB);
                        intent.putExtra("phonenum", usernumFromDB);
                        startActivity(intent);

                    }
                    else{
                        login_editTxt_password.setError("Wrong Password");

                    }
                }
                else{
                    login_editTxt_phoneNum.setError("This number is not registered yet");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}