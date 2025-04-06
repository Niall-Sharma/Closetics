package com.example.closetics.follow;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.closetics.ProfileFragment;
import com.example.closetics.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class FollowFragment extends Fragment {

    private static final String ARG_IS_FOLLOWING = "isFollowing";
    private boolean isFollowing;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private FollowTabAdapter tabAdapter;

    public FollowFragment() {
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_FOLLOWING, true);
        setArguments(args);
    }

    public FollowFragment(boolean isFollowing) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_FOLLOWING, isFollowing);
        setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isFollowing = getArguments().getBoolean(ARG_IS_FOLLOWING);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_follow, container, false);

        // override default back behavior
//        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
////                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav_view);
////                bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
//                Navigation.findNavController(view).navigate(R.id.action_navigation_follow_to_navigation_profile);
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        // initialize ui
        tabLayout = view.findViewById(R.id.follow_tab_layout);
        viewPager2 = view.findViewById(R.id.follow_viewPager2);
        tabAdapter = new FollowTabAdapter(getActivity());
        viewPager2.setAdapter(tabAdapter);

        // set listeners
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        // switch to Followers tab by default if needed
        if (!isFollowing) {
            tabLayout.getTabAt(1).select();
        }

        return view;
    }

}