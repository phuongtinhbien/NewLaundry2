package com.example.vuphu.newlaundry.Main.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vuphu.newlaundry.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    public static AccountFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
