package com.example.mcc_deliveryapp.Rider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mcc_deliveryapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class editprofile_changePass extends AppCompatActivity {

    private EditText oldPass, newPass, confirmPass;
    private TextView forgotPass;
    private ImageButton btnSaveChanges;
    private String riderpass, phoneNum;

    private DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        Intent intent=getIntent();
        phoneNum=intent.getStringExtra("riderphone");

        btnSaveChanges=findViewById(R.id.btn_saveChanges);
        oldPass=findViewById(R.id.enter_oldPW);
        newPass=findViewById(R.id.enter_newPW);
        confirmPass=findViewById(R.id.confirm_newPW);
        forgotPass=findViewById(R.id.forgotPass);

        oldPass.addTextChangedListener(new GenericTextWatcher(oldPass));
        newPass.addTextChangedListener(new GenericTextWatcher(newPass));
        confirmPass.addTextChangedListener(new GenericTextWatcher(confirmPass));

        root = FirebaseDatabase.getInstance().getReference("riders");
        Query check=root.orderByChild("riderphone").equalTo(phoneNum);




        btnSaveChanges.setVisibility(View.GONE);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            riderpass=snapshot.child(phoneNum).child("riderpass").getValue(String.class);
                            if(oldPass.getEditableText().toString().equals(riderpass)){
                                String password = newPass.getEditableText().toString();
                                String pwConfirm = confirmPass.getEditableText().toString();
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
                                    newPass.setError("Password most have at least 6 characters, one uppercase, lowercase, and number.");
                                    newPass.setBackgroundResource(R.drawable.error_border_edittext);


                                }

                                else if (min6 && uppercase && lowercase && digits >=1)
                                {

                                    if (password.equals(pwConfirm))
                                    {
                                        newPass.setBackgroundResource(R.drawable.graphics_edittext_1);
                                        confirmPass.setBackgroundResource(R.drawable.graphics_edittext_1);
                                        PWgood = true;
                                    }
                                    else {
                                        confirmPass.setError("Passwords do not match");
                                        newPass.setBackgroundResource(R.drawable.error_border_edittext);
                                        confirmPass.setBackgroundResource(R.drawable.error_border_edittext);
                                    }

                                }

                                if (PWgood)
                                {
                                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                                    DatabaseReference rootie = db.getReference("riders");
                                    FirebaseUser userCurrent = mAuth.getCurrentUser();

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(String.valueOf(phoneNum)).build();
                                    userCurrent.updateProfile(profileUpdates);
                                    rootie = db.getReference();

                                    HashMap riderInfo = new HashMap();
                                    riderInfo.put("riderpass",password);
                                    rootie.child("riders").child(phoneNum).updateChildren(riderInfo);
                                    Toast.makeText(editprofile_changePass.this,"Password Updated",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        });
    }
    private class GenericTextWatcher implements TextWatcher{

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
}
