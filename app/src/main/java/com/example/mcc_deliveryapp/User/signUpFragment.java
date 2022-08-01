package com.example.mcc_deliveryapp.User;

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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class signUpFragment extends Fragment {
    //setting the value of the given edit_text
    EditText editTxt_fullname,editTxt_phoneNum,editTxt_password,editTxt_Cpassword;
    Button btn_createAcc;
    TextView txt_hashpassword;

    TextInputLayout textInputPassword;
    //Database Realtime
    FirebaseDatabase root;
    DatabaseReference DbRef;
    FirebaseAuth fAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        // Initialization of text
        txt_hashpassword =view.findViewById(R.id.Hashpassword);
        editTxt_fullname = view.findViewById(R.id.edTextUserName);
        editTxt_phoneNum = view.findViewById(R.id.edTextPhoneNo);
        editTxt_password = view.findViewById(R.id.edTextPass);
        editTxt_Cpassword = view.findViewById(R.id.edTextConfirmPass);
        btn_createAcc= view.findViewById(R.id.btn_createAccount);
        textInputPassword = view.findViewById(R.id.edTextPassL);
        btn_createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                calling the user password validation
//                Hashing the password and confirm password
//                PasswordHash(editTxt_password.toString());
//                PasswordHash(editTxt_Cpassword.toString());
                // Verification for blank space in the user registration form

                boolean clear = true;

                if(TextUtils.isEmpty(editTxt_fullname.getText().toString()))
                {
                    editTxt_fullname.setError("Required");
                    clear = false;
                    return;
                }
                else if(TextUtils.isEmpty(editTxt_phoneNum.getText().toString()))
                {
                    editTxt_phoneNum.setError("Required");
                    clear = false;
                    return;
                }
                else if(TextUtils.isEmpty(editTxt_password.getText().toString()))
                {
                    editTxt_password.setError("Required");
                    clear = false;
                    return;
                }
                else if(TextUtils.isEmpty(editTxt_Cpassword.getText().toString()))
                {
                    editTxt_Cpassword.setError("Required");
                    clear = false;
                    return;
                }

                else
                {
                    clear = true;
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
                    editTxt_password.setError("Password most have at least 6 characters, one uppercase, lowercase, and number.");

                }

                // add confirm password function
                else if (min6 && uppercase && lowercase && digits >=1)
                {

                    if (password.equals(pwConfirm))
                    {

                        PWgood = true;
                    }
                    else {
                        editTxt_Cpassword.setError("Passwords do not match");

                    }

                }


                if(PWgood && clear){
                    root = FirebaseDatabase.getInstance();
                    DbRef = root.getReference("users");
                    // Getting the value of The given info in sign up to store in firebase
                    String fullname = editTxt_fullname.getEditableText().toString();
                    String phoneNum = editTxt_phoneNum.getEditableText().toString();
                    String pass = editTxt_password.getEditableText().toString();
                    String Cpass = editTxt_Cpassword.getEditableText().toString();
                    String passwordhash = txt_hashpassword.getText().toString();

                    // To check if the user already exists
                    Query accCheck = DbRef.orderByChild("userPhone").equalTo(phoneNum);
                    accCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Toast.makeText(getContext(), "Account Already Exists. Please Sign In", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                    //user helper class in order to store the the given info in sign up form
                                    UserHelperClass userHelperClass = new UserHelperClass(fullname, phoneNum, pass, Cpass);
                                    DbRef.child(phoneNum).setValue(userHelperClass);
                                    Toast.makeText(getContext(), "Account Successfully Created", Toast.LENGTH_SHORT).show();
                                    //Implementing the Clear Section in Sign up after the Creation of Account
                                    Clear();

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

    // Password and Confirm Password Hashing
    public void PasswordHash(String password){
        try{
            //Create hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer MD5Hash = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
            {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length()< 2 )
                    h = "0" + h;
                MD5Hash.append(h);
            }
            txt_hashpassword.setText(MD5Hash);

        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    }
}