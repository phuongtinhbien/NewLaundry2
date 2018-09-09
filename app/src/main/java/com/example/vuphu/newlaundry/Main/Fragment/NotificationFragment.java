package com.example.vuphu.newlaundry.Main.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vuphu.newlaundry.R;


public class NotificationFragment extends Fragment {
   
    public NotificationFragment() {
        // Required empty public constructor
    }

    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    public static NotificationFragment newInstance() {
        
        Bundle args = new Bundle();
        
        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

   
}
