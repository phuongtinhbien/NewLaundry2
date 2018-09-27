package com.example.vuphu.newlaundry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.Popup.Popup;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;
import com.example.vuphu.newlaundry.Graphql.Services;
import com.example.vuphu.newlaundry.Main.MainActivity;
import com.example.vuphu.newlaundry.Utils.Util;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private MaterialButton btnLogin;
    private Popup popup;
    private static String token;
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
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.createLoadingDialog();
                popup.show();
                authentication(email.getText().toString(), password.getText().toString());
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
        startActivity(new Intent(this, SignUpActivity.class));
    }

    public String authentication(String email, String pass) {

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

                            popup.hide();
                            PreferenceUtil.setAuthToken(getApplicationContext(), token);
                            PreferenceUtil.setLastCheckedAuthTokenTime(getApplicationContext(), Calendar.getInstance().getTimeInMillis());
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
        return token;
    }

    private void successLogin(){
        String preToken = PreferenceUtil.getAuthToken(getApplicationContext());
        if (!Util.isEmptyorNull(preToken)) {
            popup.createLoadingDialog();
            popup.show();
            final Services services = new Services(
                    GraphqlClient.getApolloClient(preToken,
                            false));
            CurrentUserQuery.CurrentUser currentUser= services.currentUser();
            if (currentUser!= null){
                popup.hide();
                Toast.makeText(LoginActivity.this, R.string.login_successfuly, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }
    }
}
