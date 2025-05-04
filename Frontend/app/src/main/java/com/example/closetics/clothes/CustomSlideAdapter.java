package com.example.closetics.clothes;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.HashMap;
import java.util.Map;

public interface CustomSlideAdapter {


    public final Map<Integer, Fragment> fragmentMap = new HashMap<>();

    public Fragment getFragment(int position);
}
