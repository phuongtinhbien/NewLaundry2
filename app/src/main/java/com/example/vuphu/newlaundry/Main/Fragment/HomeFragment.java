package com.example.vuphu.newlaundry.Main.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.vuphu.newlaundry.Clothes.ClothesActivity;
import com.example.vuphu.newlaundry.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private MaterialSearchView searchView;
    private Button placeAnOrder;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home, container, false);
        placeAnOrder = v.findViewById(R.id.place_an_order);
        placeAnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ClothesActivity.class));
            }
        });
        return v;
    }

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_search_action);
        searchView.setMenuItem(item);
    }
}
