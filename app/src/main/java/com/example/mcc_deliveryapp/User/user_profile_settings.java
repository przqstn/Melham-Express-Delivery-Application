package com.example.mcc_deliveryapp.User;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.mcc_deliveryapp.MainActivity2;
import com.example.mcc_deliveryapp.R;
import com.example.mcc_deliveryapp.Rider.riderLogin;
import com.example.mcc_deliveryapp.Rider.riderReportPage;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class user_profile_settings extends DialogFragment {
    TextView changePassword, editProfile, logout, faqs, reportBug;

    private GoogleSignInClient googlesignInClient;
    private GoogleSignInOptions googlesignInOptions;
    private View view;

    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.settings_panel, container, false);

        googlesignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googlesignInClient = GoogleSignIn.getClient(requireActivity(), googlesignInOptions);
        changePassword = view.findViewById(R.id.txt_changePW);
        editProfile = view.findViewById(R.id.txt_editProfile);
        logout = view.findViewById(R.id.txt_logout);
        faqs = view.findViewById(R.id.txt_faqs);
        reportBug = view.findViewById(R.id.txt_reportBug);

        Bundle mArgs =getArguments();
        String phoneNum = mArgs.getString("userPhone");

        sharedPreferences = getActivity().getSharedPreferences("autoLogin", Context.MODE_PRIVATE);

        Window window = getDialog().getWindow();
        // To make popup fully transparent
        window.setBackgroundDrawableResource(R.color.color_transparent);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        // Gravity with X and Y position of settings popup
        window.setGravity(Gravity.TOP|Gravity.RIGHT);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 50;
        params.y = 160;
        window.setAttributes(params);

        // Popup dismisses when clicked outside
        getDialog().setCanceledOnTouchOutside(true);


        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goChangepass = new Intent(getContext(), user_editprofile_changePass.class);
                getContext().startActivity(goChangepass);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goEditProfile = new Intent(getContext(), user_editprofile_fragment.class);
                goEditProfile.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                goEditProfile.putExtra("userPhone",phoneNum);
                getContext().startActivity(goEditProfile);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("key", 0);
                editor.apply();
                Intent activity = new Intent(getContext(), MainUserActivity.class);

                leavePage();
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
                Intent goReportBug = new Intent(getContext(), user_reportBug.class);
                getContext().startActivity(goReportBug);
            }
        });

        return view;
    }

    void leavePage(){
        googlesignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                Intent goExit = new Intent(getContext(), MainActivity2.class);
                goExit.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(goExit);
            }
        });
    }



}
