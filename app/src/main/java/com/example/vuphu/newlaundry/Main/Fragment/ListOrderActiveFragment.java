package com.example.vuphu.newlaundry.Main.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.GetOrderActiveQuery;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.Main.Adapter.AdapterListOrder;
import com.example.vuphu.newlaundry.Main.OBOrderFragment;
import com.example.vuphu.newlaundry.Popup.Popup;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;

public class ListOrderActiveFragment extends Fragment{
    private RecyclerView listOrderActive;
    private ArrayList<OBOrderFragment> list;
    private AdapterListOrder adapterListOrder;
    private String token;
    private String idUser;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Popup popup;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_order_active, container, false);
        addControls(v);
        getListOrderFromServer();
        return v;
    }

    private void getListOrderFromServer() {
        list.clear();
        popup.createLoadingDialog();
        popup.show();
        token = PreferenceUtil.getAuthToken(getActivity());
        idUser = PreferenceUtil.getIdUser(getActivity());
        GraphqlClient.getApolloClient(token, false).query(GetOrderActiveQuery.builder()
                .id(idUser)
                .build()
        ).enqueue(new ApolloCall.Callback<GetOrderActiveQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetOrderActiveQuery.Data> response) {
                List<GetOrderActiveQuery.Node> nodes = response.data().allCustomerOrders().nodes();
                if(nodes != null){
                    for(GetOrderActiveQuery.Node node : nodes) {
                        OBOrderFragment obOrderFragment = new OBOrderFragment();
                        obOrderFragment.setId(node.id());
                        obOrderFragment.setDate(node.createDate());
                        obOrderFragment.setBranchAddress(node.branchByBranchId().address());
                        obOrderFragment.setBranchName(node.branchByBranchId().branchName());
                        obOrderFragment.setIdBranch(node.branchByBranchId().id());
                        if(node.receiptsByOrderId().nodes().size() > 0) {
                            obOrderFragment.setReciever(node.receiptsByOrderId().nodes().get(0).staffByStaffPickUp().fullName());
                        }
                        obOrderFragment.setStatus(node.status());
                        list.add(obOrderFragment);
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initializeListOrder();
                        popup.hide();
                    }
                });

            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e("getOrderActive", e.getCause() +" - "+e);
            }
        });
    }

    private void initializeListOrder() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listOrderActive.setLayoutManager(linearLayoutManager);
        adapterListOrder = new AdapterListOrder(getActivity(), list);
        listOrderActive.setAdapter(adapterListOrder);
    }

    private void addControls(View v) {
        swipeRefreshLayout = v.findViewById(R.id.swipe_list_order_active);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("RefreshClick", "Yes");
                refresh();
            }
        });
        listOrderActive = v.findViewById(R.id.list_order_active);
        list = new ArrayList<>();
        popup = new Popup(getActivity());
    }

    private void refresh() {
        list.clear();
        token = PreferenceUtil.getAuthToken(getActivity());
        idUser = PreferenceUtil.getIdUser(getActivity());
        ArrayList<OBOrderFragment> listRefresh = new ArrayList<>();
        GraphqlClient.getApolloClient(token, false).query(GetOrderActiveQuery.builder()
                .id(idUser)
                .build()
        ).enqueue(new ApolloCall.Callback<GetOrderActiveQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetOrderActiveQuery.Data> response) {
                List<GetOrderActiveQuery.Node> nodes = response.data().allCustomerOrders().nodes();
                if(nodes != null){
                    for(GetOrderActiveQuery.Node node : nodes) {
                        OBOrderFragment obOrderFragment = new OBOrderFragment();
                        obOrderFragment.setId(node.id());
                        obOrderFragment.setDate(node.createDate());
                        obOrderFragment.setBranchAddress(node.branchByBranchId().address());
                        obOrderFragment.setBranchName(node.branchByBranchId().branchName());
                        obOrderFragment.setIdBranch(node.branchByBranchId().id());
                        if(node.receiptsByOrderId().nodes().size() > 0) {
                            obOrderFragment.setReciever(node.receiptsByOrderId().nodes().get(0).staffByStaffPickUp().fullName());
                        }
                        obOrderFragment.setStatus(node.status());
                        listRefresh.add(obOrderFragment);
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        if(!listRefresh.isEmpty() && adapterListOrder != null) {
                            adapterListOrder.refreshAdapter(listRefresh);
                        }
                    }
                });

            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e("getOrderActive", e.getCause() +" - "+e);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

}
