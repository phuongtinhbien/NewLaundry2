package com.example.vuphu.newlaundry.Authen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.Popup.Popup;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.RegisterUserMutation;
import com.example.vuphu.newlaundry.Utils.StringKey;
import com.example.vuphu.newlaundry.type.RegisterUserInput;

import org.jetbrains.annotations.NotNull;

import static com.example.vuphu.newlaundry.Graphql.Services.USER_TYPE;

public class SignUpActivity extends AppCompatActivity {

    private EditText lastName, firstName, email, password;
    private MaterialButton signUp;
    private static RegisterUserMutation.RegisterUser registerUser;
    private Popup popup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
    }

    private void init() {
        popup = new Popup(this);
        lastName = findViewById(R.id.sign_up_last_name);
        firstName = findViewById(R.id.sign_up_first_name);
        email = findViewById(R.id.sign_up_email);
        password = findViewById(R.id.sign_up_password);
        signUp = findViewById(R.id.btn_sign_up);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()){

                    popup.createLoadingDialog();
                    popup.show();
                    registerUser(email.getText().toString(),
                            password.getText().toString(),
                            firstName.getText().toString(),
                            lastName.getText().toString());

                }
            }
        });
    }

    public boolean validate(){
        boolean validate = true;
        if (lastName.getText().length()==0){
            TextInputLayout layout = findViewById(R.id.sign_up_last_name_layout);
            layout.setError(getResources().getString(R.string.input_last_name_err));
            validate = false;
        }
        if (firstName.getText().length()==0){
            TextInputLayout layout = findViewById(R.id.sign_up_first_name_layout);
            layout.setError(getResources().getString(R.string.input_first_name_err));
            validate = false;
        }
        if (email.getText().length() == 0){
            TextInputLayout layout = findViewById(R.id.sign_up_email_layout);
            layout.setError(getResources().getString(R.string.input_email_err));
            validate = false;
        }

        if (password.getText().length() == 0){
            TextInputLayout layout = findViewById(R.id.sign_up_password_layout);
            layout.setError(getResources().getString(R.string.input_password_err));
            validate= false;
        }
        return validate;
    }

    public RegisterUserMutation.RegisterUser registerUser (final String email, final String pass, String firstName, String lastName){
        final RegisterUserInput input = RegisterUserInput.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .password(pass)
                .userType(USER_TYPE).build();

        GraphqlClient.getApolloClient(null, true).mutate(RegisterUserMutation.builder().registerUserInput(input)
                .build()).enqueue(new ApolloCall.Callback<RegisterUserMutation.Data>() {
            @Override
            public void onResponse(@NotNull final Response<RegisterUserMutation.Data> response) {
                Log.e("register_user", response.data().registerUser().toString());
                registerUser = response.data().registerUser();
                SignUpActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (registerUser.user() != null){
                            popup.hide();
                            View.OnClickListener onClickListener = new View.OnClickListener() {
                                @SuppressLint("RestrictedApi")
                                @Override
                                public void onClick(View view) {
                                    Bundle account = new Bundle();
                                    account.putString(StringKey.CUSTOMER_EMAIL,email);
                                    account.putString(StringKey.CUSTOMER_PASSWORD, pass);
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    intent.putExtra(StringKey.CUSTOMER_ACCOUNT, account);
                                    setResult(RESULT_OK, intent);
                                    popup.hide();
                                    finish();
                                }
                            };
                            popup.createSuccessDialog(R.string.notify_successfully_create_account,R.string.btn_login,onClickListener);
                            popup.show();
                        }
                        else if (registerUser.user() == null){
                            popup.hide();
                            popup.createFailDialog(R.string.notify_fail_email_exist, R.string.btn_fail);
                            popup.show();
                        }
                        else {
                            popup.hide();
                            popup.createFailDialog(response.errors().get(0).message(), "Fail");
                            popup.show();
                        }
                    }
                });
            }
            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e("register_err", e.getCause() +" - "+e);
            }
        });
        return registerUser;
    }


}
