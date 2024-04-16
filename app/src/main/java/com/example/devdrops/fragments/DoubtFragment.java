package com.example.devdrops.fragments;

import static android.app.PendingIntent.getActivity;

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
    ViewPager2 viewPager2;
    TabLayout tabLayout;


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
        viewPager2 = rootview.findViewById(R.id.viewPager);
        tabLayout = rootview.findViewById(R.id.tabLayout);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new QuestionsFragment());
        fragments.add(new MyQueriesFragment());


        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getActivity(), fragments);
        viewPager2.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText(getTabTitle(position))
        ).attach();
        return rootview;
    }

    private String getTabTitle(int position) {
        if (position == 0){
            return "Queries";
        }  else {
            return "My Queries";
        }
    }

}