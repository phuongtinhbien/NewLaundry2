package com.example.vuphu.newlaundry.Main.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.ChooseUnitActivity;
import com.example.vuphu.newlaundry.GetServiceTypesQuery;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.Order.Activity.PrepareOrderActivity;
import com.example.vuphu.newlaundry.Popup.Popup;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Service.ListServiceAdapter;
import com.example.vuphu.newlaundry.Service.OBService;
import com.example.vuphu.newlaundry.Service.iFService;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements iFService {

    private RecyclerView recyclerViewService;
    private ArrayList<OBService> listService;
    private ListServiceAdapter listServiceAdapter;
    private Button placeAnOrder;
    private Popup popup;
    private String token;
    private GetServiceTypesQuery.AllServiceTypes allServiceTypes;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home, container, false);
        token = PreferenceUtil.getAuthToken(getActivity());
        fetchData();
        recyclerViewService = v.findViewById(R.id.list_service);
        listService = new ArrayList<>();
//
        placeAnOrder = v.findViewById(R.id.place_an_order);
        placeAnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PrepareOrderActivity.class));
            }
        });
        return v;
    }

    private void fetchData() {
        GraphqlClient.getApolloClient(token, false).query(GetServiceTypesQuery.builder().build())
                .enqueue(new ApolloCall.Callback<GetServiceTypesQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<GetServiceTypesQuery.Data> response) {
                        List<GetServiceTypesQuery.Node> list = response.data().allServiceTypes().nodes();
                        for (GetServiceTypesQuery.Node node : list) {
                            listService.add(new OBService(node.id(), node.serviceTypeName(), node.serviceTypeDesc(), node.postByServiceTypeAvatar().headerImageFile()));
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initRecycleView();
                            }
                        });

                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.e("getServiceType", e.getCause() +" - "+e);
                    }
                });
    }

    private void initRecycleView() {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewService.setLayoutManager(gridLayoutManager);
        listServiceAdapter = new ListServiceAdapter(listService, getActivity(), this);
        recyclerViewService.setAdapter(listServiceAdapter);
    }

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void itemClick(int pos) {
        Intent intent = new Intent(getActivity(), ChooseUnitActivity.class);
        intent.putExtra("idService", listService.get(pos).getId());
        intent.putExtra("NameService", listService.get(pos).getName());
        startActivity(intent);
    }

    @Override
    public void detail(String mes) {
        popup = new Popup(getActivity());
        popup.createInfoPopup(mes);
        popup.show();
    }
}
