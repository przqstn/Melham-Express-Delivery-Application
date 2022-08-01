package com.example.mcc_deliveryapp.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mcc_deliveryapp.R;

public class user_home extends AppCompatActivity {
    String name,phonenum;
    TextView welcomeText;
    Button bookOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Intent intent = getIntent();
        name = intent.getStringExtra("username");
        phonenum = intent.getStringExtra("phonenum");
        welcomeText = findViewById(R.id.welcomeText);
        bookOrder = findViewById(R.id.bookOrder);


        welcomeText.setText("Hi, "+ name + "!");


        bookOrder.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
             Intent intent = new Intent(user_home.this,user_parceltransaction.class);
             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
             intent.putExtra("phonenum", phonenum);
             intent.putExtra("username", name);
             startActivity(intent);
              }
           }

        );




    }


}