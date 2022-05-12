package com.example.mcc_deliveryapp.User;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mcc_deliveryapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signUpFragment extends Fragment {
    //~~~~~~~~~~~~~~NEED TO PUSH AND COMMIT~~~~~~~~~~~~~//

    //setting the value of the given edit_text
    EditText editTxt_fullname,editTxt_phoneNum,editTxt_password,editTxt_Cpassword;
    Button btn_createAcc;

   //Database Realtime
    FirebaseDatabase root;
    DatabaseReference DbRef;
    FirebaseAuth fAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        // initialization of text
        editTxt_fullname = view.findViewById(R.id.edTextUserName);
        editTxt_phoneNum = view.findViewById(R.id.edTextPhoneNo);
        editTxt_password = view.findViewById(R.id.edTextPass);
        editTxt_Cpassword = view.findViewById(R.id.edTextConfirmPass);
        btn_createAcc= view.findViewById(R.id.btn_createAccount);

        btn_createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verification for blank space in the user registration form
                if(TextUtils.isEmpty(editTxt_fullname.getText().toString()))
                {
                    editTxt_fullname.setError("Required");
                    Toast.makeText(getContext(),"Full name is Required",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(editTxt_phoneNum.getText().toString()))
                {
                    editTxt_phoneNum.setError("Required");
                    Toast.makeText(getContext(),"Number is required",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(editTxt_password.getText().toString()))
                {
                    editTxt_password.setError("Required");
                    Toast.makeText(getContext(),"Password is required",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(editTxt_Cpassword.getText().toString()))
                {
                    editTxt_Cpassword.setError("Required");
                    Toast.makeText(getContext(),"Full name is Required",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!editTxt_password.equals(editTxt_Cpassword)){
                    editTxt_Cpassword.setError("Password not match");
                    return;
                }

                root = FirebaseDatabase.getInstance();
                DbRef = root.getReference("User Registration");

                //Getting the value of The given info in sign up to store in firebase
                String fullname = editTxt_fullname.getEditableText().toString();
                String phoneNum = editTxt_phoneNum.getEditableText().toString();
                String pass = editTxt_password.getEditableText().toString();
                String Cpass = editTxt_Cpassword.getEditableText().toString();

                //user helper class in order to store the the given info in sign up form
                UserHelperClass userHelperClass = new UserHelperClass(fullname,phoneNum,pass,Cpass);

                DbRef.child(fullname).setValue(userHelperClass);
            }
        });



        return view;
    }
}