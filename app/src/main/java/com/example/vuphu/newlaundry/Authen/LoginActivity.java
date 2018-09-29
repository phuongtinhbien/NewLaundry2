package com.example.vuphu.newlaundry.Authen;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.AuthenticateMutation;
import com.example.vuphu.newlaundry.CurrentUserQuery;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.Popup.Popup;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;
import com.example.vuphu.newlaundry.Graphql.Services;
import com.example.vuphu.newlaundry.Main.MainActivity;
import com.example.vuphu.newlaundry.Utils.StringKey;
import com.example.vuphu.newlaundry.Utils.Util;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity{

    private TextInputEditText email;
    private TextInputEditText password;
    private MaterialButton btnLogin;
    private Popup popup;
    private static String token;
    private static CurrentUserQuery.CurrentUser currentUser;
    private boolean newAccount;

    public static final int REQUEST_CODE=1123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        successLogin();
    }

    private  void init(){
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.btn_login);
        popup = new Popup(this);
        newAccount = PreferenceUtil.getSetUpInfo(getApplicationContext());
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    popup.createLoadingDialog();
                    popup.show();
                    authentication(email.getText().toString(), password.getText().toString());
                }
            }
        });

    }

    public boolean validate(){

        boolean validate = true;
        if (email.getText().length() == 0){
            TextInputLayout layout = findViewById(R.id.login_email_layout);
            layout.setError(getResources().getString(R.string.input_email_err));
            validate = false;
        }

        if (password.getText().length() == 0){
            TextInputLayout layout = findViewById(R.id.login_password_layout);
            layout.setError(getResources().getString(R.string.input_password_err));
            validate= false;
        }
        return validate;


    }

    public void signUp(View v){
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void authentication(String email, String pass) {

        GraphqlClient.getApolloClient(null, true).mutate(AuthenticateMutation.builder()
                .email(email)
                .password(pass)
                .userType(Services.USER_TYPE)
                .build()).enqueue(new ApolloCall.Callback<AuthenticateMutation.Data>() {
            @Override
            public void onResponse(@NotNull final Response<AuthenticateMutation.Data> response) {
                /*   Log.i("authentication_token", response.data().authenticate().jwt());*/
                token = response.data().authenticate().jwt();
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!Util.isEmptyorNull(token)){
                            PreferenceUtil.setAuthToken(getApplicationContext(), token);
                            PreferenceUtil.setLastCheckedAuthTokenTime(getApplicationContext(), Calendar.getInstance().getTimeInMillis());
                            popup.hide();
                            successLogin();
                        }
                        else {
                            popup.hide();
                            popup.createFailDialog(R.string.email_password_wrong, R.string.btn_fail);
                            popup.show();
                        }

                        if (response.hasErrors()){
                            popup.hide();
                            popup.createFailDialog(response.errors().get(0).message(), "Fail");
                            popup.show();
                        }

                    }

                });

            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e("authentication_err", e.getCause() + " - " + e);
            }

        });
    }

    private void successLogin(){
        token = PreferenceUtil.getAuthToken(getApplicationContext());
        if (!Util.isEmptyorNull(token)) {
            popup.createLoadingDialog();
            popup.show();
            GraphqlClient.getApolloClient(token, false).query(CurrentUserQuery.builder().build())
                    .enqueue(new ApolloCall.Callback<CurrentUserQuery.Data>() {
                @Override
                public void onResponse(@NotNull Response<CurrentUserQuery.Data> response) {
                    currentUser = response.data().currentUser();
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (currentUser!= null) {
                                    popup.hide();

                                    if (!newAccount){
                                        Toast.makeText(LoginActivity.this, R.string.welcome, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), SetUpInfoActivity.class));
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(LoginActivity.this, R.string.login_successfuly, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();
                                    }

                                }
                                else{
                                    popup.hide();
                                    popup.createFailDialog(R.string.notify_fail, R.string.btn_fail);
                                    popup.show();
                                }
                            }
                        });

                }

                @Override
                public void onFailure(@NotNull ApolloException e) {
                    Log.e("current_user_err", e.getCause() +" - "+e);

                }
            });

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Bundle bundle = data.getBundleExtra(StringKey.CUSTOMER_ACCOUNT);
            email.setText(bundle.getString(StringKey.CUSTOMER_EMAIL));
            password.setText(bundle.getString(StringKey.CUSTOMER_PASSWORD));
            PreferenceUtil.setSetUpInfo(getApplicationContext(), false);
            newAccount = false;
        }
    }
}
