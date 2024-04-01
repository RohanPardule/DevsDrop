package com.example.devdrops.fragments;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.devdrops.R;
import com.example.devdrops.adapter.MyPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;


public class DoubtFragment extends Fragment {


    public DoubtFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_doubt, container, false);
        ViewPager2 viewPager = rootview.findViewById(R.id.viewPager);
        TabLayout tabLayout = rootview.findViewById(R.id.tabLayout);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new QuestionsFragment());
        fragments.add(new QuestionsFragment());
        fragments.add(new QuestionsFragment());

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(requireActivity(), fragments);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(getTabTitle(position))
        ).attach();

        TabLayout.Tab tab = tabLayout.getTabAt(0);
        if (tab != null) {
            tab.select();
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabLayout.setTabTextColors(ContextCompat.getColor(getContext(), R.color.gray), ContextCompat.getColor(getContext(), R.color.midnight_blue));
                tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.midnight_blue));

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabLayout.setTabTextColors(ContextCompat.getColor(getContext(), R.color.midnight_blue), ContextCompat.getColor(getContext(), R.color.midnight_blue));}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return rootview;
    }

    private String getTabTitle(int position) {
        switch (position) {
            case 0:
                return "Queries";
            case 1:
                return "Forum";
            case 2:
                return "My queries";
            default:
                return null;
        }
    }
}