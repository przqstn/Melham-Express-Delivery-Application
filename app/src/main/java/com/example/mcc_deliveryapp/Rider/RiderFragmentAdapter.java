package com.example.mcc_deliveryapp.Rider;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mcc_deliveryapp.User.signInFragment;
import com.example.mcc_deliveryapp.User.signUpFragment;

public class RiderFragmentAdapter extends FragmentStateAdapter {


    public RiderFragmentAdapter(FragmentManager childFragmentManager, Lifecycle lifecycle) {
        super(childFragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1){
            return new cancelled_fragment();

        }
        return new completed_fragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
