package com.example.vuphu.newlaundry.Main.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vuphu.newlaundry.Authen.LoginActivity;
import com.example.vuphu.newlaundry.Popup.Popup;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private FloatingActionButton logOut;
    private Popup popup;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_account, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        logOut = v.findViewById(R.id.btn_logout);
        popup = new Popup(getActivity());
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.createLoadingDialog();
                popup.show();
                logOut();
            }
        });
    }

    private void logOut() {
        PreferenceUtil.setPreferenceNull(getContext());
        popup.hide();
        getActivity().startActivity(new Intent(getContext(), LoginActivity.class));

        getActivity().finish();
    }

    public static AccountFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
