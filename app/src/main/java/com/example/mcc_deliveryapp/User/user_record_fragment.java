package com.example.mcc_deliveryapp.User;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mcc_deliveryapp.R;
import com.google.android.material.tabs.TabLayout;

public class user_record_fragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private String userNum, userName;
    UserFragmentAdapter useradapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        userNum = intent.getStringExtra("phonenum");
        userName = intent.getStringExtra("username");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_record_fragment, container, false);
        tabLayout = view.findViewById(R.id.userRecordTab);
        viewPager2 = view.findViewById(R.id.viewPager2);

//		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        useradapter = new UserFragmentAdapter(getChildFragmentManager(), getLifecycle());
        viewPager2.setAdapter(useradapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
                viewPager2.setEnabled(false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {

                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        return view;
    }
}