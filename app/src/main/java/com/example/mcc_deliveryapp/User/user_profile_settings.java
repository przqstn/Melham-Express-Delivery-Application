package com.example.mcc_deliveryapp.User;

import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mcc_deliveryapp.R;

public class user_profile_settings extends AppCompatActivity {
    TextView changePassword, editProfile, logout, faqs, reportBug;

    public void showPopupSettings(final View view) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.settings_panel, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        boolean focus = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focus);

        // Settings panel shows in upper-right corner
        popupWindow.showAtLocation(view, Gravity.TOP | Gravity.RIGHT, 50, 260);

        changePassword = popupView.findViewById(R.id.txt_changePW);
        editProfile = popupView.findViewById(R.id.txt_editProfile);
        logout = popupView.findViewById(R.id.txt_logout);
        faqs = popupView.findViewById(R.id.txt_faqs);
        reportBug = popupView.findViewById(R.id.txt_reportBug);


        // TODO: Set onClick to activities
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove toast then add activity
                Toast.makeText(view.getContext(), "Change Password", Toast.LENGTH_SHORT).show();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove toast then add activity
                Toast.makeText(view.getContext(), "Edit Profile", Toast.LENGTH_SHORT).show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove toast then add activity
                Toast.makeText(view.getContext(), "Logout", Toast.LENGTH_SHORT).show();
            }
        });

        faqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove toast then add activity
                Toast.makeText(view.getContext(), "FAQs", Toast.LENGTH_SHORT).show();
            }
        });

        reportBug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove toast then add activity
                Toast.makeText(view.getContext(), "Report A Bug", Toast.LENGTH_SHORT).show();
            }
        });

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}
