package com.example.mcc_deliveryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterRider extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_rider);


        Spinner spinCity = findViewById(R.id.spinnerCityDriver);
        Spinner spinVehicle = findViewById(R.id.spinnerVehicleDriver);
        final List<String> cityList = new ArrayList<String>(R.array.cityRider);
        ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(this,R.layout.spinner_items_1,cityList)
        {
            @Override
            public boolean isEnabled(int position)
            {
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getDropDownView(position,convertView,parent);
                TextView tv = (TextView) view;
                if(position == 0 )
                {
                    tv.setTextColor(Color.GRAY);
                }
                else
                {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }

        };
        final List<String> VehicleList = new ArrayList<String>(R.array.ridervehicletype);
        ArrayAdapter<String> adapterVehicle = new ArrayAdapter<String>(this,R.layout.spinner_items_1,VehicleList)
        {
            @Override
            public boolean isEnabled(int position)
            {
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getDropDownView(position,convertView,parent);
                TextView tv = (TextView) view;
                if(position == 0 )
                {
                    tv.setTextColor(Color.GRAY);
                }
                else
                {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }

        };
        adapterCity.setDropDownViewResource(R.layout.spinner_items_1);
        adapterVehicle.setDropDownViewResource(R.layout.spinner_items_1);
        spinVehicle.setAdapter(adapterVehicle);
        spinCity.setAdapter(adapterCity);

        spinCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItemText = (String) adapterView.getItemAtPosition(i);
                if(i > 0){
                    Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItemText = (String) adapterView.getItemAtPosition(i);
                if(i > 0){
                    Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }
}