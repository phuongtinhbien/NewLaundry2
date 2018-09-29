package com.example.vuphu.newlaundry.Authen;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.CurrentUserQuery;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.Graphql.Services;
import com.example.vuphu.newlaundry.ItemListDialogFragment;
import com.example.vuphu.newlaundry.Main.MainActivity;
import com.example.vuphu.newlaundry.Popup.Popup;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.UpdateCustomerMutation;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;
import com.example.vuphu.newlaundry.Utils.Util;
import com.example.vuphu.newlaundry.type.CustomerPatch;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetUpInfoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private List<String> genderList;
    private Popup popup;
    private TextInputEditText gender, phone, address;
    private FloatingActionButton setUp;
    private String token;
    private static CurrentUserQuery.CurrentUser currentUser;
    private static UpdateCustomerMutation.Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_info);
        initToolbar();
        init();
    }

    private void init() {

        popup = new Popup(this);
        token = PreferenceUtil.getAuthToken(getApplicationContext());
        setCurrentUser();
        genderList = Arrays.asList(getResources().getStringArray(R.array.arr_gender));
        gender = findViewById(R.id.set_up_gender);
        phone = findViewById(R.id.set_up_phone);
        address = findViewById(R.id.set_up_address);
        setUp = findViewById(R.id.set_up_btn_finish);
        Util.showHideCursor(gender);
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               popup.createListPopup(genderList, getResources().getString(R.string.input_gender), gender);
               popup.show();
            }
        });

        setUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    popup.createLoadingDialog();
                    popup.show();
                    setUpInfo();
                }
            }
        });

    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Set up information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public boolean validate(){
        boolean validate = true;
        if (gender.getText().length()<=0){
            TextInputLayout layout = findViewById(R.id.set_up_gender_layout);
            layout.setError(getResources().getString(R.string.error_field_required));
            validate = false;
        }
        if (phone.getText().length()<=0){
            TextInputLayout layout = findViewById(R.id.set_up_phone_layout);
            layout.setError(getResources().getString(R.string.error_field_required));
            validate = false;
        }
        if (address.getText().length()<=0){
            TextInputLayout layout = findViewById(R.id.set_up_address_layout);
            layout.setError(getResources().getString(R.string.error_field_required));
            validate = false;
        }

        return validate;
    }

    private void setUpInfo (){


        CustomerPatch customerPatch = CustomerPatch.builder()
                .address(address.getText().toString())
                .gender( gender.getText().toString().equals("Female")?true:false)
                .phone(phone.getText().toString()).build();
        GraphqlClient.getApolloClient(token, false)
                .mutate(UpdateCustomerMutation.builder()
                        .id(String.valueOf(currentUser.id()))
                        .customerPath(customerPatch).build())
                .enqueue(new ApolloCall.Callback<UpdateCustomerMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<UpdateCustomerMutation.Data> response) {
                        customer  = response.data().updateCustomerById().customer();
                        SetUpInfoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (customer != null){
                                    popup.hide();
                                    View.OnClickListener onClickListener = new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(SetUpInfoActivity.this, MainActivity.class));
                                            finish();
                                        }
                                    };
                                    PreferenceUtil.setSetUpInfo(getApplicationContext(), true);
                                    popup.createSuccessDialog(R.string.notify_successfully_saved, R.string.use_now, onClickListener);
                                    popup.show();
                                }
                                else{
                                    popup.createFailDialog(R.string.notify_fail, R.string.btn_fail);
                                    popup.show();
                                }
                            }
                        });

                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.e("set_up_info_err", e.getCause() +" - "+e);
                    }
                });

    }

    public void setCurrentUser() {
        GraphqlClient.getApolloClient(token, false).query(CurrentUserQuery.builder().build())
                .enqueue(new ApolloCall.Callback<CurrentUserQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<CurrentUserQuery.Data> response) {
                        currentUser = response.data().currentUser();
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Log.e("current_user_err", e.getCause() +" - "+e);
                    }
                });
    }
}
