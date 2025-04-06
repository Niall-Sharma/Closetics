package com.example.closetics.follow;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FollowTabAdapter extends FragmentStateAdapter {
    public FollowTabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new FollowingTabFragment();
            case 1: return new FollowersTabFragment();
            default: return new FollowingTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
