package com.example.mcc_deliveryapp.User;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.vcn.VcnUnderlyingNetworkTemplate;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.mcc_deliveryapp.R;

public class user_profile_settings extends DialogFragment {
    TextView changePassword, editProfile, logout, faqs, reportBug;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.settings_panel, container, false);

        changePassword = view.findViewById(R.id.txt_changePW);
        editProfile = view.findViewById(R.id.txt_editProfile);
        logout = view.findViewById(R.id.txt_logout);
        faqs = view.findViewById(R.id.txt_faqs);
        reportBug = view.findViewById(R.id.txt_reportBug);

        Window window = getDialog().getWindow();
        // To make popup fully transparent
        window.setBackgroundDrawableResource(R.color.color_transparent);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        // Gravity with X and Y position of settings popup
        window.setGravity(Gravity.TOP|Gravity.RIGHT);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 50;
        params.y = 170;
        window.setAttributes(params);

        // Popup dismisses when clicked outside
        getDialog().setCanceledOnTouchOutside(true);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // COPY ONCLICK LISTENER OF REPORT BUG (except if class is fragment)
                // THEN, DELETE TOAST AND COMMENTS
                Toast.makeText(getActivity(), "Change Password", Toast.LENGTH_SHORT).show();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // COPY ONCLICK LISTENER OF REPORT BUG (except if class is fragment)
                // THEN, DELETE TOAST AND COMMENTS
                Toast.makeText(getActivity(), "Edit Profile", Toast.LENGTH_SHORT).show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // COPY ONCLICK LISTENER OF REPORT BUG (except if class is fragment)
                // THEN, DELETE TOAST AND COMMENTS
                Toast.makeText(getActivity(), "Logout", Toast.LENGTH_SHORT).show();
            }
        });

        faqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goFAQs = new Intent(getContext(), frequentlyAskedQuestions.class);
                getContext().startActivity(goFAQs);
            }
        });

        reportBug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), user_reportBug.class);
                getContext().startActivity(intent);
            }
        });

        return view;
    }

}
