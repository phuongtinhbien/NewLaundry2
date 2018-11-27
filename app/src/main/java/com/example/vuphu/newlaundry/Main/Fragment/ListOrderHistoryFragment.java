package com.example.vuphu.newlaundry.Main.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.GetOrderFinishQuery;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.Main.Adapter.AdapterListOrder;
import com.example.vuphu.newlaundry.Main.OBOrderFragment;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListOrderHistoryFragment extends Fragment{
    private RecyclerView listOrderFinish;
    private ArrayList<OBOrderFragment> list;
    private AdapterListOrder adapterListOrder;
    private String token;
    private String idUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_order_history, container, false);
        addControls(v);
        getListOrderFromServer();
        return v;
    }

    private void getListOrderFromServer() {
        token = PreferenceUtil.getAuthToken(getActivity());
        idUser = PreferenceUtil.getIdUser(getActivity());
        GraphqlClient.getApolloClient(token, false).query(GetOrderFinishQuery.builder()
                .id(idUser)
                .build()
        ).enqueue(new ApolloCall.Callback<GetOrderFinishQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetOrderFinishQuery.Data> response) {
                List<GetOrderFinishQuery.Node> nodes = response.data().allCustomerOrders().nodes();
                if(nodes != null){
                    for(GetOrderFinishQuery.Node node : nodes) {
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
                    }
                });

            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e("getOrderHistory", e.getCause() +" - "+e);
            }
        });
    }

    private void initializeListOrder() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listOrderFinish.setLayoutManager(linearLayoutManager);
        adapterListOrder = new AdapterListOrder(getActivity(), list);
        listOrderFinish.setAdapter(adapterListOrder);
    }

    private void addControls(View v) {
        listOrderFinish = v.findViewById(R.id.list_order_history);
        list = new ArrayList<>();
    }
}
