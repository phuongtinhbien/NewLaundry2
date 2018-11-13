package com.example.vuphu.newlaundry.Main.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.vuphu.newlaundry.Main.Fragment.ListOrderActiveFragment;
import com.example.vuphu.newlaundry.Main.Fragment.ListOrderHistoryFragment;
import com.example.vuphu.newlaundry.Main.Fragment.OrderFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int mNumOfTabs) {
        super(fm);
        this.mNumOfTabs = mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag=null;
        switch (position){
            case 0:
                frag = new ListOrderActiveFragment();
                break;
            case 1:
                frag = new ListOrderHistoryFragment();
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
