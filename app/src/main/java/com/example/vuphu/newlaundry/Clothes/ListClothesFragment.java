package com.example.vuphu.newlaundry.Clothes;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vuphu.newlaundry.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListClothesFragment extends Fragment {


    public ListClothesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_clothes, container, false);
    }

    public static ListClothesFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ListClothesFragment fragment = new ListClothesFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
