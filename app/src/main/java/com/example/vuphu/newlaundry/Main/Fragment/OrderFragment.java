package com.example.vuphu.newlaundry.Main.Fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vuphu.newlaundry.Main.Adapter.PagerAdapter;
import com.example.vuphu.newlaundry.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_order, container, false);
        tabLayout = v.findViewById(R.id.tab_order);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_order_active));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_order_history));
        viewPager = v.findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagerAdapter(getFragmentManager(), tabLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return v;
    }

    public static OrderFragment newInstance() {
        
        Bundle args = new Bundle();
        
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
