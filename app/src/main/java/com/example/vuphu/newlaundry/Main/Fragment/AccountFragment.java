package com.example.vuphu.newlaundry.Main.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.Authen.LoginActivity;
import com.example.vuphu.newlaundry.Authen.SetUpInfoActivity;
import com.example.vuphu.newlaundry.CurrentUserQuery;
import com.example.vuphu.newlaundry.GetCustomerQuery;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.Popup.Popup;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;

import org.jetbrains.annotations.NotNull;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private FloatingActionButton logOut;
    private Popup popup;
    private TextView name, email;
    private static CurrentUserQuery.CurrentUser currentUser;
    private static GetCustomerQuery.CustomerById customer;
    private TextInputEditText gender, phone, address;
    private String token;
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
        name = v.findViewById(R.id.nav_txt_name);
        email = v.findViewById(R.id.nav_txt_email);
        gender = v.findViewById(R.id.acc_gender);
        phone = v.findViewById(R.id.acc_phone);
        address = v.findViewById(R.id.acc_address);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.createLoadingDialog();
                popup.show();
                logOut();
            }
        });
        token = PreferenceUtil.getAuthToken(getContext());
        setCurrentUser();

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

    public void setCurrentUser() {
        GraphqlClient.getApolloClient(token, false).query(CurrentUserQuery.builder().build())
                .enqueue(new ApolloCall.Callback<CurrentUserQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<CurrentUserQuery.Data> response) {
                        currentUser = response.data().currentUser();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                name.setText(currentUser.lastName()+ " " + currentUser.firstName());
                                getCustomerInfo();
                            }
                        });
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.e("current_user_err", e.getCause() +" - "+e);
                    }
                });
    }

    public void getCustomerInfo(){
        GraphqlClient.getApolloClient(token, false)
                .query(GetCustomerQuery.builder()
                        .id(String.valueOf(currentUser.id())).build())
                .enqueue(new ApolloCall.Callback<GetCustomerQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<GetCustomerQuery.Data> response) {
                        customer = response.data().customerById();
                        if ( customer != null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    email.setText(customer.email());
                                    gender.setText(customer.gender()?"Female":"Male");
                                    phone.setText(customer.phone());
                                    address.setText(customer.address());
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.e("customer_err", e.getCause() +" - "+e);
                    }
                });
    }

}
