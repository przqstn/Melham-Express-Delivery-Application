package com.example.mcc_deliveryapp.Rider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mcc_deliveryapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class riderLogin extends AppCompatActivity {

    EditText numberRider,passRider;
    Button btnLogin;
    Button btnRegister;
    FirebaseAuth mAuth;
    FirebaseDatabase db = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_login);

        numberRider = (EditText) findViewById(R.id.edtTextRiderNumber);
        passRider = (EditText) findViewById(R.id.edtTextRiderPassword);
        btnLogin = findViewById(R.id.btnLoginRider);
        btnRegister = findViewById(R.id.btnRegisterRider);
        mAuth = FirebaseAuth.getInstance();

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


    }

    private void loginRider()
    {
        String number = numberRider.getText().toString();
        String pass = passRider.getText().toString();


        if(TextUtils.isEmpty(number))
        {
            numberRider.setError("Number Required");
            numberRider.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(pass))
        {
            passRider.setError("Password Required");
            passRider.requestFocus();
            return;
        }
        String riderNumEntered = numberRider.getText().toString().trim();
        String riderpassEntered = passRider.getText().toString().trim();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("riders");
        Query checkRider = ref.orderByChild("riderphone").equalTo(riderNumEntered);
        checkRider.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//get credentials of rider using OTP same as user
                if (snapshot.exists()) {
                    numberRider.setError(null);
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
                        passRider.setError("Wrong Password");
                        }


                }

                else{
                numberRider.setError("This number is not registered yet");
            }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }
}
