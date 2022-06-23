package com.example.mcc_deliveryapp.Rider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mcc_deliveryapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class riderLogin extends AppCompatActivity {

    EditText emailRider,passRider;
    Button btnLogin;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_login);

        emailRider = findViewById(R.id.edtTextRiderNumber);
        passRider = findViewById(R.id.edtTextRiderPassword);
        btnLogin = findViewById(R.id.btnLoginRider);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginRider();
            }
        });



    }

    private void loginRider()
    {
        String email = emailRider.getText().toString();
        String pass = passRider.getText().toString();


        if(TextUtils.isEmpty(email))
        {
            emailRider.setError("Email Required");
            emailRider.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(pass))
        {
            passRider.setError("Email Required");
            passRider.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Intent intent = new Intent(riderLogin.this, MainActivityRider.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(riderLogin.this,task.getException().toString(),Toast.LENGTH_LONG).show();
                }
            }
        });



    }


}