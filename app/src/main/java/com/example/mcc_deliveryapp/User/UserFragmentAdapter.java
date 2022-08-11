package com.example.mcc_deliveryapp.User;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mcc_deliveryapp.Rider.cancelled_fragment;
import com.example.mcc_deliveryapp.Rider.completed_fragment;
import com.example.mcc_deliveryapp.User.user_cancelled_fragment;
import com.example.mcc_deliveryapp.User.user_completed_fragment;
import com.example.mcc_deliveryapp.User.user_pending_fragment;

public class UserFragmentAdapter extends FragmentStateAdapter {


    public UserFragmentAdapter(FragmentManager childFragmentManager, Lifecycle lifecycle) {
        super(childFragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1){
            return new user_completed_fragment();
        }
        else if (position == 2){
            return new user_cancelled_fragment();
        }
        else {
            return new user_pending_fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
